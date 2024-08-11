package com.kingbean.jsonutilstool.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonUtils {
    private static final Configuration CONFIG_READ_PATH = Configuration.builder().options(Option.AS_PATH_LIST, Option.SUPPRESS_EXCEPTIONS).build();
    private static final Configuration CONFIG_PARSE_DOC = Configuration.builder().options(Option.DEFAULT_PATH_LEAF_TO_NULL, Option.SUPPRESS_EXCEPTIONS).build();
    private static final Gson GSON = new Gson();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static JsonObject shortenJson(JsonObject jsonObject, List<String> whiteList, List<String> blackList) {
        var jsonString = convertJsonObjectToJsonString(jsonObject);
        var result = shortenMessageByWhitelistJsonPaths(jsonString, whiteList);
        result = shortenMessageByBlacklistJsonPaths(result, blackList);
        return convertJsonStringToJsonObject(result);
    }

    public static String shortenMessageByWhitelistJsonPaths(String messageJsonString, List<String> whitelistJsonPaths) {
        if (whitelistJsonPaths.isEmpty()) {
            return messageJsonString;
        }
        var result = new JsonObject();
        var originalJsonObject = convertJsonStringToJsonObject(messageJsonString);
        for (var jsonPath : whitelistJsonPaths) {
            result = fillValue(originalJsonObject, jsonPath, result);
        }
        return convertJsonObjectToJsonString(result);
    }

    public static String shortenMessageByBlacklistJsonPaths(String messageJsonString, List<String> blacklistJsonPaths) {
        if (blacklistJsonPaths.isEmpty()) {
            return messageJsonString;
        }
        var messageDocContext = JsonPath.using(CONFIG_PARSE_DOC).parse(messageJsonString);
        for (var jsonPath : blacklistJsonPaths) {
            messageDocContext.delete(jsonPath);
        }
        return messageDocContext.jsonString();
    }

    public static JsonObject fillValue(JsonObject originalJsonObject, String jsonPath, JsonObject jsonObjectNeedsFillValue) {
        findEndPath(jsonObjectNeedsFillValue, convertJsonObjectToInteratorMapEntry(originalJsonObject));
        var originalJsonString = convertJsonObjectToJsonString(originalJsonObject);
        List<String> pathDocContexts = JsonPath.using(CONFIG_READ_PATH).parse(originalJsonString).read(jsonPath);
        var resultDocContexts = JsonPath.using(CONFIG_PARSE_DOC).parse(convertJsonObjectToJsonString(jsonObjectNeedsFillValue));
        for (var pathDocContext : pathDocContexts) {
            resultDocContexts.set(pathDocContext, JsonPath.using(CONFIG_PARSE_DOC).parse(originalJsonString).read(pathDocContext));
        }
        return convertJsonStringToJsonObject(resultDocContexts.jsonString());
    }

    public static void findEndPath(JsonObject result, Iterator<Map.Entry<String, Object>> iterator) {
        while (iterator.hasNext()) {
            var entry = iterator.next();
            var key = entry.getKey();
            var value = entry.getValue();
            if (null != result.get(key)) {
                continue;
            }
            if (value instanceof JsonObject jsonObject) {
                var newJsonObject = new JsonObject();
                result.add(key, newJsonObject);
                findEndPath(newJsonObject, convertJsonObjectToInteratorMapEntry(jsonObject));
            } else if (value instanceof JsonArray jsonArray) {
                var newJsonArray = new JsonArray();
                result.add(key, newJsonArray);
                findEndArrPath(newJsonArray, jsonArray.iterator());
            }
        }
    }

    public static void findEndArrPath(JsonArray result, Iterator<JsonElement> iterator) {
        while (iterator.hasNext()) {
            var element = iterator.next();
            if (element instanceof JsonObject jsonObject) {
                var newJsonObject = new JsonObject();
                result.add(newJsonObject);
                findEndPath(newJsonObject, convertJsonObjectToInteratorMapEntry(jsonObject));
            } else if (element instanceof JsonArray jsonArray) {
                var newJsonArray = new JsonArray();
                result.add(newJsonArray);
                findEndArrPath(newJsonArray, jsonArray.iterator());
            }
        }
    }

    public static JsonObject convertJsonNodeToJsonObject(JsonNode jsonNode) {
        try {
            return GSON.fromJson(OBJECT_MAPPER.writeValueAsString(jsonNode), JsonObject.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static JsonNode convertJsonObjectToJsonNode(JsonObject jsonObject) {
        try {
            return OBJECT_MAPPER.readTree(GSON.toJson(jsonObject));
        } catch (Exception e) {
            return null;
        }
    }

    public static JsonObject convertJsonStringToJsonObject(String jsonString) {
        try {
            return GSON.fromJson(jsonString, JsonObject.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static String convertJsonObjectToJsonString(JsonObject jsonObject) {
        try {
            return GSON.toJson(jsonObject);
        } catch (Exception e) {
            return null;
        }
    }

    public static Iterator<Map.Entry<String, Object>> convertJsonObjectToInteratorMapEntry(JsonObject jsonObject) {
        var entrySet = jsonObject.entrySet();
        return new Iterator<Map.Entry<String, Object>>() {
            private Iterator<Map.Entry<String, JsonElement>> iterator = entrySet.iterator();
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            @Override
            public Map.Entry<String, Object> next() {
                var entry = iterator.next();
                return new Map.Entry<String, Object>() {
                    @Override
                    public String getKey() {
                        return entry.getKey();
                    }
                    @Override
                    public Object getValue() {
                        return entry.getValue();
                    }
                    @Override
                    public Object setValue(Object value) {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }
}
