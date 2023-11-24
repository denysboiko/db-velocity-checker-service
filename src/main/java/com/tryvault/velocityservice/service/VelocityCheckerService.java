package com.tryvault.velocityservice.service;

import com.tryvault.velocityservice.model.Transaction;

public interface VelocityCheckerService {
    Boolean isAccepted(Transaction transaction);
}
