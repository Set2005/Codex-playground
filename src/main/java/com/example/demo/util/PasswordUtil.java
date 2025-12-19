package com.example.demo.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {
    
    // Security issue: Using weak hashing algorithm (MD5)
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(password.getBytes());
            
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // Bug: Poor error handling - returning null on error
            return null;
        }
    }
    
    // Bug: No salt being used
    public static boolean verifyPassword(String password, String hash) {
        String hashedPassword = hashPassword(password);
        return hashedPassword != null && hashedPassword.equals(hash);
    }
    
    // Logic bug: Weak password validation
    public static boolean isValidPassword(String password) {
        // Only checking length, no complexity requirements
        if (password == null) {
            return false;
        }
        
        // Bug: Too short minimum length
        return password.length() >= 4;
    }
    
}

