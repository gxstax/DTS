package com.heytea.dtc.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 缓存配置
 * </p>
 *
 * @author Ant
 * @since 2022/1/5 3:33 下午
 */
@Configuration
public class CacheConfig {

    @Bean
    @Primary
    public CacheManager caffeineCacheManager() {
        List<CaffeineCache> caffeineCaches = new ArrayList<>();

        caffeineCaches.add(new CaffeineCache("kafka-topic-config",
                Caffeine.newBuilder()
                        .maximumSize(10_000)
                        .expireAfterWrite(30, TimeUnit.MINUTES)
                .build())
        );

        caffeineCaches.add(new CaffeineCache("topic-partition-rule",
                        Caffeine.newBuilder()
                                .maximumSize(10_000)
                                .expireAfterWrite(30, TimeUnit.MINUTES)
                                .build())
        );

        caffeineCaches.add(new CaffeineCache("field-filter-config",
                Caffeine.newBuilder()
                        .maximumSize(10_000)
                        .expireAfterWrite(30, TimeUnit.MINUTES)
                        .build())
        );

        caffeineCaches.add(new CaffeineCache("kafka-db-mapping-config",
                Caffeine.newBuilder()
                        .maximumSize(10_000)
                        .expireAfterWrite(30, TimeUnit.MINUTES)
                        .build())
        );

        caffeineCaches.add(new CaffeineCache("rabbit-db-mapping-config",
                Caffeine.newBuilder()
                        .maximumSize(10_000)
                        .expireAfterWrite(30, TimeUnit.MINUTES)
                        .build())
        );

        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(caffeineCaches);
        return cacheManager;
    }

}
