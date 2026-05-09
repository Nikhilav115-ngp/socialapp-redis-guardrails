package com.nikhil.socialapp.scheduler;

import com.nikhil.socialapp.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class NotificationScheduler {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private NotificationService notificationService;

    private static final String PENDING_PREFIX = "user:pending_notifs:";

    // -----------------------------
    // RUN EVERY 5 MINUTES
    // -----------------------------
    @Scheduled(fixedRate = 300000)
    public void processPendingNotifications() {

        // get all users with pending notifications
        Set<String> keys = redisTemplate.keys(PENDING_PREFIX + "*");

        if (keys == null || keys.isEmpty()) {
            return;
        }

        for (String key : keys) {

            // extract userId from key
            String userIdStr = key.replace(PENDING_PREFIX, "");
            Long userId = Long.parseLong(userIdStr);

            // fetch all messages
            List<Object> messages = redisTemplate.opsForList().range(key, 0, -1);

            if (messages == null || messages.isEmpty()) {
                continue;
            }

            int count = messages.size();

            // create summary message
            String summary = "Summarized Notification: Bot + Comments + " + count + " interactions on your posts.";

            // simulate push notification
            System.out.println(" Sent to User " + userId + " → " + summary);

            // clear queue after sending
            notificationService.clearNotifications(userId);
        }
    }
}