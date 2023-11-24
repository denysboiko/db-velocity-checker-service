package com.tryvault.velocityservice.limits;

import com.tryvault.velocityservice.model.Transaction;
import com.tryvault.velocityservice.repo.TransactionsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;


@Service
@Slf4j
public class DailyVelocityLimit extends VelocityLimit {
    private static final int DAILY_TRANSACTIONS_LIMIT = 3;
    public DailyVelocityLimit(TransactionsRepository transactionsRepository) {
        super(transactionsRepository);
        this.period = "Daily";
        this.limit = 5000.00;
        this.startingDate = LocalTime.MIN;
    }

    @Override
    public List<String> checkLimits(Transaction transaction) {
        List<String> rejectionReasons = new LinkedList<>();
        List<Transaction> previousTransactions = loadPreviousTransactions(transaction);
        if (exceedsLimit(previousTransactions, transaction)) {
            String reason = String.format("%s velocity limit exceeded: %s", period, limit);
            rejectionReasons.add(reason);
            log.warn("{}. Transaction id: {}", reason, transaction.getId());
        }
        if (previousTransactions.size() + 1 > DAILY_TRANSACTIONS_LIMIT) {
            String message = String.format("%s transaction limit exceeded: %s", period, previousTransactions.size() + 1);
            rejectionReasons.add(message);
            log.warn("{}. Transaction id: {}", message, transaction.getId());
        }
        return rejectionReasons;

    }
}
