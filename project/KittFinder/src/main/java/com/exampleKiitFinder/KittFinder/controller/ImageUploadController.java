package com.exampleKiitFinder.KittFinder.controller;

import com.exampleKiitFinder.KittFinder.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = "https://kiitfinderui-abbi.vercel.app")
public class ImageUploadController {
    @Autowired
    private CloudinaryService cloudinaryService;
    @PostMapping("/image")
    public ResponseEntity<Map<String,String>> uplaodImage(
            @RequestParam("file")MultipartFile file
            ){
        try {
            if (file.isEmpty()){
                return ResponseEntity.badRequest().body(
                    Map.of("error","Please select a file to upload")
                );

            }
            String contentType = file.getContentType();
            if (contentType==null||!contentType.startsWith("image/")){
                return ResponseEntity.badRequest().body(
                        Map.of("error","Only image files are allowed")
                );
            }
            if (file.getSize() > 5 * 1024 * 1024) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "File size should not exceed 5MB")
                );
            }
            String imageUrl = cloudinaryService.uploadImage(file);
            Map<String,String> response = new HashMap<>();
            response.put("message", "Image uploaded successfully");
            response.put("imageUrl", imageUrl);

            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(
                    Map.of("error", "Failed to upload image: " + e.getMessage())
            );
        }
    }
}
