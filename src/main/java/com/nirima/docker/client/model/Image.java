package com.nirima.docker.client.model;

import com.google.common.base.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Image {

    @JsonProperty("Repository")
    private String repository;

    @JsonProperty("Tag")
    private String tag;

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Created")
    private long created;

    @JsonProperty("Size")
    private long size;

    @JsonProperty("VirtualSize")
    private long virtualSize;

    public String getRepository() {
        return repository;
    }

    public String getTag() {
        return tag;
    }

    public String getId() {
        return id;
    }

    public long getCreated() {
        return created;
    }

    public long getSize() {
        return size;
    }

    public long getVirtualSize() {
        return virtualSize;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("repository", repository)
                .add("tag", tag)
                .add("id", id)
                .add("created", created)
                .add("size", size)
                .add("virtualSize", virtualSize)
                .toString();
    }
}
