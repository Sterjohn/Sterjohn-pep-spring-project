package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // Added for proper response handling
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SocialMediaController {

    @Autowired
    AccountService accountService;

    @Autowired
    MessageService messageService;

    @PostMapping("/register")
    // Changed return type to ResponseEntity
    public ResponseEntity<Account> register(@RequestBody Account account) {
        Account registered = accountService.register(account);
        if (registered == null) {
            return ResponseEntity.status(409).build();
        }
        return ResponseEntity.ok(registered);
    }

    @PostMapping("/login")
    // Changed return type to ResponseEntity
    public ResponseEntity<Account> login(@RequestBody Account account) {
        Account loggedIn = accountService.login(account);
        if (loggedIn == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(loggedIn);
    }

    @PostMapping("/messages")
    // Changed return type to ResponseEntity
    public ResponseEntity<Message> postMessage(@RequestBody Message message) {
        Message created = messageService.createMessage(message);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/messages")
    // Changed return type to ResponseEntity
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    @GetMapping("/messages/{message_id}")
    // Changed return type to ResponseEntity
    public ResponseEntity<Message> getMessageById(@PathVariable("message_id") int messageId) {
        Message found = messageService.getMessageById(messageId);
        if (found == null) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok(found);
    }

    @GetMapping("/accounts/{user_id}/messages")
    // Changed return type to ResponseEntity
    public ResponseEntity<List<Message>> getMessagesByUser(@PathVariable("user_id") int userId) {
        return ResponseEntity.ok(messageService.getMessagesByUser(userId));
    }

    @PatchMapping("/messages/{message_id}")
    // Changed return type to ResponseEntity
    public ResponseEntity<Message> updateMessage(@PathVariable("message_id") int messageId, @RequestBody Message message) {
        Message updated = messageService.updateMessage(messageId, message);
        if (updated == null) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/messages/{message_id}")
    // Changed return type to ResponseEntity
    public ResponseEntity<?> deleteMessage(@PathVariable("message_id") int messageId) {
        boolean deleted = messageService.deleteMessage(messageId);
        if (!deleted) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok().build();
    }
}
