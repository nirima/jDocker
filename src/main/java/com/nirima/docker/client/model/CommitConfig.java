package com.nirima.docker.client.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class CommitConfig  implements Serializable {

    @JsonProperty("container")
    private String container;

    @JsonProperty("repo")
    private String repo;

    @JsonProperty("tag")
    private String tag;

    @JsonProperty("m")
    private String message;

    //author (eg. “John Hannibal Smith <hannibal@a-team.com>”)
    @JsonProperty("author")
    private String author;

    //config automatically applied when the image is run. (ex: {“Cmd”: [“cat”, “/world”], “PortSpecs”:[“22”]})
    @JsonProperty("run")
    private String run;

    public String getContainer() {
        return container;
    }

    public String getRepo() {
        return repo;
    }

    public String getTag() {
        return tag;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }

    public String getRun() {
        return run;
    }

}
