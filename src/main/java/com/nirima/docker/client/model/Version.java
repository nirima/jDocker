package com.nirima.docker.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Version  implements Serializable {

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

    public int[] getVersionComponents() {
        String[] v = StringUtils.split(getVersion(), ".");
        int[] result = new int[v.length];
        for(int i=0; i<v.length;i++) {
            result[i] = Integer.parseInt(v[i]);
        }
        return result;
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
