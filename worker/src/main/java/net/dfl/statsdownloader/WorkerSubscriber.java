package net.dfl.statsdownloader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import net.dfl.statsdownloader.model.Job;
import net.dfl.statsdownloader.pubsub.RedisMessageSubscriber;

@Service
public class WorkerSubscriber implements RedisMessageSubscriber {
	
	@Autowired
	WorkerService worker;
	
    public void onMessage(final Message redisMessage, final byte[] pattern) {
        
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        Object obj = serializer.deserialize(redisMessage.getBody());
        
        if(obj != null && obj instanceof Job) {
        		Job job = (Job) obj;
        		System.out.println("Recived Job: " + job);
        		try {
        				worker.work(job);
				} catch (Exception e) {
					e.printStackTrace();
				}
        } else {
        		System.out.println("Message received on channel <" + new String(redisMessage.getChannel()) + ">: failed to deserialize");
        }
    }
	
	
}
