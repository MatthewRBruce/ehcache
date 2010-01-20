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

package net.sf.ehcache.transaction.xa;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.transaction.Transaction;
import javax.transaction.xa.Xid;

import net.sf.ehcache.transaction.xa.EhCacheXAResourceImpl.Version;

public class TransactionTableFactoryImpl implements TransactionTableFactory {

    public ConcurrentMap<Transaction, XaTransactionContext> getTransactionDataTable() {
        return new ConcurrentHashMap<Transaction, XaTransactionContext>();
    }

    public ConcurrentMap<Xid, Transaction> getTransactionXids() {
        return new ConcurrentHashMap<Xid, Transaction>();
    }

    public ConcurrentMap<Transaction, Long> getTxnVersions() {
        return new ConcurrentHashMap<Transaction, Long>();
    }

    public ConcurrentMap<Object, Version> getVersionStore() {
        return new ConcurrentHashMap<Object, Version>();
    }

    public ConcurrentMap<Xid, Xid> getPrepareXids() {
        return new ConcurrentHashMap<Xid, Xid>();
    }

}
