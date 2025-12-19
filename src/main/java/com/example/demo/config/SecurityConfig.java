package com.example.demo.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {
    
    // Security: CORS wide open to all origins
    public void configureCors() {
        // Allows any origin to access the API
        // Access-Control-Allow-Origin: *
    }
    
    // Security: Disabled CSRF protection
    public void disableCSRF() {
        // CSRF protection is disabled
        // This makes the application vulnerable to CSRF attacks
    }
    
    // Security: No rate limiting
    public void noRateLimiting() {
        // No rate limiting configured
        // Vulnerable to brute force and DoS attacks
    }
    
    // Bad practice: Debug mode enabled in production
    public static final boolean DEBUG_MODE = true;
    public static final boolean ENABLE_DETAILED_ERRORS = true;
    
    // Hardcoded encryption key
    public static final String ENCRYPTION_KEY = "ThisIsAVeryBadEncryptionKey123!";
    
    // Weak session configuration
    public int getSessionTimeoutMinutes() {
        return 43200;  // 30 days - way too long
    }
    
    // Security: No password complexity requirements
    public boolean isValidPassword(String password) {
        return password != null && password.length() >= 3;
    }
    
    // Missing security headers
    public void configureSecurityHeaders() {
        // Missing:
        // - X-Frame-Options (clickjacking protection)
        // - X-Content-Type-Options (MIME sniffing protection)
        // - Content-Security-Policy
        // - Strict-Transport-Security (HSTS)
    }
    
}

