package com.nirima.docker.client.model;

import com.google.common.base.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by ben on 12/12/13.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DriverStatus implements Serializable {

    @JsonProperty("Root Dir")
    private String rootDir;

    @JsonProperty("Dirs")
    private int dirs;

    public String getRootDir() {
        return rootDir;
    }

    public int getDirs() {
        return dirs;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("rootDir", rootDir)
                .add("dirs", dirs)
                .toString();
    }
}
