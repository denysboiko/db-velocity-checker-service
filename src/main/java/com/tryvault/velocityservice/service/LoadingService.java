package com.tryvault.velocityservice.service;

import com.tryvault.velocityservice.dto.LoadResponse;
import com.tryvault.velocityservice.model.Transaction;

public interface LoadingService {
    boolean isRepeat(Transaction transaction);

    LoadResponse loadFunds(Transaction transaction);
}
