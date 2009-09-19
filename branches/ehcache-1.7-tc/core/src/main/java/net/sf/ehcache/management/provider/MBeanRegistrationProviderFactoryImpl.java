/**
 *  Copyright 2003-2009 Terracotta, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package net.sf.ehcache.management.provider;

import net.sf.ehcache.management.sampled.SampledMBeanRegistrationProvider;

/**
 * Defult implementation of {@link MBeanRegistrationProvider}
 * 
 * <p />
 * 
 * @author <a href="mailto:asanoujam@terracottatech.com">Abhishek Sanoujam</a>
 * @since 1.7
 */
public class MBeanRegistrationProviderFactoryImpl implements
        MBeanRegistrationProviderFactory {

    /**
     * System property that defines if sampled mbeans should be used or not.
     */
    public static final String USE_SAMPLED_MBEANS_PROP_NAME = "net.sf.ehcache.jmx.use-sampled-mbeans";

    private static final MBeanRegistrationProvider DEFAULT_PROVIDER = new NullMBeanRegistrationProvider();

    /**
     * {@inheritDoc}
     */
    public MBeanRegistrationProvider createMBeanRegistrationProvider() {
        MBeanRegistrationProvider provider;
        if (useSampledMBeans(false)) {
            provider = new SampledMBeanRegistrationProvider();
        } else {
            provider = DEFAULT_PROVIDER;
        }
        return provider;
    }

    private boolean useSampledMBeans(boolean defaultValue) {
        // temporary hack, probably should come from config or always return
        // true?
        String prop = System.getProperty(USE_SAMPLED_MBEANS_PROP_NAME);
        if (prop == null || "".equals(prop.trim())) {
            return defaultValue;
        }
        return "true".equalsIgnoreCase(prop);
    }

}
