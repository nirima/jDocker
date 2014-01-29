package com.kpelykh.docker.client.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Map;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class HostConfig {

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

    public class LxcConf {
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
