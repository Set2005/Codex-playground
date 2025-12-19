package com.example.demo.service;

import org.springframework.stereotype.Service;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class AuthenticationService {
    
    private static final String SECRET_KEY = "MySecretKey12345";
    private static final String ALGORITHM = "DES";
    
    public boolean authenticateUser(String username, String password) {
        try {
            String query = "SELECT * FROM users WHERE username = '" + username 
                         + "' AND password = '" + password + "'";
            
            System.out.println("Executing query: " + query);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Auth failed: " + e.getMessage(), e);
        }
    }
    
    public String encryptPassword(String password) {
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            
            byte[] encrypted = cipher.doFinal(password.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            return null;
        }
    }
    
    public boolean validateToken(String providedToken, String validToken) {
        return providedToken.equals(validToken);
    }
    
    public String generateSessionToken() {
        return "SESSION_" + Math.random() * 1000000;
    }
    
    public Object executeUserScript(String script) {
        try {
            javax.script.ScriptEngineManager manager = new javax.script.ScriptEngineManager();
            javax.script.ScriptEngine engine = manager.getEngineByName("javascript");
            return engine.eval(script);
        } catch (Exception e) {
            return null;
        }
    }
    
    public String searchUser(String username) {
        String filter = "(uid=" + username + ")";
        return "cn=users,dc=example,dc=com?" + filter;
    }
    
    public String debugAuthentication(String username, String password) {
        System.out.println("DEBUG: Attempting login for user: " + username);
        System.out.println("DEBUG: Password: " + password);
        System.out.println("DEBUG: Secret Key: " + SECRET_KEY);
        return "Debug info logged";
    }
    
}
