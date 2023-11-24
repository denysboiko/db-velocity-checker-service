package com.tryvault.velocityservice.service;

import com.tryvault.velocityservice.limits.VelocityLimit;
import com.tryvault.velocityservice.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class VelocityCheckerServiceImpl implements VelocityCheckerService {
    private final Map<String, VelocityLimit> limits;
    @Override
    public Boolean isAccepted(Transaction transaction) {
        List<String> rejectionReasons = limits.values()
                .stream()
                .flatMap(limit -> limit.checkLimits(transaction).stream())
                .toList();
        if (!rejectionReasons.isEmpty()) {
            log.warn("Total {} rejection reasons were found during processing", rejectionReasons.size());
        }
        return rejectionReasons.isEmpty();
    }
}
