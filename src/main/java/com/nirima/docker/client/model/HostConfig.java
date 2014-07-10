package com.nirima.docker.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class HostConfig implements Serializable {

    @JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
    @JsonProperty("Binds")
    private String[] binds;

    @JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
    @JsonProperty("ContainerIDFile")
    private String containerIDFile;

    @JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
    @JsonProperty("LxcConf")
    private LxcConf[] lxcConf;

    @JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
    @JsonProperty("PortBindings")   private Map<String, PortBinding[]> portBindings;

    @JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
    @JsonProperty("Privileged")
    private Boolean privileged;

    @JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
    @JsonProperty("PublishAllPorts")
    private Boolean publishAllPorts;

    @JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
    @JsonProperty("Dns")
    private String[] dns;

    @JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
    @JsonProperty("VolumesFrom")
    private String[] volumesFrom;

    public HostConfig() {

    }

    public HostConfig(String[] binds) {
        this.binds = binds;
    }

    public Map<String, PortBinding[]> getPortBindings() {
        return portBindings;
    }

    public void setPortBindings(Map<String, PortBinding[]> portBindings) {
        this.portBindings = portBindings;
    }

    public String[] getBinds() {
        return binds;
    }

    public void setBinds(String[] binds) {
        this.binds = binds;
    }

    public String getContainerIDFile() {
        return containerIDFile;
    }

    public void setContainerIDFile(String containerIDFile) {
        this.containerIDFile = containerIDFile;
    }

    public LxcConf[] getLxcConf() {
        return lxcConf;
    }

    public void setLxcConf(LxcConf[] lxcConf) {
        this.lxcConf = lxcConf;
    }

    public Boolean getPrivileged() {
        return privileged;
    }

    public void setPrivileged(Boolean privileged) {
        this.privileged = privileged;
    }

    public Boolean getPublishAllPorts() {
        return publishAllPorts;
    }

    public void setPublishAllPorts(Boolean publishAllPorts) {
        this.publishAllPorts = publishAllPorts;
    }

    public String[] getDns() {
        return dns;
    }

    public void setDns(String[] dns) {
        this.dns = dns;
    }

    public String[] getVolumesFrom() {
        return volumesFrom;
    }

    public void setVolumesFrom(String[] volumesFrom) {
        this.volumesFrom = volumesFrom;
    }

    public class LxcConf implements Serializable {
        @JsonProperty("Key")
        public String key;

        @JsonProperty("Value")
        public String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
}
