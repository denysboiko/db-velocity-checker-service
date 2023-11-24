package com.tryvault.velocityservice.model;

import com.tryvault.velocityservice.dto.TransactionDTO;
import com.tryvault.velocityservice.exception.IncorrectAmountException;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDateTime;

@Data
@Builder
@Table("TRANSACTIONS")
public class Transaction {
    @Id
    Long id;
    Long customerId;
    BigDecimal loadAmount;
    LocalDateTime time;
    Boolean accepted;

    public static Transaction of(TransactionDTO request) {
        DecimalFormat df = new DecimalFormat("$#.##");
        df.setParseBigDecimal(true);
        BigDecimal amount;
        try {
            amount = (BigDecimal) df.parse(request.getLoadAmount());
        } catch (ParseException e) {
            throw new IncorrectAmountException(String.format("Cannot parse transaction amount: %s", request.getLoadAmount()));
        }
        return Transaction.builder()
                .id(request.getId())
                .customerId(request.getCustomerId())
                .loadAmount(amount)
                .time(request.getTime())
                .build();
    }
}
