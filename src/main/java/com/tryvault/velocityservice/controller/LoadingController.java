package com.tryvault.velocityservice.controller;

import com.tryvault.velocityservice.dto.LoadResponse;
import com.tryvault.velocityservice.dto.TransactionDTO;
import com.tryvault.velocityservice.model.Transaction;
import com.tryvault.velocityservice.service.LoadingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoadingController {
    private final LoadingService loadingService;
    @PostMapping("/load")
    public ResponseEntity<LoadResponse> loadFunds(@RequestBody TransactionDTO request) {
        Transaction transaction = Transaction.of(request);
        LoadResponse loadResponse = loadingService.loadFunds(transaction);
        return ResponseEntity.ok(loadResponse);
    }
}
