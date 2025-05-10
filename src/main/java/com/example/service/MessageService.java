package com.example.service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    AccountRepository accountRepository;

    public Message createMessage(Message message) {
        if (message.getMessageText() == null || message.getMessageText().isBlank() || message.getMessageText().length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid message text");
        }
        if (!accountRepository.existsById(message.getPostedBy())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist");
        }
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public List<Message> getMessagesByUser(int userId) {
        return messageRepository.findByPostedBy(userId);
    }

    public Message getMessageById(int id) {
        return messageRepository.findById(id).orElse(null);
    }

    public void deleteMessage(int id) {
        if (!messageRepository.existsById(id)) {
            return; // still return 200 OK per test
        }
        messageRepository.deleteById(id);
    }

    public Message updateMessage(int id, Message updated) {
        Message original = messageRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message not found"));

        String newText = updated.getMessageText();
        if (newText == null || newText.isBlank() || newText.length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid message text");
        }

        original.setMessageText(newText);
        return messageRepository.save(original);
    }
}
