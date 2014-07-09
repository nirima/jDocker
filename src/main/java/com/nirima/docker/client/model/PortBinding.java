package com.nirima.docker.client.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by magnayn on 09/01/2014.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class PortBinding  implements Serializable {

    @JsonProperty(value = "HostIp")
    @JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
    public String hostIp;

    @JsonProperty(value="HostPort")
    @JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
    public String hostPort;

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("hostIp", hostIp)
                .add("hostPort", hostPort)
                .toString();
    }
}
