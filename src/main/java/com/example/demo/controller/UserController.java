package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // Bug: No input validation
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        // Bug: No exception handling - will return 500 on non-existent ID
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        // Security issue: Exposing all user data including passwords
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestBody User userDetails) {
        // Bug: No validation
        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        // Bug: No error handling
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam String username,
            @RequestParam String password) {
        // Security: No rate limiting, plain text password
        User user = userService.authenticate(username, password);
        
        if (user != null) {
            // Security issue: Returning full user object with password
            return ResponseEntity.ok(user);
        }
        
        // Bug: Leaking information about username existence
        return ResponseEntity.status(401).body("Invalid username or password");
    }
    
    // SQL Injection vulnerability through path variable
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchByEmail(@RequestParam String email) {
        // This uses the vulnerable query in UserRepository
        List<User> users = userService.userRepository.findByEmail(email);
        return ResponseEntity.ok(users);
    }
    
}

