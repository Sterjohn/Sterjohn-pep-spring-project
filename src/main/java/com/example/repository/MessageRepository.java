package com.example.repository;

import com.example.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Repository for Message entity operations
@Repository
public interface MessageRepository 
        extends JpaRepository<Message, Integer> {

    // === Filtered Queries ===

    // Get all messages posted by a specific account
    List<Message> findByPostedBy(Integer accountId);
}
