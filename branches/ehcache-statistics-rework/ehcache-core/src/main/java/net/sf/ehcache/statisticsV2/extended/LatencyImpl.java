/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.ehcache.statisticsV2.extended;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.sf.ehcache.statisticsV2.extended.ExtendedStatistics.Latency;
import net.sf.ehcache.statisticsV2.extended.ExtendedStatistics.Statistic;
import org.terracotta.statistics.SourceStatistic;
import org.terracotta.statistics.Time;
import org.terracotta.statistics.ValueStatistic;
import org.terracotta.statistics.archive.Timestamped;
import org.terracotta.statistics.derived.EventParameterSimpleMovingAverage;
import org.terracotta.statistics.derived.LatencySampling;
import org.terracotta.statistics.observer.OperationObserver;

/**
 *
 * @author cdennis
 */
class LatencyImpl<T extends Enum<T>> implements Latency {
    private final SourceStatistic<OperationObserver<T>> source;
    private final LatencySampling<T> latencySampler;
    private final EventParameterSimpleMovingAverage average;
    private final StatisticImpl<Long> minimumStatistic;
    private final StatisticImpl<Long> maximumStatistic;
    private final StatisticImpl<Double> averageStatistic;

    private boolean alive = false;
    private long touchTimestamp = -1;
    
    public LatencyImpl(SourceStatistic<OperationObserver<T>> statistic, Set<T> targets, long averagePeriod, TimeUnit averageUnit, ScheduledExecutorService executor, int historySize, long historyPeriod, TimeUnit historyUnit) {
        this.average = new EventParameterSimpleMovingAverage(averagePeriod, averageUnit);
        this.minimumStatistic = new StatisticImpl<Long>(average.minimumStatistic(), executor, historySize, historyPeriod, historyUnit);
        this.maximumStatistic = new StatisticImpl<Long>(average.maximumStatistic(), executor, historySize, historyPeriod, historyUnit);
        this.averageStatistic = new StatisticImpl<Double>(average.averageStatistic(), executor, historySize, historyPeriod, historyUnit);
        this.latencySampler = new LatencySampling(targets, 1.0);
        latencySampler.addDerivedStatistic(average);
        this.source = statistic;
    }

    public synchronized void start() {
        if (!alive) {
            source.addDerivedStatistic(latencySampler);
            minimumStatistic.startSampling();
            maximumStatistic.startSampling();
            averageStatistic.startSampling();
        }
    }

    public synchronized void stop() {
        if (alive) {
            source.removeDerivedStatistic(latencySampler);
            minimumStatistic.stopSampling();
            maximumStatistic.stopSampling();
            averageStatistic.stopSampling();
        }
    }

    @Override
    public Statistic<Long> minimum() {
        return minimumStatistic;
    }

    @Override
    public Statistic<Long> maximum() {
        return maximumStatistic;
    }

    @Override
    public Statistic<Double> average() {
        return averageStatistic;
    }

    private synchronized void touch() {
        touchTimestamp = Time.absoluteTime();
        start();
    }
    
    public synchronized boolean expire(long expiry) {
        if (touchTimestamp < expiry) {
            stop();
            return true;
        } else {
            return false;
        }
    }
    
    class StatisticImpl<T> implements Statistic<T> {

        private final ValueStatistic<T> value;
        private final SampledStatistic<T> history;

        public StatisticImpl(ValueStatistic<T> value, ScheduledExecutorService executor, int historySize, long historyPeriod, TimeUnit historyUnit) {
            this.value = value;
            this.history = new SampledStatistic<T>(value, executor, historySize, historyPeriod, historyUnit);
        }

        @Override
        public T value() {
            touch();
            return value.value();
        }

        @Override
        public List<Timestamped<T>> history() throws UnsupportedOperationException {
            touch();
            return history.history();
        }

        private void startSampling() {
            history.startSampling();
        }
        
        private void stopSampling() {
            history.stopSampling();
        }
    }
}