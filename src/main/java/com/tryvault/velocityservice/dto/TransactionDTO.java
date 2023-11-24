package com.tryvault.velocityservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionDTO {
    Long id;
    @JsonProperty("customer_id")
    Long customerId;
    @JsonProperty("load_amount")
    String loadAmount;
    LocalDateTime time;
}
