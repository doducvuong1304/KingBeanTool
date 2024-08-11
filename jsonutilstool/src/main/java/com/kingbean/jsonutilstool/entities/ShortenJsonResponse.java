package com.kingbean.jsonutilstool.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShortenJsonResponse {
    private List<String> blackList;
    private List<String> whiteList;
    private JsonNode jsonObjectShortened;
    private String errorMessage;
}
