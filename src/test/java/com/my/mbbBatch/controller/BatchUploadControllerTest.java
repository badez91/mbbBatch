package com.my.mbbBatch.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BatchUploadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // To test batch file upload
    @Test
    void testUploadBatchFile() throws Exception {
        // Mock data
//        String mockContent = "ACCOUNT_NUMBER|TRX_AMOUNT|DESCRIPTION|TRX_DATE|TRX_TIME|CUSTOMER_ID\n" +
//                "12345678|100.00|DESC|2023-10-01|10:00:00|999\n";
        Path path = Paths.get("src/test/resources/test-files/record.txt");
        byte[] filecontent = Files.readAllBytes(path);

        MockMultipartFile file = new MockMultipartFile(
                "file",                 // name of the request param
                "records.txt",          // filename
                MediaType.TEXT_PLAIN_VALUE,
//                mockContent.getBytes()
                filecontent
        );

        mockMvc.perform(multipart("/api/batch/upload")
                        .file(file))
                .andExpect(status().isOk());

        System.out.println("success");
    }
}
