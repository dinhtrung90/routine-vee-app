package com.vdt.veeapp.config;

import io.github.jhipster.config.JHipsterProperties;
import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.vdt.veeapp.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.vdt.veeapp.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.vdt.veeapp.domain.User.class.getName());
            createCache(cm, com.vdt.veeapp.domain.Authority.class.getName());
            createCache(cm, com.vdt.veeapp.domain.User.class.getName() + ".authorities");
            createCache(cm, com.vdt.veeapp.domain.Category.class.getName());
            createCache(cm, com.vdt.veeapp.domain.SubCategory.class.getName());
            createCache(cm, com.vdt.veeapp.domain.Category.class.getName() + ".subCategories");
            createCache(cm, com.vdt.veeapp.domain.Habit.class.getName());
            createCache(cm, com.vdt.veeapp.domain.Reminder.class.getName());
            createCache(cm, com.vdt.veeapp.domain.EventTimes.class.getName());
            createCache(cm, com.vdt.veeapp.domain.Habit.class.getName() + ".eventTimes");
            createCache(cm, com.vdt.veeapp.domain.UserProfile.class.getName());
            createCache(cm, com.vdt.veeapp.domain.FollowingRelationships.class.getName());
            createCache(cm, com.vdt.veeapp.domain.UserGroups.class.getName());
            createCache(cm, com.vdt.veeapp.domain.UserGroups.class.getName() + ".users");
            createCache(cm, com.vdt.veeapp.domain.UserGroups.class.getName() + ".userProfiles");
            createCache(cm, com.vdt.veeapp.domain.UserProfile.class.getName() + ".userGroups");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache == null) {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }
}
