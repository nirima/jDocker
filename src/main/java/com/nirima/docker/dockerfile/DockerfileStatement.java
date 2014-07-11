package com.nirima.docker.dockerfile;

import com.nirima.docker.client.DockerException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;

/**
 * Created by magnayn on 11/07/2014.
 */
public class DockerfileStatement {

    protected final String statement;

    private DockerfileStatement(String statement) {
        this.statement = statement;
    }

    public static class Add extends DockerfileStatement {

        private Add(String statement) {
            super(statement);
        }

        public String getSource() {
            return StringUtils.split(statement, " \t")[1];
        }

        public String getDestination() {
            return StringUtils.split(statement, " \t")[2];
        }
    }

    public static DockerfileStatement createFromLine(String cmd) {

        String addArgs[] = StringUtils.split(cmd, " \t");

        if (addArgs.length > 0 && addArgs[0].equalsIgnoreCase("ADD")) {

            if (addArgs.length != 3) {
                throw new DockerException(String.format("Wrong format on line [%s]", cmd));
            }
            return new Add(cmd);
        } else {
            return new DockerfileStatement(cmd);
        }
    }
}
