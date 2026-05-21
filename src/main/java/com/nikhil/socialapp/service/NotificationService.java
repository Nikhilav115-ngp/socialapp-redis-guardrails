package com.nikhil.socialapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class NotificationService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // Redis Keys
    private static final String COOLDOWN_PREFIX = "notif_cooldown:user:";
    private static final String PENDING_PREFIX = "user:pending_notifs:";

    // SEND NOTIFICATION

    public void sendNotification(Long userId, String message) {

        String cooldownKey = COOLDOWN_PREFIX + userId;
        String pendingKey = PENDING_PREFIX + userId;

        Boolean hasCooldown = redisTemplate.hasKey(cooldownKey);

        // IF COOLDOWN ACTIVE → ADD TO QUEUE

        if (Boolean.TRUE.equals(hasCooldown)) {

            redisTemplate.opsForList().rightPush(pendingKey, message);

            System.out.println("Notification queued for user: " + userId);

        } else {

            // SEND IMMEDIATELY

            System.out.println("Push Notification Sent: " + message);

            // Set 15-minute cooldown
            redisTemplate.opsForValue()
                    .set(cooldownKey, "1", Duration.ofMinutes(15));
        }
    }

    // ADD DIRECTLY TO QUEUE

    public void addToQueue(Long userId, String message) {

        String pendingKey = PENDING_PREFIX + userId;

        redisTemplate.opsForList().rightPush(pendingKey, message);
    }

    // GET PENDING COUNT

    public long getPendingCount(Long userId) {

        String pendingKey = PENDING_PREFIX + userId;

        Long size = redisTemplate.opsForList().size(pendingKey);

        return size == null ? 0 : size;
    }

    // CLEAR QUEUE

    public void clearNotifications(Long userId) {

        String pendingKey = PENDING_PREFIX + userId;

        redisTemplate.delete(pendingKey);
    }
}