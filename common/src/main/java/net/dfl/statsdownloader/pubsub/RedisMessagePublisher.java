package net.dfl.statsdownloader.pubsub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class RedisMessagePublisher implements MessagePublisher { 
	
	@Autowired
    private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
    private ChannelTopic topic;

	/*
    public void publish(final Job message) {
    		System.out.println("Publishing message: " + message);
    		System.out.println("To channel: " + topic.getTopic());
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
    */

	@Override
	public void publish(Object message) {
		System.out.println("Publishing message: " + message);
		System.out.println("To channel: " + topic.getTopic());
		redisTemplate.convertAndSend(topic.getTopic(), message);
	}
}
