package com.kpelykh.docker.client.model;

import com.google.common.base.Objects;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Container {

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Command")
    private String command;

    @JsonProperty("Image")
    private String image;

    @JsonProperty("Created")
    private long created;

    @JsonProperty("Status")
    private String status;

    /* Example:
       "Ports": {
            "22/tcp": [
                {
                    "HostIp": "0.0.0.0",
                    "HostPort": "8022"
                }
            ]
        }
     */
    @JsonProperty("Ports")
    private Ports ports;

    @JsonProperty("SizeRw")
    private int size;

    @JsonProperty("SizeRootFs")
    private int sizeRootFs;

    public String getId() {
        return id;
    }

    public String getCommand() {
        return command;
    }

    public String getImage() {
        return image;
    }

    public long getCreated() {
        return created;
    }

    public String getStatus() {
        return status;
    }

    public Ports getPorts() {
        return ports;
    }

    public int getSize() {
        return size;
    }

    public int getSizeRootFs() {
        return sizeRootFs;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("command", command)
                .add("image", image)
                .add("created", created)
                .add("status", status)
                .add("ports", ports)
                .add("size", size)
                .add("sizeRootFs", sizeRootFs)
                .toString();
    }
}
