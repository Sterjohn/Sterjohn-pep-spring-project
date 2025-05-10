package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SocialMediaController {

    @Autowired
    AccountService accountService;

    @Autowired
    MessageService messageService;

    @PostMapping("/register")
    public Account register(@RequestBody Account account) {
        return accountService.register(account);
    }

    @PostMapping("/login")
    public Account login(@RequestBody Account account) {
        return accountService.login(account);
    }

    @PostMapping("/messages")
    public Message postMessage(@RequestBody Message message) {
        return messageService.createMessage(message);
    }

    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping("/accounts/{id}/messages")
    public List<Message> getMessagesByUser(@PathVariable int id) {
        return messageService.getMessagesByUser(id);
    }

    @GetMapping("/messages/{message_id}")
    public Message getMessageById(@PathVariable int message_id) {
        return messageService.getMessageById(message_id);
    }

    @DeleteMapping("/messages/{message_id}")
    public void deleteMessage(@PathVariable int message_id) {
        messageService.deleteMessage(message_id);
    }

    @PatchMapping("/messages/{message_id}")
    public Message updateMessage(@PathVariable int message_id, @RequestBody Message updatedMsg) {
        return messageService.updateMessage(message_id, updatedMsg);
    }
}
