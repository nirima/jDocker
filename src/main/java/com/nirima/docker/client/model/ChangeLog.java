package com.nirima.docker.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;


/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ChangeLog {

    @JsonProperty("Path")
    private String path;

    @JsonProperty("Kind")
    private int kind;

    public String getPath() {
        return path;
    }

    public int getKind() {
        return kind;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("path", path)
                .add("kind", kind)
                .toString();
    }
}
