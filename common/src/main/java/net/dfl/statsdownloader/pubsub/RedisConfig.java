package net.dfl.statsdownloader.pubsub;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {
	
	/*
	@Value("${redis.url}")
	private String redisUrl;
	
	@Value("${spring.redis.pool.max-active}")
	private int redisPoolMaxActive;
	
	@Value("${spring.redis.pool.max-idle}")
	private int redisPoolMaxIdle;
	
	@Value("${spring.redis.pool.min-idle}")
	private int redisPoolMinIdle;
	

	@Bean
	public JedisConnectionFactory redisConnectionFactory() {
		try {
			URI redisURI = new URI(redisUrl);
			
			JedisPoolConfig poolConfig = new JedisPoolConfig();
			poolConfig.setMaxTotal(redisPoolMaxActive);
			poolConfig.setMaxIdle(redisPoolMaxIdle);
			poolConfig.setMinIdle(redisPoolMinIdle);
			poolConfig.setTestOnBorrow(true);
			poolConfig.setTestOnReturn(true);
			poolConfig.setTestWhileIdle(true);
			
			JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(poolConfig);
			jedisConnectionFactory.setHostName(redisURI.getHost());
			jedisConnectionFactory.setPort(redisURI.getPort());
			jedisConnectionFactory.setPassword(redisURI.getUserInfo().split(":",2)[1]);
					
			return jedisConnectionFactory;
		} catch (URISyntaxException ex) {
			throw new RuntimeException("Redis couldn't be configured from URL in REDIS_URL env var: " + System.getenv("REDIS_URL"));
		}
	}
	*/
	
	@Bean
    public ChannelTopic topic() {
        return new ChannelTopic("pubsub:queue");
    }
	    
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        final RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(connectionFactory);
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}
