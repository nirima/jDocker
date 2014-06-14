package com.nirima.docker.client.model;

import com.google.common.base.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class SearchItem {

    @JsonProperty("star_count")
    private int starCount;

    @JsonProperty("is_official")
    private boolean isOfficial;

    @JsonProperty("is_automated")
    private boolean isAutomated;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    public int getStarCount() {
        return starCount;
    }

    public boolean isOfficial() {
        return isOfficial;
    }

    public boolean isAutomated() {
        return isAutomated;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("starCount", starCount)
                .add("isOfficial", isOfficial)
                .add("isAutomated", isAutomated)
                .add("name", name)
                .add("description", description)
                .toString();
    }
}
