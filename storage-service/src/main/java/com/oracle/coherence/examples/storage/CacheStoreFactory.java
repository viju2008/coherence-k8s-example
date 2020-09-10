package com.oracle.coherence.examples.storage;

import com.tangosol.net.cache.CacheStore;

/**
 * @author Jonathan Knight  2020.09.09
 */
public class CacheStoreFactory {

    public static CacheStore<?, ?> createCacheStore(String cacheName) throws Exception {
        if ("students".equalsIgnoreCase(cacheName)) {
            return new StudentCacheStore();
        }

        throw new IllegalArgumentException("Cannot create a CacheStore for cache " + cacheName);
    }
}
