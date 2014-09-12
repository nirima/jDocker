package com.nirima.docker.dockerfile;

import junit.framework.TestCase;

import java.io.File;
import java.util.Collection;

public class DockerfileTest extends TestCase {

    public void testGetStatements() throws Exception {
        File baseDir = new File(Thread.currentThread().getContextClassLoader().getResource("testAddFile").getFile());
        File df = new File(baseDir, "Dockerfile");
        Dockerfile dockerFile = new Dockerfile(df);
        Collection<DockerfileStatement> statements = dockerFile.getStatements();
        for( DockerfileStatement statement : statements ) {
            if( statement instanceof DockerfileStatement.Add )
            {
                DockerfileStatement.Add add = (DockerfileStatement.Add)statement;
                System.out.println(add);
            }
        }
    }
}