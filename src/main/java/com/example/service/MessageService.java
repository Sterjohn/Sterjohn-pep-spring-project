package com.example.service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

// Service layer for message-related logic
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public MessageService(
        MessageRepository messageRepository,
        AccountRepository   accountRepository
    ) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    // === Create ===

    public ResponseEntity<Message> createMessage(Message message) {
        // Validate text and postedBy
        if (
            message.getMessageText() == null || message.getMessageText().trim().isEmpty() ||
            message.getMessageText().length() > 255 || message.getPostedBy() == null
        ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Check that the user exists
        String username = getUsernameByAccountId(message.getPostedBy());
        if (username == null || !accountRepository.existsByUsername(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Save the message
        Message savedMessage = messageRepository.save(message);
        return ResponseEntity.ok(savedMessage);
    }

    // === Read ===

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public ResponseEntity<Message> getMessageById(Integer messageId) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        return optionalMessage
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.ok().build());
    }

    public ResponseEntity<List<Message>> getMessagesByAccountId(Integer accountId) {
        List<Message> messages = messageRepository.findByPostedBy(accountId);
        return ResponseEntity.ok(messages);
    }

    // === Update ===

    public ResponseEntity<Object> updateMessageText(
        Integer messageId,
        Message newMessageText
    ) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);

        if (optionalMessage.isEmpty()) {
            return ResponseEntity
                   .status(HttpStatus.BAD_REQUEST)
                   .body("Message not found");
        }

        String text = newMessageText.getMessageText();
        if (text == null || text.trim().isEmpty()) {
            return ResponseEntity
                   .status(HttpStatus.BAD_REQUEST)
                   .body("Message text cannot be empty");
        }

        if (text.length() > 255) {
            return ResponseEntity
                   .status(HttpStatus.BAD_REQUEST)
                   .body("Message too long: it must have a length of at most 255 characters");
        }

        Message message = optionalMessage.get();
        message.setMessageText(text);
        messageRepository.save(message);

        return ResponseEntity.ok().body("1"); // one row updated
    }

    // === Delete ===

    public ResponseEntity<Object> deleteMessageById(Integer messageId) {
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return ResponseEntity.ok().body("1");
        } else {
            return ResponseEntity.ok().build();
        }
    }

    // === Helper ===

    public String getUsernameByAccountId(Integer accountId) {
        Account account = accountRepository.findByAccountId(accountId);
        return account != null ? account.getUsername() : null;
    }
}
