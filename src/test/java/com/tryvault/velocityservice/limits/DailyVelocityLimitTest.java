package com.tryvault.velocityservice.limits;

import com.tryvault.velocityservice.model.Transaction;
import com.tryvault.velocityservice.repo.TransactionsRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
@ActiveProfiles("test")
class DailyVelocityLimitTest {
    @MockBean
    TransactionsRepository repository;

    @Autowired
    DailyVelocityLimit limit;

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
        Mockito.when(repository.findByCustomerIdAndTimeBetweenAndAcceptedTrue(
                        anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(t1, t2, t3));
        Transaction transaction = Transaction.builder()
                .id(4L)
                .customerId(1L)
                .loadAmount(BigDecimal.valueOf(1000.00))
                .time(LocalDateTime.now())
                .accepted(true)
                .build();
        List<String> reasons = limit.checkLimits(transaction);
        assertEquals(1, reasons.size());
    }

    @Test
    void whenFourthTransactionAndExceedsDailyLimit_rejected() {
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
                .loadAmount(BigDecimal.valueOf(2000.00))
                .time(LocalDateTime.now())
                .accepted(true)
                .build();
        Mockito.when(repository.findByCustomerIdAndTimeBetweenAndAcceptedTrue(
                        anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(t1, t2, t3));
        Transaction transaction = Transaction.builder()
                .id(4L)
                .customerId(1L)
                .loadAmount(BigDecimal.valueOf(2000.00))
                .time(LocalDateTime.now())
                .accepted(true)
                .build();
        List<String> reasons = limit.checkLimits(transaction);
        assertEquals(2, reasons.size());
    }

    @Test
    void whenThirdTransaction_accepted() {
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
        Mockito.when(repository.findByCustomerIdAndTimeBetweenAndAcceptedTrue(
                        anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(t1, t2));
        Transaction transaction = Transaction.builder()
                .id(4L)
                .customerId(1L)
                .loadAmount(BigDecimal.valueOf(1000.00))
                .time(LocalDateTime.now())
                .accepted(true)
                .build();
        List<String> reasons = limit.checkLimits(transaction);
        assertEquals(0, reasons.size());
    }

    @Test
    void whenLessThanDailyLimit_accepted() {
        Transaction t1 = Transaction.builder()
                .id(1L)
                .customerId(1L)
                .loadAmount(BigDecimal.valueOf(2000.00))
                .time(LocalDateTime.now())
                .accepted(true)
                .build();
        Transaction t2 = Transaction.builder()
                .id(2L)
                .customerId(1L)
                .loadAmount(BigDecimal.valueOf(2000.00))
                .time(LocalDateTime.now())
                .accepted(true)
                .build();
        Mockito.when(repository.findByCustomerIdAndTimeBetweenAndAcceptedTrue(
                        anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(t1, t2));
        Transaction transaction = Transaction.builder()
                .id(4L)
                .customerId(1L)
                .loadAmount(BigDecimal.valueOf(1000.00))
                .time(LocalDateTime.now())
                .accepted(true)
                .build();
        List<String> reasons = limit.checkLimits(transaction);
        assertEquals(0, reasons.size());
    }

    @Test
    void whenExceedsDailyLimit_rejected() {
        Transaction t1 = Transaction.builder()
                .id(1L)
                .customerId(1L)
                .loadAmount(BigDecimal.valueOf(2000.00))
                .time(LocalDateTime.now())
                .accepted(true)
                .build();
        Transaction t2 = Transaction.builder()
                .id(2L)
                .customerId(1L)
                .loadAmount(BigDecimal.valueOf(2000.00))
                .time(LocalDateTime.now())
                .accepted(true)
                .build();
        Mockito.when(repository.findByCustomerIdAndTimeBetweenAndAcceptedTrue(
                        anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(t1, t2));
        Transaction transaction = Transaction.builder()
                .id(4L)
                .customerId(1L)
                .loadAmount(BigDecimal.valueOf(1001.00))
                .time(LocalDateTime.now())
                .accepted(true)
                .build();
        List<String> reasons = limit.checkLimits(transaction);
        assertEquals(1, reasons.size());
    }
}