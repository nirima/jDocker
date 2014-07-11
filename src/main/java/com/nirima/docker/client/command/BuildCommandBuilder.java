package com.nirima.docker.client.command;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.kpelykh.docker.client.utils.CompressArchiveUtil;
import com.nirima.docker.client.DockerClient;
import com.nirima.docker.client.DockerException;
import com.nirima.docker.client.model.EventStreamItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class BuildCommandBuilder {
    private final DockerClient client;
    private File dockerFile;
    private File tarFile;
    private String tag;

    public BuildCommandBuilder(DockerClient client) {
        this.client = client;
    }

    public BuildCommandBuilder dockerFile(File path) {
        if( !path.exists() )
            throw new DockerException("Dockerfile does not exist");
        this.dockerFile = path;
        return this;
    }

    public BuildCommandBuilder useTarFile(File tarFile) {
        this.tarFile = tarFile;
        return this;
    }

    public BuildCommandBuilder tag(String tag) {
        this.tag = tag;
        return this;
    }

    public BuildCommandResponse execute() throws IOException {

        boolean delete = false;

        if( tarFile == null ) {
            delete = true;
            tarFile = makeTarFile();
        }

        InputStream results;
        FileInputStream tarInputStream = new FileInputStream(tarFile);
        try {
            results = client.miscApi().build(tag, false, false, false, false, tarInputStream);
        } finally {
            tarInputStream.close();

            if( delete ) {
                tarFile.delete();
                tarFile = null;
            }
        }

        Collection<EventStreamItem> items = makeEventStream( results );

        return new BuildCommandResponse(items);
    }

    private File makeTarFile() throws IOException {
        // Dockerfile points to path.
        File dockerPath = dockerFile.getParentFile();

        // ARCHIVE TAR
        String archiveNameWithOutExtension = UUID.randomUUID().toString();

        return CompressArchiveUtil.archiveTARFiles(dockerPath, archiveNameWithOutExtension);

    }

    private Collection<EventStreamItem> makeEventStream(InputStream inputStream) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        // we'll be reading instances of MyBean
        ObjectReader reader = mapper.reader(EventStreamItem.class);
        // and then do other configuration, if any, and read:
        Iterator<EventStreamItem> items = reader.readValues(inputStream);

        return ImmutableList.copyOf(items);
    }
}
