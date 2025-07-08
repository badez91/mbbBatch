package com.my.mbbBatch.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "records", indexes = {
        @Index(name = "idx_account_number", columnList = "accountNumber"),
        @Index(name = "idx_trx_date", columnList = "trxDate"),
        @Index(name = "idx_customer_id", columnList = "customerId")
})

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Records {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String accountNumber;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal trxAmount;

    @Column(length = 255)
    private String description;

    private LocalDate trxDate;

    private LocalTime trxTime;

    @Column(nullable = false)
    private Long customerId;

    @Version
    @Column(name = "version")
    private Long version;

    @Builder.Default
    @Column(nullable = false, updatable = false)
    private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();

    @Builder.Default
    @Column(nullable = false)
    private java.time.LocalDateTime updatedAt = java.time.LocalDateTime.now();
}
