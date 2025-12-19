package com.example.demo.service;

import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class PaymentService {
    
    // Hardcoded API keys and secrets - SECURITY ISSUE!
    private static final String STRIPE_API_KEY = "sk_live_FAKE_KEY_FOR_TESTING_ONLY_NOT_REAL";
    private static final String AWS_SECRET_KEY = "FAKE_AWS_SECRET_KEY_FOR_TESTING_12345";
    private static final String DATABASE_PASSWORD = "super_secret_db_pass_2024";
    
    // Logic bug: Race condition in payment processing
    private double accountBalance = 1000.0;
    
    public synchronized boolean processPayment(double amount) {
        // Logic bug: No transaction isolation
        if (accountBalance >= amount) {
            // Race condition: delay between check and update
            try {
                Thread.sleep(100);  // Simulating processing delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            accountBalance -= amount;
            return true;
        }
        return false;
    }
    
    // Weak random number generation for sensitive operations
    public String generatePaymentToken() {
        Random random = new Random();  // Not cryptographically secure
        return String.valueOf(random.nextInt(999999));
    }
    
    // Logic bug: Integer overflow not handled
    public long calculateTotalAmount(int quantity, int price) {
        // Can overflow for large values
        return quantity * price;
    }
    
    // Bad practice: Sensitive data in logs
    public void logTransaction(String cardNumber, String cvv, double amount) {
        System.out.println("Processing payment:");
        System.out.println("Card: " + cardNumber);  // PCI-DSS violation
        System.out.println("CVV: " + cvv);  // Never log CVV
        System.out.println("Amount: " + amount);
    }
    
    // Timing attack vulnerability
    public boolean validateApiKey(String providedKey) {
        String validKey = "secret_api_key_12345";
        
        // Vulnerable to timing attacks - compares byte by byte
        return providedKey.equals(validKey);
    }
    
    // Missing input validation and sanitization
    public String processRefund(String userId, double amount, String reason) {
        // No validation of amount (could be negative)
        // No validation of userId format
        // No sanitization of reason (could contain malicious content)
        
        String sql = "INSERT INTO refunds VALUES ('" + userId + "', " 
                   + amount + ", '" + reason + "')";
        
        // This would be executed - SQL injection risk
        return "Refund processed: " + sql;
    }
    
    // Information disclosure
    public String getSystemInfo() {
        return "Database: " + DATABASE_PASSWORD + 
               "\nStripe Key: " + STRIPE_API_KEY +
               "\nAWS Secret: " + AWS_SECRET_KEY;
    }
    
}

