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

package net.sf.ehcache.store;

import net.sf.ehcache.Ehcache;

/**
 * Factory for creating Store implementations
 *
 * @author teck
 * @since 1.7
 */
public interface StoreFactory {

    /**
     * Create a Store for the given cache
     *
     * @param cache the cache will backed by the returned store
     * @return store instance
     */
    Store create(Ehcache cache);

    /**
     * Return Terracotta version
     */
    String getTCVersion();
}
