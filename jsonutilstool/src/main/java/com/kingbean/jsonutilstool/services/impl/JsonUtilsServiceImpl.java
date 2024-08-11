package com.kingbean.jsonutilstool.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.kingbean.jsonutilstool.entities.ShortenJsonResponse;
import com.kingbean.jsonutilstool.services.JsonUtilsService;
import com.kingbean.jsonutilstool.utils.JsonUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class JsonUtilsServiceImpl implements JsonUtilsService {
    @Override
    public ShortenJsonResponse handleShortenJsonRequest(JsonNode shortenJsonRequest) {
        var requestJson = JsonUtils.convertJsonNodeToJsonObject(shortenJsonRequest);
        if (requestJson == null || requestJson.isJsonNull()) {
            var response = new ShortenJsonResponse();
            response.setErrorMessage("Null or empty Request body!");
            return response;
        }
        var jsonObject = Optional.ofNullable(requestJson.get("jsonObject")).map(JsonElement::getAsJsonObject).orElse(null);
        if (jsonObject == null) {
            var response = new ShortenJsonResponse();
            response.setErrorMessage("Null or empty Json need to shorten!");
            return response;
        }
        var whiteList = Optional.ofNullable(requestJson.get("whiteList")).map(JsonElement::getAsJsonArray).map(JsonArray::asList).map(listElement -> listElement.stream().map(JsonElement::getAsString).toList()).orElse(new ArrayList<String>());
        var blackList = Optional.ofNullable(requestJson.get("blackList")).map(JsonElement::getAsJsonArray).map(JsonArray::asList).map(listElement -> listElement.stream().map(JsonElement::getAsString).toList()).orElse(new ArrayList<String>());
        if (whiteList.isEmpty() && blackList.isEmpty()) {
            var response = new ShortenJsonResponse();
            response.setJsonObjectShortened(JsonUtils.convertJsonObjectToJsonNode(jsonObject));
            response.setErrorMessage("Null or empty WhiteList/ BlackList JsonPath for handle shorten Json!");
            return response;
        }
        var result = new ShortenJsonResponse();
        result.setWhiteList(whiteList);
        result.setBlackList(blackList);
        var shortenedJson = JsonUtils.shortenJson(jsonObject, whiteList, blackList);
        result.setErrorMessage("Handle shorten Json SUCCESSFULLY!");
        result.setJsonObjectShortened(JsonUtils.convertJsonObjectToJsonNode(shortenedJson));
        return result;
    }
}
