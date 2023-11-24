package com.tryvault.velocityservice.repo;

import com.tryvault.velocityservice.model.Transaction;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionsRepository extends CrudRepository<Transaction, Long> {
    Optional<Transaction> findFirstByIdAndCustomerId(Long id, Long customerId);

    List<Transaction> findByCustomerIdAndTimeBetweenAndAcceptedTrue(Long customerId, LocalDateTime start, LocalDateTime end);

    @Modifying
    @Query("""
         INSERT INTO Transactions VALUES (
            :#{#transaction.id},
            :#{#transaction.customerId},
            :#{#transaction.loadAmount},
            :#{#transaction.time},
            :#{#transaction.accepted})
    """)
    void saveTransaction(Transaction transaction);
}
