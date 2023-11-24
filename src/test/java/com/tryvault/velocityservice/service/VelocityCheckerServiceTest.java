package com.tryvault.velocityservice.service;

import com.tryvault.velocityservice.model.Transaction;
import com.tryvault.velocityservice.repo.TransactionsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@ActiveProfiles("test")
class VelocityCheckerServiceTest {

    @Autowired
    TransactionsRepository repository;

    @Autowired
    VelocityCheckerService service;

    @Test
    void whenFourthTransaction_rejected() {
        Transaction t1 = Transaction.builder()
                .id(1L)
                .customerId(1L)
                .loadAmount(BigDecimal.valueOf(1000.00))
                .time(LocalDateTime.now())
                .accepted(true)
                .build();
        Transaction t2 = Transaction.builder()
                .id(2L)
                .customerId(1L)
                .loadAmount(BigDecimal.valueOf(1000.00))
                .time(LocalDateTime.now())
                .accepted(true)
                .build();
        Transaction t3 = Transaction.builder()
                .id(3L)
                .customerId(1L)
                .loadAmount(BigDecimal.valueOf(1000.00))
                .time(LocalDateTime.now())
                .accepted(true)
                .build();
        repository.saveTransaction(t1);
        repository.saveTransaction(t2);
        repository.saveTransaction(t3);
        Transaction transaction = Transaction.builder()
                .id(4L)
                .customerId(1L)
                .loadAmount(BigDecimal.valueOf(1000.00))
                .time(LocalDateTime.now())
                .accepted(true)
                .build();
        boolean accepted = service.isAccepted(transaction);
        assertFalse(accepted);
    }

    @Test
    void whenThirdTransaction_accepted() {
        Transaction t1 = Transaction.builder()
                .id(1L)
                .customerId(2L)
                .loadAmount(BigDecimal.valueOf(1000.00))
                .time(LocalDateTime.now())
                .accepted(true)
                .build();
        Transaction t2 = Transaction.builder()
                .id(2L)
                .customerId(2L)
                .loadAmount(BigDecimal.valueOf(1000.00))
                .time(LocalDateTime.now())
                .accepted(true)
                .build();
        repository.saveTransaction(t1);
        repository.saveTransaction(t2);
        Transaction transaction = Transaction.builder()
                .id(4L)
                .customerId(2L)
                .loadAmount(BigDecimal.valueOf(1000.00))
                .time(LocalDateTime.now())
                .build();
        boolean accepted = service.isAccepted(transaction);
        assertTrue(accepted);
    }

    @Test
    void whenLessThanDailyLimit_accepted() {
        Transaction t1 = Transaction.builder()
                .id(1L)
                .customerId(3L)
                .loadAmount(BigDecimal.valueOf(2000.00))
                .time(LocalDateTime.now())
                .accepted(true)
                .build();
        Transaction t2 = Transaction.builder()
                .id(2L)
                .customerId(3L)
                .loadAmount(BigDecimal.valueOf(2000.00))
                .time(LocalDateTime.now())
                .accepted(true)
                .build();
        repository.saveTransaction(t1);
        repository.saveTransaction(t2);
        Transaction transaction = Transaction.builder()
                .id(4L)
                .customerId(3L)
                .loadAmount(BigDecimal.valueOf(1000.00))
                .time(LocalDateTime.now())
                .build();
        boolean accepted = service.isAccepted(transaction);
        assertTrue(accepted);
    }

    @Test
    void whenExceedsDailyLimit_rejected() {
        Transaction t1 = Transaction.builder()
                .id(1L)
                .customerId(4L)
                .loadAmount(BigDecimal.valueOf(2000.00))
                .time(LocalDateTime.now())
                .accepted(true)
                .build();
        Transaction t2 = Transaction.builder()
                .id(2L)
                .customerId(4L)
                .loadAmount(BigDecimal.valueOf(2000.00))
                .time(LocalDateTime.now())
                .accepted(true)
                .build();
        repository.saveTransaction(t1);
        repository.saveTransaction(t2);
        Transaction transaction = Transaction.builder()
                .id(4L)
                .customerId(4L)
                .loadAmount(BigDecimal.valueOf(1001.00))
                .time(LocalDateTime.now())
                .build();
        boolean accepted = service.isAccepted(transaction);
        assertFalse(accepted);
    }

    @Test
    void whenLessThanWeeklyLimit_rejected() {
        for (Transaction transaction : getWeekOfTransactions(5L, 3000.00)) {
            repository.saveTransaction(transaction);
        }
        Transaction transaction = Transaction.builder()
                .id(7L)
                .customerId(5L)
                .loadAmount(BigDecimal.valueOf(2000.00))
                .time(LocalDateTime.of(2023, 5, 7, 0, 1))
                .build();
        boolean accepted = service.isAccepted(transaction);
        assertTrue(accepted);
    }

    @Test
    void whenExceedsWeeklyLimit_rejected() {
        for (Transaction transaction : getWeekOfTransactions(6L, 1000.00)) {
            repository.saveTransaction(transaction);
        }
        Transaction transaction = Transaction.builder()
                .id(7L)
                .customerId(6L)
                .loadAmount(BigDecimal.valueOf(2000.00))
                .time(LocalDateTime.of(2023, 5, 7, 0, 1))
                .build();
        boolean accepted = service.isAccepted(transaction);
        assertTrue(accepted);
    }
    private List<Transaction> getWeekOfTransactions(Long customerId, double amount) {
        List<Transaction> transactions = new LinkedList<>();
        for (int i = 1; i < 7; i++) {
            Transaction t = Transaction.builder()
                    .id((long) i)
                    .customerId(customerId)
                    .loadAmount(BigDecimal.valueOf(amount))
                    .time(LocalDateTime.of(2023, 5, i, 0, 1))
                    .accepted(true)
                    .build();
            transactions.add(t);
        }
        return transactions;
    }
}