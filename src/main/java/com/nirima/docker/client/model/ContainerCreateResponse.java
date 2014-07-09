package com.nirima.docker.client.model;

import com.google.common.base.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ContainerCreateResponse implements Serializable{

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Warnings")
    private String[] warnings;

    public String getId() {
        return id;
    }

    public String[] getWarnings() {
        return warnings;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("warnings", warnings)
                .toString();
    }
}
