package com.oracle.coherence.examples.storage;

import com.tangosol.net.BackingMapManagerContext;
import com.tangosol.net.cache.CacheStore;
import com.tangosol.util.ResourceRegistry;

import org.springframework.context.ApplicationContext;

/**
 * @author Jonathan Knight  2020.09.09
 */
public class CacheStoreFactory {

    public static CacheStore<?, ?> createCacheStore(String cacheName, BackingMapManagerContext bmCtx) {
        ResourceRegistry registry = bmCtx.getManager().getCacheFactory().getResourceRegistry();
        ApplicationContext ctx = registry.getResource(ApplicationContext.class);

        if ("students".equalsIgnoreCase(cacheName)) {
            return ctx.getBean(StudentCacheStore.class);
        }

        throw new IllegalArgumentException("Cannot create a CacheStore for cache " + cacheName);
    }
}
