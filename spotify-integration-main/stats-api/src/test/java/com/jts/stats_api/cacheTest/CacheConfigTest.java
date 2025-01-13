package com.jts.stats_api.cacheTest;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.jts.stats_api.config.CacheConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class CacheConfigTest {

    private CacheConfig cacheConfig;

    @BeforeEach
    public void setUp() {
        cacheConfig = new CacheConfig();
    }

    @Test
    public void testCacheManager() {
        CaffeineCacheManager cacheManager = cacheConfig.cacheManager();
        assertNotNull(cacheManager);
        assertNotNull(cacheManager.getCache("testCache"));
    }

    @Test
    public void testCaffeineCacheBuilder() {
        Caffeine<Object, Object> caffeine = cacheConfig.caffeineCacheBuilder();
        assertNotNull(caffeine);
        assertEquals(30, caffeine.build().policy().expireAfterWrite().get().getExpiresAfter(TimeUnit.SECONDS));
        assertEquals(100, caffeine.build().policy().eviction().get().getMaximum());
    }
}