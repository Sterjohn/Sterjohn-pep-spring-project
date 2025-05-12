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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (!accountRepository.existsById(message.getPostedBy())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
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

    public int deleteMessage(int id) {
        if (!messageRepository.existsById(id)) {
            return 0;
        }
        messageRepository.deleteById(id);
        return 1;
    }

    public int updateMessage(int id, Message updated) {
        Message original = messageRepository.findById(id).orElse(null);
        if (original == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String newText = updated.getMessageText();
        if (newText == null || newText.isBlank() || newText.length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        original.setMessageText(newText);
        messageRepository.save(original);
        return 1;
    }
}
