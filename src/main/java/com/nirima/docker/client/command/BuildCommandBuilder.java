package com.nirima.docker.client.command;


import com.nirima.docker.client.DockerClient;
import com.nirima.docker.client.DockerException;
import com.nirima.docker.client.model.EventStreamItem;

import java.io.File;
import java.util.Collection;

public class BuildCommandBuilder {
    private final DockerClient client;
    private File dockerFolder;
    private String tag;

    public BuildCommandBuilder(DockerClient client) {
        this.client = client;
    }

    public BuildCommandBuilder dockerFileFolder(File path) {
        this.dockerFolder = path;
        return this;
    }

    public BuildCommandBuilder tag(String tag) {
        this.tag = tag;
        return this;
    }

    public BuildCommandResponse execute() throws DockerException {
        Collection<EventStreamItem> items = client.system().build(dockerFolder, tag);
        return new BuildCommandResponse(items);
    }
}
