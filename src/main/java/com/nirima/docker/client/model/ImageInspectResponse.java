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
public class ImageInspectResponse  implements Serializable {

    @JsonProperty("id")
    private String _id;

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Parent") private String parent;

    @JsonProperty("Created") private String created;

    @JsonProperty("Container") private String container;

    @JsonProperty("ContainerConfig") private ContainerConfig containerConfig;

    @JsonProperty("Size") private long size;

    @JsonProperty("DockerVersion") private String dockerVersion;

    @JsonProperty("Config") private ContainerConfig config;

    @JsonProperty("Architecture") private String arch;

    @JsonProperty("Comment") private String comment;

    @JsonProperty("Author") private String author;

    public String getId() {
        return Objects.firstNonNull(id, _id);
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
                .add("id", getId())
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
