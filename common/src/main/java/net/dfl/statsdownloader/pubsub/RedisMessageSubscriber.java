package net.dfl.statsdownloader.pubsub;

import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
public interface RedisMessageSubscriber extends MessageListener {}
