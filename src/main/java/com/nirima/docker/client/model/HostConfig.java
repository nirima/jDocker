package com.nirima.docker.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Objects;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.jvnet.hk2.component.MultiMap;

import java.io.Serializable;
import java.util.Collection;

import java.util.HashMap;
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
    @JsonProperty("Links")
    private String[] links;

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

    /**
     * Set up some port mappings
     * **/
    public void setPortBindings(Iterable<PortMapping> portMappingCollection) {

        Multimap<String, PortBinding> bindings = ArrayListMultimap.create();

        for(PortMapping portMapping : portMappingCollection) {
            bindings.put(portMapping.getContainerPortString(), portMapping.getHostPortBinding());
        }

        Map<String, PortBinding[]> values = new HashMap<String, PortBinding[]>();
        for( String key : bindings.keySet() ) {
            values.put(key,  bindings.get(key).toArray(new PortBinding[1]));
        }
        setPortBindings(values);

    }

    public String[] getBinds() {
        return binds;
    }

    public void setBinds(String[] binds) {
        this.binds = binds;
    }

    public String[] getLinks() {
        return links;
    }

    public void setLinks(String[] links) {
        this.links = links;
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

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("binds", binds)
                .add("containerIDFile", containerIDFile)
                .add("lxcConf", lxcConf)
                .add("portBindings", portBindings)
                .add("privileged", privileged)
                .add("publishAllPorts", publishAllPorts)
                .add("dns", dns)
                .add("volumesFrom", volumesFrom)
                .toString();
    }
}
