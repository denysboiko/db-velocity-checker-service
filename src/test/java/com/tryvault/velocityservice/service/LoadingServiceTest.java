package com.tryvault.velocityservice.service;

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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class LoadingServiceTest {

    @MockBean
    TransactionsRepository repository;

    @Autowired
    LoadingService service;

    @Test
    void wenTransactionIsRepeated_success() {
        Transaction transaction = Transaction.builder()
                .id(1L)
                .customerId(1L)
                .loadAmount(BigDecimal.TEN)
                .time(LocalDateTime.now())
                .build();
        Mockito.when(repository.findFirstByIdAndCustomerId(1L, 1L)).thenReturn(Optional.of(transaction));
        boolean actual = service.isRepeat(transaction);
        assertTrue(actual);
    }

    @Test
    void wenTransactionIsUnique_success() {
        Transaction transaction = Transaction.builder()
                .id(1L)
                .customerId(1L)
                .loadAmount(BigDecimal.TEN)
                .time(LocalDateTime.now())
                .build();
        boolean actual = service.isRepeat(transaction);
        assertFalse(actual);
    }

}