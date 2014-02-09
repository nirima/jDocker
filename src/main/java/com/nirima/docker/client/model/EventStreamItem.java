package com.nirima.docker.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by magnayn on 09/02/2014.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class EventStreamItem {

    @JsonProperty("stream")
    private String stream;

    // {"error":"Error...", "errorDetail":{"code": 123, "message": "Error..."}}
    @JsonProperty("error")
    private String error;

    @JsonProperty("errorDetail")
    private ErrorDetail errorDetail;

    public String getStream() {
        return stream;
    }

    public String getError() {
        return error;
    }

    public ErrorDetail getErrorDetail() {
        return errorDetail;
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class ErrorDetail {
        @JsonProperty("code")
        String code;
        @JsonProperty("message")
        String message;
    }
}
