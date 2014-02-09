package com.nirima.docker.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Created by magnayn on 09/01/2014.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ExposedPort {

    @JsonProperty
    @JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
    String port;
}
