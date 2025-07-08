package com.my.mbbBatch.commonUtils;

import com.my.mbbBatch.entity.Records;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class FileParser {

    public static List<Records> parse(MultipartFile file) throws IOException {

        List<Records> list = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            Boolean isFirstLine = true;
            int row = 0;

            while ((line = bufferedReader.readLine()) != null) {
                row++;
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] parts = line.split("\\|");
                if (parts.length < 6) {
                    System.out.println("Row : " + row + " missing column." );
                    continue;
                }

                String accountNumber = parts[0].trim();
                String trxAmountStr = parts[1].trim();
                String description = parts[2].trim();
                String trxDateStr = parts[3].trim();
                String trxTimeStr = parts[4].trim();
                String customerIdStr = parts[5].trim();

                //Validation
                if (!accountNumber.matches("\\d{0,20}")){
                    System.out.println("Row : " + row + " invalid account number - " + accountNumber);
                    continue;
                }

                BigDecimal trxAmount;
                try {
                    trxAmount = new BigDecimal(trxAmountStr);
                    if (trxAmount.compareTo(BigDecimal.ZERO) <= 0){
                        System.out.println("Row : " + row + " invalid Trx Amount must be greater than 0 - " + trxAmountStr);
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Row : " + row + " invalid Trx Amount is not numeric - " + trxAmountStr);
                    continue;
                }

                if (description.length() > 255){
                    System.out.println("Row : " + row + " invalid Description is too long.");
                    continue;
                }

                LocalDate trxDate;
                try {
                    trxDate = LocalDate.parse(trxDateStr);
                    if (trxDate.isAfter(LocalDate.now())){
                        System.out.println("Row : " + row + "invalid Trx Date is in future - " + trxDateStr);
                        continue;
                    }
                } catch (DateTimeParseException e){
                    System.out.println("Row : " + row + " invalid Trx Date is not Date - " + trxDateStr);
                    continue;
                }

                LocalTime trxTime;
                try {
                    trxTime = LocalTime.parse(trxTimeStr);
                } catch (DateTimeParseException e){
                    System.out.println("Row : " + row + " invalid Trx Time is not Time - " + trxTimeStr);
                    continue;
                }

                Long customerId;
                try {
                    customerId = Long.parseLong(customerIdStr);
                    if (customerId < 0) {
                        System.out.println("Row : " + row + " invalid Customer Id must be positive - " + customerIdStr);
                        continue;
                    }
                } catch (NumberFormatException e){
                    System.out.println("Row : " + row + " invalid Customer Id must be numeric");
                    continue;
                }


                Records records = Records.builder()
                        .accountNumber(accountNumber)
                        .trxAmount(trxAmount)
                        .description(description)
                        .trxDate(trxDate)
                        .trxTime(trxTime)
                        .customerId(customerId)
                        .build();

                list.add(records);
            }
        }


        return list;
    }
}
