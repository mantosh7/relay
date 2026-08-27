package com.relay.relay.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

public class DistributedLockService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String LOCK_PREFIX = "job.lock:";
    private static final long LOCK_TTL_SECONDS = 30;

    public DistributedLockService(RedisTemplate<String, String> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public boolean acquireLock(String jobId){
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String key = LOCK_PREFIX + jobId;

        Boolean success = ops.setIfAbsent(key, "LOCKED", Duration.ofSeconds(LOCK_TTL_SECONDS));
        return Boolean.TRUE.equals(success);
    }

    public void releaseLock(String jobId){
        String key = LOCK_PREFIX + jobId ;
        redisTemplate.delete(jobId);
    }
}
