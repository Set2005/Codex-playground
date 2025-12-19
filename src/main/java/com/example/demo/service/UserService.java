package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public User createUser(User user) {
        // Bug: No validation for duplicate username
        // Bug: Storing password in plain text
        return userRepository.save(user);
    }
    
    public User getUserById(Long id) {
        // Bug: Potential NullPointerException - using get() instead of orElse()
        return userRepository.findById(id).get();
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id).get();  // Bug: No null check
        
        user.setUsername(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());  // Security: Plain text password
        user.setEmail(userDetails.getEmail());
        user.setRole(userDetails.getRole());
        user.setActive(userDetails.isActive());
        
        return userRepository.save(user);
    }
    
    public void deleteUser(Long id) {
        // Bug: No check if user exists before deletion
        userRepository.deleteById(id);
    }
    
    public User authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        
        // Security issue: Plain text password comparison
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        
        return null;
    }
    
    // Performance issue: N+1 query problem
    public List<User> getUsersByRole(String role) {
        List<User> users = userRepository.findByRole(role);
        
        // Simulating additional queries for each user
        for (User user : users) {
            // This would trigger additional queries in a real scenario
            System.out.println("Processing user: " + user.getUsername());
        }
        
        return users;
    }
    
}

