package com.tryvault.velocityservice.service;

import java.util.List;

public interface TransactionProcessingService {
    List<String> processTransactions() throws Exception;
}
