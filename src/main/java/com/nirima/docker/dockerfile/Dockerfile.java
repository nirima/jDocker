package com.nirima.docker.dockerfile;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.kpelykh.docker.client.utils.CompressArchiveUtil;
import com.nirima.docker.client.DockerException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

/**
 * Parse a dockerfile
 */
public class Dockerfile {

    private File dockerFile;

    public Dockerfile(File dockerFile) {
        this.dockerFile = dockerFile;
    }

    public Collection<DockerfileStatement> getStatements() throws IOException {
        List<String> dockerFileContent = FileUtils.readLines(dockerFile);

        return Collections2.transform(dockerFileContent, new Function<String, DockerfileStatement>() {
            @Override
            public DockerfileStatement apply(@Nullable String input) {
                return DockerfileStatement.createFromLine(input);
            }
        });
   }
}
