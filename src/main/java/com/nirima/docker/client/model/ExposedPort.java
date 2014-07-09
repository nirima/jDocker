package com.nirima.docker.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;


/**
 * Created by magnayn on 09/01/2014.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ExposedPort implements Serializable {

    @JsonProperty
    @JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
    String port;
}
