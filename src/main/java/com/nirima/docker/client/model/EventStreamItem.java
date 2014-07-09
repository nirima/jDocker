package com.nirima.docker.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * Created by magnayn on 09/02/2014.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class EventStreamItem implements Serializable {

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

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("code", code)
                    .add("message", message)
                    .toString();
        }
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("stream", stream)
                .add("error", error)
                .add("errorDetail", errorDetail)
                .toString();
    }
}
