package net.dfl.statsdownloader.pubsub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class RedisMessagePublisher implements MessagePublisher { 
	
	private static final Logger log = LoggerFactory.getLogger(MessagePublisher.class);
	
	@Autowired
    private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
    private ChannelTopic topic;
	
	@Override
	public void publish(Object message) {
		log.info("Channel: {} - Publishing: {}", topic.getTopic(), message);
		redisTemplate.convertAndSend(topic.getTopic(), message);
	}
}
