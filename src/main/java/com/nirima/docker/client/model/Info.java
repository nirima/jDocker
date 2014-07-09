package com.nirima.docker.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;


import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Info  implements Serializable {

    @JsonProperty("Debug")
    private boolean debug;

    @JsonProperty("Containers")
    private int    containers;

    @JsonProperty("Driver")
    private String driver;

    @JsonProperty("DriverStatus")
    private List<Object> driverStatuses;

    @JsonProperty("Images")
    private int    images;

    @JsonProperty("IPv4Forwarding")
    private String IPv4Forwarding;

    @JsonProperty("IndexServerAddress")
    private String IndexServerAddress;

    @JsonProperty("KernelVersion")
    private String kernelVersion;

    @JsonProperty("LXCVersion")
    private String lxcVersion;

    @JsonProperty("MemoryLimit")
    private boolean memoryLimit;

    @JsonProperty("NEventsListener")
    private long nEventListener;

    @JsonProperty("NFd")
    private int    NFd;

    @JsonProperty("NGoroutines")
    private int    NGoroutines;

    @JsonProperty("InitPath")
    private String initPath;

    @JsonProperty("InitSha1")
    private String initSha1;

    @JsonProperty("SwapLimit")
    private int swapLimit;

    @JsonProperty("ExecutionDriver")
    private String executionDriver;

    public boolean isDebug() {
        return debug;
    }

    public int getContainers() {
        return containers;
    }

    public String getDriver() {
        return driver;
    }

    public List<Object> getDriverStatuses() {
        return driverStatuses;
    }

    public int getImages() {
        return images;
    }

    public String getIPv4Forwarding() {
        return IPv4Forwarding;
    }

    public String getIndexServerAddress() {
        return IndexServerAddress;
    }

    public String getKernelVersion() {
        return kernelVersion;
    }

    public String getLxcVersion() {
        return lxcVersion;
    }

    public boolean isMemoryLimit() {
        return memoryLimit;
    }

    public long getnEventListener() {
        return nEventListener;
    }

    public int getNFd() {
        return NFd;
    }

    public int getNGoroutines() {
        return NGoroutines;
    }

    public String getExecutionDriver() {
        return executionDriver;
    }

    public int getSwapLimit() {
        return swapLimit;
    }

    public String getInitPath() {
        return initPath;
    }

    public String getInitSha1() {
        return initSha1;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("debug", debug)
                .add("containers", containers)
                .add("driver", driver)
                .add("driverStatuses", driverStatuses)
                .add("images", images)
                .add("IPv4Forwarding", IPv4Forwarding)
                .add("IndexServerAddress", IndexServerAddress)
                .add("kernelVersion", kernelVersion)
                .add("lxcVersion", lxcVersion)
                .add("memoryLimit", memoryLimit)
                .add("nEventListener", nEventListener)
                .add("NFd", NFd)
                .add("NGoroutines", NGoroutines)
                .add("initPath", initPath)
                .add("initSha1", initSha1)
                .add("swapLimit", swapLimit)
                .add("executionDriver", executionDriver)
                .toString();
    }
}
