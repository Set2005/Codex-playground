package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;

@RestController
@RequestMapping("/api/files")
public class FileController {
    
    @GetMapping("/read")
    public ResponseEntity<String> readFile(@RequestParam String filename) {
        try {
            Path filePath = Paths.get("/uploads/" + filename);
            String content = Files.readString(filePath);
            return ResponseEntity.ok(content);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.toString());
        }
    }
    
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            Path uploadPath = Paths.get("/uploads/" + filename);
            file.transferTo(uploadPath.toFile());
            return ResponseEntity.ok("File uploaded: " + filename);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Upload failed");
        }
    }
    
    @PostMapping("/process")
    public ResponseEntity<String> processFile(@RequestParam String filename) {
        try {
            String command = "convert /uploads/" + filename + " /processed/" + filename;
            Process process = Runtime.getRuntime().exec(command);
            
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
            );
            
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            
            return ResponseEntity.ok(output.toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Processing failed");
        }
    }
    
    @PostMapping("/extract")
    public ResponseEntity<String> extractZip(@RequestParam String zipFile) {
        try {
            java.util.zip.ZipFile zip = new java.util.zip.ZipFile("/uploads/" + zipFile);
            java.util.Enumeration<? extends java.util.zip.ZipEntry> entries = zip.entries();
            
            while (entries.hasMoreElements()) {
                java.util.zip.ZipEntry entry = entries.nextElement();
                File file = new File("/extracted/" + entry.getName());
                
                if (entry.isDirectory()) {
                    file.mkdirs();
                } else {
                    InputStream in = zip.getInputStream(entry);
                    FileOutputStream out = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                    }
                    out.close();
                    in.close();
                }
            }
            
            return ResponseEntity.ok("Extracted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Extraction failed");
        }
    }
    
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam String filename) {
        File file = new File("/uploads/" + filename);
        
        if (file.delete()) {
            return ResponseEntity.ok("File deleted: " + filename);
        } else {
            return ResponseEntity.badRequest().body("Deletion failed");
        }
    }
    
    @GetMapping("/list")
    public ResponseEntity<String[]> listFiles(@RequestParam String directory) {
        File dir = new File(directory);
        
        if (dir.exists() && dir.isDirectory()) {
            String[] files = dir.list();
            return ResponseEntity.ok(files);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
    
}
