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
public class Version {


    @JsonProperty("Version")
    private String version;

    @JsonProperty("GitCommit")
    private String  gitCommit;

    @JsonProperty("GoVersion")
    private String  goVersion;

    public String getVersion() {
        return version;
    }

    public String getGitCommit() {
        return gitCommit;
    }

    public String getGoVersion() {
        return goVersion;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("version", version)
                .add("gitCommit", gitCommit)
                .add("goVersion", goVersion)
                .toString();
    }
}
