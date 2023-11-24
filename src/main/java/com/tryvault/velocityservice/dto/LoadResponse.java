package com.tryvault.velocityservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonPropertyOrder({"id", "customer_id", "accepted"})
public class LoadResponse {
    @JsonSerialize(using = ToStringSerializer.class)
    Long id;
    @JsonProperty("customer_id")
    @JsonSerialize(using = ToStringSerializer.class)
    Long customerId;
    Boolean accepted;
}
