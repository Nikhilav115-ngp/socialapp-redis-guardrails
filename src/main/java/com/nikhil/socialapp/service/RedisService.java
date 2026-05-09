package com.nikhil.socialapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // -------------------------
    // KEY BUILDERS
    // -------------------------
    private String viralityKey(Long postId) {
        return "post:" + postId + ":virality_score";
    }

    private String botCountKey(Long postId) {
        return "post:" + postId + ":bot_count";
    }

    private String cooldownKey(Long botId, Long userId) {
        return "cooldown:bot:" + botId + ":user:" + userId;
    }

    // -------------------------
    // VIRALITY
    // -------------------------
    public void increaseVirality(Long postId, int value) {
        redisTemplate.opsForValue()
                .increment(viralityKey(postId), value);
    }

    // -------------------------
    // BOT COUNT ( FINAL FIX)
    // -------------------------
    public long safeIncreaseBotCount(Long postId) {

    String key = botCountKey(postId);

    Long count = redisTemplate.opsForValue().increment(key);

    if (count != null && count > 100) {
        // rollback
        redisTemplate.opsForValue().decrement(key);
        return -1;
    }

    return count;
}

    // -------------------------
    // COOLDOWN CHECK
    // -------------------------
    public boolean isCooldownActive(Long botId, Long userId) {
        return Boolean.TRUE.equals(
                redisTemplate.hasKey(cooldownKey(botId, userId))
        );
    }

    // -------------------------
    // SET COOLDOWN
    // -------------------------
    public void setCooldown(Long botId, Long userId) {
        redisTemplate.opsForValue()
                .set(
                        cooldownKey(botId, userId),
                        "1",
                        Duration.ofMinutes(10)
                );
    }
}