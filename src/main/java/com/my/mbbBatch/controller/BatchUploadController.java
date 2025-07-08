package com.my.mbbBatch.controller;

import com.my.mbbBatch.service.RecordsService;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/batch")
public class BatchUploadController {


    private final RecordsService recordsService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadBatchFile(@RequestParam("file")MultipartFile file) {

        try {
            recordsService.processUploadFile(file);
            return ResponseEntity.ok("File Uploaded");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to process file " + e.getMessage());
        }


    }

}