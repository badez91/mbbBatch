package com.my.mbbBatch.exception;

import jakarta.persistence.OptimisticLockException;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({OptimisticLockException.class, ObjectOptimisticLockingFailureException.class})
    public ResponseEntity<String> handleOptimisticLocking(Exception ex) {
        return ResponseEntity.status(409).body("Conflict: Record was updated by another transaction.");
    }
}
