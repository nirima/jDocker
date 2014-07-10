package com.nirima.docker.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * Created by magnayn on 09/02/2014.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class PushEventStreamItem implements Serializable {

    @JsonProperty("status")
    private String status;

    // {"error":"Error...", "errorDetail":{"code": 123, "message": "Error..."}}
    @JsonProperty("progress")
    private String progress;

    @JsonProperty("progressDetail")
    private ProgressDetail progressDetail;


    public String getStatus() {
        return status;
    }

    public String getProgress() {
        return progress;
    }

    public ProgressDetail getProgressDetail() {
        return progressDetail;
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class ProgressDetail implements Serializable {
        @JsonProperty("current")
        int current;


        @Override
        public String toString() {
            return "current " + current;
        }
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("status", status)
                .add("progress", progress)
                .add("progressDetail", progressDetail)
                .toString();
    }
}
