package com.example.repository;

import com.example.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository for Account entity operations
@Repository
public interface AccountRepository 
        extends JpaRepository<Account, Long> {

    // === Exists Check ===

    // Returns true if username is taken
    boolean existsByUsername(String username);

    // === Find by Single Field ===

    // Find account by username
    Account findByUsername(String username);

    // Find account by ID
    Account findByAccountId(Integer accountId);

    // === Find by Composite Fields ===

    // Find account with matching username + password
    Account findByUsernameAndPassword(
        String username,
        String password
    );
}
