package com.tryvault.velocityservice.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class TransactionProcessingServiceTest {
    @Value("classpath:expected.jsonl")
    Resource expectedOutputFile;

    @Autowired
    TransactionProcessingService transactionProcessingService;

    @Test
    void processTransactions_success() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(expectedOutputFile.getInputStream()));
        List<String> expected = br.lines().toList();
        List<String> actual = transactionProcessingService.processTransactions();
        assertEquals(expected, actual);
    }
}