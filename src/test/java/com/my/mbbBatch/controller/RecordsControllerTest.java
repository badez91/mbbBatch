package com.my.mbbBatch.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.mbbBatch.dto.RecordsUpdateDto;
import com.my.mbbBatch.entity.Records;
import com.my.mbbBatch.repository.RecordsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class RecordsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecordsRepository recordsRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Records existing;

    // To test search function and pagination
    @Test
    void testSearchByCustomerId() throws Exception{
        mockMvc.perform(get("/api/records/search")
                        .param("customerId", "123")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    // Initialize test data
    @BeforeEach
    void setup() {

        //
        existing = Records.builder()
                .accountNumber("8872838283")
                .trxAmount(BigDecimal.valueOf(100.00))
                .description("Initial desc")
                .trxDate(LocalDate.from(LocalDateTime.now()))
                .customerId(222L)
                .build();

        existing = recordsRepository.save(existing);
    }

    // To test update all record
    @Test
    void testUpdateRecordSuccess() throws Exception{

        RecordsUpdateDto dto = new RecordsUpdateDto();
        dto.setId(existing.getId());
        dto.setDescription("Updated description");
        dto.setTrxAmount(BigDecimal.valueOf(150.00));
        dto.setVersion(existing.getVersion());

        mockMvc.perform(put("/api/records/updateRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated description"))
                .andExpect(jsonPath("$.trxAmount").value(150.00));
    }

    // To test concurrent update
    @Test
    void testUpdateConflict() throws Exception {

        // Perform an update to increase the version
        existing.setDescription("Interfering update");
        existing = recordsRepository.save(existing); // version becomes 1

        RecordsUpdateDto dto = new RecordsUpdateDto();
        dto.setId(existing.getId());
        dto.setDescription("Conflict update");
        dto.setTrxAmount(BigDecimal.valueOf(999));
        dto.setVersion(0L); // wrong version

        mockMvc.perform(put("/api/records/updateRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    // To test partially update record
    @Test
    void testUpdatePartialRecordSuccess() throws Exception{

        // Set value only to description
        RecordsUpdateDto dto = new RecordsUpdateDto();
        dto.setId(existing.getId());
        dto.setDescription("Updated description");
        dto.setVersion(existing.getVersion());

        mockMvc.perform(put("/api/records/updateRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated description"));
    }
}
