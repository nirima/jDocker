package com.nirima.docker.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by magnayn on 02/02/2014.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Event {
    @JsonProperty
    String status;

    @JsonProperty
    String id;

    @JsonProperty
    String from;

    @JsonProperty
    Long time;

}
