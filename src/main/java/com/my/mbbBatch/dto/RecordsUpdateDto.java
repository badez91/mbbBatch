package com.my.mbbBatch.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RecordsUpdateDto {

    @NotNull(message = "ID is required")
    private Long id;

    private String description;

    private BigDecimal trxAmount;

    @NotNull(message = "version is required")
    private Long version;
}
