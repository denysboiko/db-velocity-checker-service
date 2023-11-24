package com.tryvault.velocityservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tryvault.velocityservice.dto.LoadResponse;
import com.tryvault.velocityservice.dto.TransactionDTO;
import com.tryvault.velocityservice.exception.IncorrectTransactionException;
import com.tryvault.velocityservice.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
public class TransactionProcessingServiceImpl implements TransactionProcessingService {
    @Value("classpath:input.jsonl")
    private Resource inputFile;

    @Autowired
    private ObjectMapper om;
    @Autowired
    private LoadingService loadingService;

    @Override
    public List<String> processTransactions() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputFile.getInputStream()));
        List<TransactionDTO> transactions = br.lines()
                .map(obj -> deserialize(obj, TransactionDTO.class))
                .toList();
        List<String> responses = new LinkedList<>();
        for (TransactionDTO transactionDto : transactions) {
            Transaction transaction = Transaction.of(transactionDto);
            LoadResponse loadResponse = loadingService.loadFunds(transaction);
            if (Objects.nonNull(loadResponse)) {
                responses.add(om.writeValueAsString(loadResponse));
            }
        }
        return responses;
    }

    private <T> T deserialize(String json, Class<T> type) {
        try {
            return om.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new IncorrectTransactionException(String.format("Cannot parse transaction: %s", json));
        }
    }
}
