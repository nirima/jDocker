package com.nirima.docker.client.model;

import com.google.common.base.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ImageInspectResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("parent") private String parent;

    @JsonProperty("created") private String created;

    @JsonProperty("container") private String container;

    @JsonProperty("container_config") private ContainerConfig containerConfig;

    @JsonProperty("Size") private long size;

    @JsonProperty("docker_version") private String dockerVersion;

    @JsonProperty("config") private ContainerConfig config;

    @JsonProperty("architecture") private String arch;

    @JsonProperty("comment") private String comment;

    @JsonProperty("author") private String author;

    public String getId() {
        return id;
    }

    public String getParent() {
        return parent;
    }

    public String getCreated() {
        return created;
    }

    public String getContainer() {
        return container;
    }

    public ContainerConfig getContainerConfig() {
        return containerConfig;
    }

    public long getSize() {
        return size;
    }

    public String getDockerVersion() {
        return dockerVersion;
    }

    public ContainerConfig getConfig() {
        return config;
    }

    public String getArch() {
        return arch;
    }

    public String getComment() {
        return comment;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("parent", parent)
                .add("created", created)
                .add("container", container)
                .add("containerConfig", containerConfig)
                .add("size", size)
                .add("dockerVersion", dockerVersion)
                .add("config", config)
                .add("arch", arch)
                .add("comment", comment)
                .add("author", author)
                .toString();
    }
}
