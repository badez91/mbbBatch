package com.my.mbbBatch.controller;

import com.my.mbbBatch.dto.RecordsUpdateDto;
import com.my.mbbBatch.entity.Records;
import com.my.mbbBatch.service.RecordsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
public class RecordController {

    private final RecordsService recordsService;

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(required = false) String customerId,
            @RequestParam(required = false) String accountNumber,
            @RequestParam(required = false) String description,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            ){

        try {
            Long customerIdLong = (customerId !=null && !customerId.isBlank()) ? Long.parseLong(customerId) : null;
            if (customerIdLong != null && customerIdLong <= 0){
                return ResponseEntity.badRequest().body("Customer ID must be positive number.");
            }
            if (accountNumber != null && accountNumber.length() > 20){
                return ResponseEntity.badRequest().body("Account Number must be less than 20 character");
            }
            if (description != null && description.length() > 255){
                return ResponseEntity.badRequest().body("Description is too long");
            }
            if (page < 0 || size <=0 || size > 100){
                return ResponseEntity.badRequest().body("Invalid pagination size");
            }

            Page<Records> records = recordsService.searchRecords(
                    Optional.ofNullable(customerIdLong),
                    Optional.ofNullable(accountNumber),
                    Optional.ofNullable(description),
                    page,
                    size);

            if (records.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(records);
        } catch (NumberFormatException e){
            return ResponseEntity.badRequest().body("Invalid format in request parameter");
        }
    }

    @PutMapping("/updateRecord")
    public ResponseEntity<?> updateRecord(@Valid @RequestBody RecordsUpdateDto dto){
        Records updatedRecords = recordsService.updateRecords(dto);
        return ResponseEntity.ok(updatedRecords);
    }
}
