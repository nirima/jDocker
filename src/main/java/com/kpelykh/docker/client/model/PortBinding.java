package com.kpelykh.docker.client.model;

import com.google.common.base.Objects;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Created by magnayn on 09/01/2014.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class PortBinding {

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
