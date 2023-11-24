package com.tryvault.velocityservice.service;

import com.tryvault.velocityservice.dto.LoadResponse;
import com.tryvault.velocityservice.model.Transaction;
import com.tryvault.velocityservice.repo.TransactionsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoadingServiceImpl implements LoadingService {
    private final TransactionsRepository transactionsRepository;
    private final VelocityCheckerService velocityCheckerService;

    @Override
    public boolean isRepeat(Transaction transaction) {
        Optional<Transaction> existingTransaction =
                transactionsRepository.findFirstByIdAndCustomerId(
                        transaction.getId(), transaction.getCustomerId());
        return existingTransaction.isPresent();
    }
    @Override
    public LoadResponse loadFunds(Transaction transaction) {
        if (isRepeat(transaction)) {
            log.info("Transaction id: {} is repeated for customer: {}. Datetime: {}",
                    transaction.getId(), transaction.getCustomerId(), transaction.getTime());
            return null;
        }
        boolean accepted = velocityCheckerService.isAccepted(transaction);
        transaction.setAccepted(accepted);
        transactionsRepository.saveTransaction(transaction);
        return LoadResponse.builder()
                .id(transaction.getId())
                .customerId(transaction.getCustomerId())
                .accepted(accepted)
                .build();
    }
}
