package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// REST controller for account and message endpoints
@RestController
public class SocialMediaController {

    private final AccountService  accountService;
    private final MessageService  messageService;

    @Autowired
    public SocialMediaController(
        AccountService  accountService,
        MessageService  messageService
    ) {
        this.accountService  = accountService;
        this.messageService  = messageService;
    }

    // === Account Routes ===

    @PostMapping("/register")
    public ResponseEntity<Account> registerAccount(
        @RequestBody Account account
    ) {
        return accountService.registerAccount(account);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(
        @RequestBody Account account
    ) {
        try {
            ResponseEntity<Account> response = accountService.login(account);
            return ResponseEntity
                    .status(response.getStatusCode())
                    .body(response.getBody());
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }
    }

    // === Message: Create ===

    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(
        @RequestBody Message message
    ) {
        return messageService.createMessage(message);
    }

    // === Message: Read ===

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(
        @PathVariable Integer messageId
    ) {
        return messageService.getMessageById(messageId);
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccountId(
        @PathVariable Integer accountId
    ) {
        return messageService.getMessagesByAccountId(accountId);
    }

    // === Message: Update ===

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Object> updateMessageText(
        @PathVariable Integer messageId,
        @RequestBody Message newMessageText
    ) {
        return messageService.updateMessageText(messageId, newMessageText);
    }

    // === Message: Delete ===

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Object> deleteMessage(
        @PathVariable Integer messageId
    ) {
        return messageService.deleteMessageById(messageId);
    }
}
