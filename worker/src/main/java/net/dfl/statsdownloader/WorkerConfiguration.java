package net.dfl.statsdownloader;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import net.dfl.statsdownloader.pubsub.RedisMessageSubscriber;

@Configuration
public class WorkerConfiguration {
	@Bean
	public RedisMessageSubscriber redisMessageSubscriber() {
		return new WorkerSubscriber();
	}
	
	@Bean
    MessageListenerAdapter messageListenerAdapter(RedisMessageSubscriber redisMessageSubscriber) {
		MessageListenerAdapter messageListenerAdapter =  new MessageListenerAdapter(redisMessageSubscriber);
		messageListenerAdapter.setSerializer(new GenericJackson2JsonRedisSerializer());
		return messageListenerAdapter;
    }

    @Bean
    RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory, MessageListenerAdapter messageListenerAdapter, ChannelTopic topic) {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        System.out.println("Topic: " + topic.getTopic());
        container.addMessageListener(messageListenerAdapter, topic);
        return container;
    }
}
