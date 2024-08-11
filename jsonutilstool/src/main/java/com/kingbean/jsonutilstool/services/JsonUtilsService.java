package com.kingbean.jsonutilstool.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.kingbean.jsonutilstool.entities.ShortenJsonResponse;

public interface JsonUtilsService {
    ShortenJsonResponse handleShortenJsonRequest(JsonNode shortenJsonRequest);
}
