package com.nirima.docker.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by magnayn on 02/02/2014.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class IdResponse {
    @JsonProperty("Id")
    private String id;

    public String getId() {
        return id;
    }
}
