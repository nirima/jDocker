package com.nirima.docker.client.model;


import com.google.common.base.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContainerInspectResponse {

    @JsonProperty("ID")
    private String id;

    @JsonProperty("Created")
    private String created;

    @JsonProperty("Path")
    private String path;

    @JsonProperty("Args")
    private String[] args;

    @JsonProperty("Config")
    public ContainerConfig config;

    @JsonProperty("State")
    private ContainerState state;

    @JsonProperty("Image")
    private String image;

    @JsonProperty("NetworkSettings")
    private NetworkSettings networkSettings;

    @JsonProperty("SysInitPath")
    private String sysInitPath;

    @JsonProperty("ResolvConfPath")
    private String resolvConfPath;

    @JsonProperty("Volumes")
    private Map<String, String> volumes;

    @JsonProperty("VolumesRW")
    private Map<String, String> volumesRW;

    @JsonProperty("HostnamePath")
    private String hostnamePath;

    @JsonProperty("HostsPath")
    private String hostsPath;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Driver")
    private String driver;

    public String getDriver() {
        return driver;
    }

    public String getHostnamePath() {
        return hostnamePath;
    }

    public String getHostsPath() {
        return hostsPath;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getCreated() {
        return created;
    }

    public String getPath() {
        return path;
    }

    public String[] getArgs() {
        return args;
    }

    public ContainerConfig getConfig() {
        return config;
    }

    public ContainerState getState() {
        return state;
    }

    public String getImage() {
        return image;
    }

    public NetworkSettings getNetworkSettings() {
        return networkSettings;
    }

    public String getSysInitPath() {
        return sysInitPath;
    }

    public String getResolvConfPath() {
        return resolvConfPath;
    }

    public Map<String, String> getVolumes() {
        return volumes;
    }

    public Map<String, String> getVolumesRW() {
        return volumesRW;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("created", created)
                .add("path", path)
                .add("args", args)
                .add("config", config)
                .add("state", state)
                .add("image", image)
                .add("networkSettings", networkSettings)
                .add("sysInitPath", sysInitPath)
                .add("resolvConfPath", resolvConfPath)
                .add("volumes", volumes)
                .add("volumesRW", volumesRW)
                .add("hostnamePath", hostnamePath)
                .add("hostsPath", hostsPath)
                .add("name", name)
                .add("driver", driver)
                .toString();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class NetworkSettings {

        @JsonProperty("IPAddress") public String ipAddress;
        @JsonProperty("IPPrefixLen") public int ipPrefixLen;
        @JsonProperty("Gateway") public String gateway;
        @JsonProperty("Bridge") public String bridge;

        @JsonProperty("PortMapping") public String portMapping;

        // Deprecated - can we remove?
//        @JsonProperty("PortMapping") public Map<String,Map<String, String>> portMapping;
        // FIXME Is this the right type? -BJE
        @JsonProperty("Ports") public Map<String, PortBinding[]> ports;

        @Override
        public String toString() {
            return "NetworkSettings{" +
                    "ipAddress='" + ipAddress + '\'' +
                    ", ipPrefixLen=" + ipPrefixLen +
                    ", gateway='" + gateway + '\'' +
                    ", bridge='" + bridge + '\'' +
                    ", ports=" + ports +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class ContainerState {

        @JsonProperty("Running") public boolean running;
        @JsonProperty("Pid") public int pid;
        @JsonProperty("ExitCode") public int exitCode;
        @JsonProperty("StartedAt") public String startedAt;
        @JsonProperty("Ghost") public boolean ghost;
        @JsonProperty("FinishedAt") private String finishedAt;

        @Override
        public String toString() {
            return Objects.toStringHelper(this)
                    .add("running", running)
                    .add("pid", pid)
                    .add("exitCode", exitCode)
                    .add("startedAt", startedAt)
                    .add("ghost", ghost)
                    .add("finishedAt", finishedAt)
                    .toString();
        }
    }

}
