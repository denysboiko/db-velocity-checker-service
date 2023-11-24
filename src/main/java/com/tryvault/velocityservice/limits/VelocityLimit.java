package com.tryvault.velocityservice.limits;

import com.tryvault.velocityservice.model.Transaction;
import com.tryvault.velocityservice.repo.TransactionsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjuster;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class VelocityLimit {
    protected String period;

    protected TemporalAdjuster startingDate;
    protected double limit;

    private final TransactionsRepository transactionsRepository;

    protected List<Transaction> loadPreviousTransactions(Transaction transaction) {
        LocalDateTime transactionTime = transaction.getTime();
        LocalDateTime startingDateTime = transactionTime.with(startingDate);
        return transactionsRepository.findByCustomerIdAndTimeBetweenAndAcceptedTrue(
                transaction.getCustomerId(), startingDateTime, transactionTime);

    }
    public List<String> checkLimits(Transaction transaction) {
        List<String> rejectionReasons = new LinkedList<>();
        List<Transaction> previousTransactions = loadPreviousTransactions(transaction);
        if (exceedsLimit(previousTransactions, transaction)) {
            String reason = String.format("%s velocity limit exceeded: %s", period, limit);
            log.warn("{}. Transaction id: {}", reason, transaction.getId());
            rejectionReasons.add(reason);
        }
        return rejectionReasons;
    }

    protected boolean exceedsLimit(List<Transaction> previousTransactions, Transaction transaction) {
        BigDecimal previousSum = previousTransactions.stream()
                .map(Transaction::getLoadAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal currentSum = previousSum.add(transaction.getLoadAmount());
        BigDecimal limitValue = BigDecimal.valueOf(limit);
        return currentSum.compareTo(limitValue) > 0;
    }

}
