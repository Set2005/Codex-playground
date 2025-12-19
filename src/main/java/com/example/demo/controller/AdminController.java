package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    @Autowired
    private UserService userService;
    
    // BACKDOOR: Hidden admin access without authentication
    @GetMapping("/secret-access")
    public String secretAdminAccess(@RequestParam String code) {
        // Hardcoded backdoor password
        if (code.equals("admin123") || code.equals("master_key_2024")) {
            return "Admin access granted. Welcome master!";
        }
        return "Access denied";
    }
    
    // SQL INJECTION: Direct string concatenation in SQL query
    @GetMapping("/search-users")
    public String searchUsers(@RequestParam String searchTerm) {
        try {
            // Hardcoded database credentials in code
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/mydb",
                "root",
                "password123"  // Security: Hardcoded password
            );
            
            Statement stmt = conn.createStatement();
            // SQL Injection vulnerability
            String query = "SELECT * FROM users WHERE username = '" + searchTerm + "'";
            ResultSet rs = stmt.executeQuery(query);
            
            StringBuilder result = new StringBuilder();
            while (rs.next()) {
                // Exposing sensitive data
                result.append("User: ").append(rs.getString("username"))
                      .append(", Password: ").append(rs.getString("password"))
                      .append(", SSN: ").append(rs.getString("ssn"))
                      .append("\n");
            }
            
            return result.toString();
        } catch (Exception e) {
            // Bad practice: Exposing stack trace to user
            return "Error: " + e.getMessage() + "\n" + e.getStackTrace();
        }
    }
    
    // Path Traversal vulnerability
    @GetMapping("/download-file")
    public String downloadFile(@RequestParam String filename) {
        try {
            // No validation - allows path traversal like ../../../etc/passwd
            java.nio.file.Path filePath = java.nio.file.Paths.get("/uploads/" + filename);
            return new String(java.nio.file.Files.readAllBytes(filePath));
        } catch (Exception e) {
            return "File not found";
        }
    }
    
    // Command Injection vulnerability
    @PostMapping("/execute-command")
    public String executeCommand(@RequestParam String cmd) {
        try {
            // Extremely dangerous: executing user input as system command
            Process process = Runtime.getRuntime().exec(cmd);
            java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.InputStreamReader(process.getInputStream())
            );
            
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            
            return output.toString();
        } catch (Exception e) {
            return "Command execution failed: " + e.getMessage();
        }
    }
    
    // Insecure deserialization
    @PostMapping("/deserialize")
    public Object deserializeObject(@RequestBody byte[] data) {
        try {
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(data);
            java.io.ObjectInputStream ois = new java.io.ObjectInputStream(bis);
            // Dangerous: deserializing untrusted data
            return ois.readObject();
        } catch (Exception e) {
            return null;
        }
    }
    
    // XXE (XML External Entity) vulnerability
    @PostMapping("/parse-xml")
    public String parseXml(@RequestBody String xml) {
        try {
            javax.xml.parsers.DocumentBuilderFactory dbf = 
                javax.xml.parsers.DocumentBuilderFactory.newInstance();
            // XXE vulnerability: external entities not disabled
            javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
            org.w3c.dom.Document doc = db.parse(
                new java.io.ByteArrayInputStream(xml.getBytes())
            );
            return "XML parsed successfully";
        } catch (Exception e) {
            return "Parse error";
        }
    }
    
}

