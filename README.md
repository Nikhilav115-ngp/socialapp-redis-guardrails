# SocialApp Backend Assignment

## Overview

This project is a Spring Boot backend microservice implementing Redis-based guardrails, concurrency protection, virality scoring, and notification batching.

The assignment focuses on:
- Redis atomic operations
- Thread safety
- Concurrent request handling
- Stateless backend design
- Scheduled notification batching

---

# Tech Stack

- Java 17
- Spring Boot 3
- MySQL
- Redis / Memurai
- Spring Data JPA
- Spring Data Redis

---

# Features

## 1. Virality Engine (Redis)

Real-time virality score stored in Redis.

### Scoring Rules
- Bot Reply = +1
- Human Like = +20
- Human Comment = +50

Redis Key:

post:{id}:virality_score

## 2. Horizontal Cap (Concurrency Protection)

A single post cannot receive more than 100 bot replies.

Redis Key:
post:{id}:bot_count

If limit exceeds:
429 Too Many Requests

## 3. Vertical Cap

Comment depth cannot exceed 20 levels.

## 4. Cooldown Cap

A specific bot cannot interact with the same human user more than once every 10 minutes.

Redis TTL Key:
cooldown:bot:{botId}:user:{userId}

## 5. Notification Engine

Bot notifications are throttled using Redis.

### Behavior

*  First interaction → immediate notification
*  Repeated interactions → queued in Redis
*  Scheduler summarizes queued notifications every 5 minutes

Redis Queue:
user:{id}:pending_notifs


##  Concurrency Testing

The system was tested using Postman Runner with:
200 concurrent bot requests

Result:
Exactly 100 comments stored successfully
Remaining requests rejected safely

##  API Endpoints

*  Create Post
POST /api/posts

*  Add Comment
POST /api/comments/{postId}

*  Like Post
POST /api/posts/{postId}/like

*  Bot Reply
POST /api/bot/reply

Example Body:


{
  "postId": 1,
  "botId": 101,
  "userId": 1
}

##  Auto Seed Data

On application startup:
* Sample user is created
* Sample post is created

This allows immediate API testing without manual database setup.

##  Running the Project

1. Start MySQL
Ensure MySQL is running locally.

2. Start Redis / Memurai

Default Redis port:
6379

3. Run Spring Boot

# Windows
.\mvnw.cmd spring-boot:run

# Mac/Linux
./mvnw spring-boot:run

##  Notes

* Redis is used as the real-time guardrail layer.

* MySQL is used instead of PostgreSQL for local development and testing.

* Docker setup was not used in this submission.

* The application remains stateless.

* No in-memory HashMaps or static counters are used.

* Sample users and posts are auto-seeded at startup for quick testing.

## Author

Nikhil Vaidya



