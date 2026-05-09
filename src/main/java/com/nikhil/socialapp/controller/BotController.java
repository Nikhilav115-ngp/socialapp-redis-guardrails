package com.nikhil.socialapp.controller;

import com.nikhil.socialapp.dto.BotReplyRequest;
import com.nikhil.socialapp.service.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bot")
public class BotController {

    @Autowired
    private BotService botService;

    @PostMapping("/reply")
    public ResponseEntity<String> botReply(@RequestBody BotReplyRequest request) {

        

        String response = botService.processBotReply(
                request.getPostId(),
                request.getBotId(),
                request.getUserId()
        );

        // 429 Too Many Requests
        if (response.contains("Rejected")) {
            return ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(response);
        }

        // 200 OK
        return ResponseEntity.ok(response);
    }
}