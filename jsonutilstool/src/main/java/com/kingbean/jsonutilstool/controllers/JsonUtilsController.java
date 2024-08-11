package com.kingbean.jsonutilstool.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.kingbean.jsonutilstool.entities.ShortenJsonResponse;
import com.kingbean.jsonutilstool.services.JsonUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jsonUtils")
public class JsonUtilsController {
    @Autowired
    private JsonUtilsService jsonUtilsService;

    @PostMapping(value = "/shortenJson", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ShortenJsonResponse> receiveJsonObject(@RequestBody JsonNode shortenJsonRequest) {
        return ResponseEntity.ok(jsonUtilsService.handleShortenJsonRequest(shortenJsonRequest));
    }
}