package com.tryvault.velocityservice;

import com.tryvault.velocityservice.service.TransactionProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@ConditionalOnProperty("file.output")
public class VelocityServiceCommandLineRunner implements CommandLineRunner {
    @Autowired
    private TransactionProcessingService transactionProcessingService;

    @Value("${file.path}")
    private String filePath;

    @Override
    public void run(String... args) throws Exception {
        Path outputPath = Paths.get(filePath);
        Files.write(outputPath, transactionProcessingService.processTransactions());
    }
}
