package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

// Service layer for account logic
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // === Register ===

    public ResponseEntity<Account> registerAccount(Account account) {
        // Validate username and password
        if (
            account.getUsername() == null || account.getUsername().isBlank() ||
            account.getPassword() == null || account.getPassword().length() < 4
        ) {
            return ResponseEntity.status(400).body(null);
        }

        // Check for duplicate username
        if (doesUsernameExist(account.getUsername())) {
            return ResponseEntity.status(409).body(null);
        }

        // Save and return account
        Account savedAccount = accountRepository.save(account);
        return ResponseEntity.status(HttpStatus.OK).body(savedAccount);
    }

    // === Login ===

    public ResponseEntity<Account> login(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();

        // Validate input
        if (
            username == null || username.isBlank() ||
            password == null || password.isBlank()
        ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Authenticate user
        Account authenticatedAccount = accountRepository.findByUsernameAndPassword(username, password);
        if (authenticatedAccount == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.ok(authenticatedAccount);
    }

    // === Helper ===

    public boolean doesUsernameExist(String username) {
        return accountRepository.existsByUsername(username);
    }
}
