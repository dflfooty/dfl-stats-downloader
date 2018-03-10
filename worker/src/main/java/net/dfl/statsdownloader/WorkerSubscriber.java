package net.dfl.statsdownloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import net.dfl.statsdownloader.model.Job;
import net.dfl.statsdownloader.pubsub.RedisMessageSubscriber;

@Service
public class WorkerSubscriber implements RedisMessageSubscriber {
	
	private static final Logger log = LoggerFactory.getLogger(WorkerSubscriber.class);
	
	@Autowired
	WorkerService worker;
	
    public void onMessage(final Message redisMessage, final byte[] pattern) {
        
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        Object obj = serializer.deserialize(redisMessage.getBody());
        
        if(obj != null && obj instanceof Job) {
        		Job job = (Job) obj;
        		log.info("Channel: {} - Received job: {}", redisMessage.getChannel(), job);
        		worker.work(job);
        } else {
        		log.info("Channel: {} - Failed to deserialize message", redisMessage.getChannel());
        }
    }
}
