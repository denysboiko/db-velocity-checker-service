package com.tryvault.velocityservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tryvault.velocityservice.dto.TransactionDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class LoadingControllerTest {

    @Value("classpath:input.jsonl")
    Resource inputFile;
    @Value("classpath:expected.jsonl")
    Resource outputFile;
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper om;

    @Test
    void testInput() throws Exception {
        var url = "http://localhost:" + port + "/load";
        BufferedReader br = new BufferedReader(new InputStreamReader(inputFile.getInputStream()));
        List<String> actual = br.lines()
                .map(obj -> restTemplate.postForObject(url, deserialize(obj, TransactionDTO.class), String.class))
                .filter(Objects::nonNull)
                .toList();
        Path actualPath = Paths.get("actual.jsonl");
        Files.write(actualPath, actual);
        var expected = loadExpectedOutput();
        assertEquals(999, actual.size());
        assertEquals(expected, actual);
        Files.delete(actualPath);
    }

    private List<String> loadExpectedOutput() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(outputFile.getInputStream()));
        return br.lines().collect(Collectors.toList());
    }

    private <T> T deserialize(String json, Class<T> type) {
        try {
            return om.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}