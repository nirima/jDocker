package com.nirima.docker.client.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.common.collect.ImmutableList;
import com.nirima.docker.client.DockerClient;
import com.nirima.docker.client.DockerException;
import com.nirima.docker.client.model.EventStreamItem;
import com.nirima.docker.client.model.PushEventStreamItem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

/**
 * Create a push command.
 */
public class PushCommandBuilder {
    private final DockerClient client;
    private String name;
    private String registry;
    private String tag;

    public PushCommandBuilder(DockerClient client) {
        this.client = client;
    }

    public PushCommandBuilder name(String name) {
        this.name = name;
        return this;
    }

    public PushCommandBuilder registry(String registry) {
        this.registry = registry;
        return this;
    }

    public PushCommandResponse execute() throws IOException {
        InputStream response = client.imagesApi().pushImageOnRegistry(name, registry);
        return new PushCommandResponse(makePushStream(response));
    }

    private Collection<PushEventStreamItem> makePushStream(InputStream inputStream) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        // we'll be reading instances of MyBean
        ObjectReader reader = mapper.reader(PushEventStreamItem.class);
        // and then do other configuration, if any, and read:
        Iterator<PushEventStreamItem> items = reader.readValues(inputStream);

        return ImmutableList.copyOf(items);
    }

}
