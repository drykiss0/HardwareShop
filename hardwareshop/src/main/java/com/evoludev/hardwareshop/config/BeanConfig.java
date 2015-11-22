package com.evoludev.hardwareshop.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import com.evoludev.hardwareshop.domain.CategoriesRefreshCacheLoader;

import net.sf.ehcache.Cache;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.CacheConfiguration.CacheExtensionFactoryConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration.Strategy;
import net.sf.ehcache.constructs.scheduledrefresh.ScheduledRefreshCacheExtensionFactory;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

@Configuration
@EnableScheduling
@EnableWebSocketMessageBroker
public class BeanConfig extends AbstractWebSocketMessageBrokerConfigurer implements SchedulingConfigurer {

	@Autowired
	private CategoriesRefreshCacheLoader cacheLoader;
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic");
		//config.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/hardwareCategories").withSockJS();
	}
	
	@Bean
	public DataSource dataSource() {
		
		EmbeddedDatabase db = new EmbeddedDatabaseBuilder()
			.setType(EmbeddedDatabaseType.HSQL)
			.addScript("db/sql/schema.sql")
			.addScript("db/sql/data.sql")
			.build();
		return db;
	}
	
	@Bean
	public JdbcTemplate getJdbcTemplate() {
		return new JdbcTemplate(dataSource());
	}	
	
	@Bean
	public Cache cache() {
		CacheExtensionFactoryConfiguration clfc = new CacheExtensionFactoryConfiguration()
				.properties("batchSize=10;quartzJobCount=2;cronExpression=* * * * * ?")
				.propertySeparator(";")
				.className(ScheduledRefreshCacheExtensionFactory.class.getName());
		
		Cache hardwareCategoryCache = new Cache(
				  new CacheConfiguration("hardwareCategoryCache", 0)
				    .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LFU)
				    .eternal(true)
				    .cacheExtensionFactory(clfc)
				    .persistence(new PersistenceConfiguration().strategy(Strategy.NONE)));
		return hardwareCategoryCache;
	}
	
	@Bean
	public CacheManager cacheManager() {
		
		cache().registerCacheLoader(cacheLoader);
		net.sf.ehcache.CacheManager manager = net.sf.ehcache.CacheManager.create();
		manager.addCache(cache());
		return new EhCacheCacheManager(manager);
	}
	
	@Bean
    public TaskScheduler taskScheduler() {
		return new ThreadPoolTaskScheduler();
    }
	
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(taskScheduler());
	}
}
