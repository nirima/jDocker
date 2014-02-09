package com.nirima.docker.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.nirima.docker.client.model.*;
import com.kpelykh.docker.client.utils.CompressArchiveUtil;
import com.nirima.docker.api.ContainersClient;
import com.nirima.docker.api.ImagesClient;
import com.nirima.docker.api.MiscClient;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.proxy.WebResourceFactory;
import org.glassfish.jersey.filter.LoggingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Created by magnayn on 02/02/2014.
 */
public class DockerClient {

    private static final Logger log = LoggerFactory.getLogger(DockerClient.class);

    public static Builder builder() {
        return new Builder();
    }



    public static class Builder {

        private String serverUrl;

        public Builder withUrl(String url) {
            this.serverUrl = url;
            return this;
        }

        public DockerClient build() {
            return new DockerClient(serverUrl);
        }
    }

    private final String serverUrl;
    private final WebTarget webTarget;

    private DockerClient(String serverUrl)
    {
        this.serverUrl = serverUrl;

        ClientConfig cc = new ClientConfig();
        LoggingFilter lf = new LoggingFilter(java.util.logging.Logger.getLogger(LoggingFilter.class.getName()), true);

        cc.register(lf);
        this.webTarget = ClientBuilder.newClient(cc).target(serverUrl);

    }

    // Remote APIs------------------------------------------------------------------
    public ImagesClient imagesApi() {
        return WebResourceFactory.newResource(ImagesClient.class, webTarget);
    }

    public ContainersClient containersApi() {
        return WebResourceFactory.newResource(ContainersClient.class, webTarget);
    }

    public MiscClient miscApi() {
        return WebResourceFactory.newResource(MiscClient.class, webTarget);
    }

    // Useful wrapper functions-----------------------------------------------------

    public class System {

        private System() {}

        public Info info() {
            return miscApi().info();
        }
        public Version version() {
            return miscApi().version();
        }

        public Collection<EventStreamItem> build(File dockerFolder, String tag) throws DockerException {
            Preconditions.checkNotNull(dockerFolder, "Folder is null");
            Preconditions.checkArgument(dockerFolder.exists(), "Folder %s doesn't exist", dockerFolder);
            Preconditions.checkState(new File(dockerFolder, "Dockerfile").exists(), "Dockerfile doesn't exist in " + dockerFolder);

            //We need to use Jersey HttpClient here, since ApacheHttpClient4 will not add boundary filed to
            //Content-Type: multipart/form-data; boundary=Boundary_1_372491238_1372806136625


            // ARCHIVE TAR
            String archiveNameWithOutExtension = UUID.randomUUID().toString();

            File dockerFolderTar = null;
            File tmpDockerContextFolder = null;

            try {
                File dockerFile = new File(dockerFolder, "Dockerfile");
                List<String> dockerFileContent = FileUtils.readLines(dockerFile);

                if (dockerFileContent.size() <= 0) {
                    throw new DockerException(String.format("Dockerfile %s is empty", dockerFile));
                }

                //Create tmp docker context folder
                tmpDockerContextFolder = new File(FileUtils.getTempDirectoryPath(), "docker-java-build" + archiveNameWithOutExtension);

                FileUtils.copyFileToDirectory(dockerFile, tmpDockerContextFolder);

                for (String cmd : dockerFileContent) {
                    if (StringUtils.startsWithIgnoreCase(cmd.trim(), "ADD")) {
                        String addArgs[] = StringUtils.split(cmd, " \t");
                        if (addArgs.length != 3) {
                            throw new DockerException(String.format("Wrong format on line [%s]", cmd));
                        }

                        File src = new File(addArgs[1]);
                        if (!src.isAbsolute()) {
                            src = new File(dockerFolder, addArgs[1]).getCanonicalFile();
                        }

                        if (!src.exists()) {
                            throw new DockerException(String.format("Sorce file %s doesnt' exist", src));
                        }
                        if (src.isDirectory()) {
                            FileUtils.copyDirectory(src, tmpDockerContextFolder);
                        } else {
                            FileUtils.copyFileToDirectory(src, tmpDockerContextFolder);
                        }
                    }
                }

                dockerFolderTar = CompressArchiveUtil.archiveTARFiles(tmpDockerContextFolder, archiveNameWithOutExtension);

            } catch (IOException ex) {
                FileUtils.deleteQuietly(dockerFolderTar);
                FileUtils.deleteQuietly(tmpDockerContextFolder);
                throw new DockerException("Error occurred while preparing Docker context folder.", ex);
            }

            try {
                return makeEventStream(miscApi().build(tag, false, false, FileUtils.openInputStream(dockerFolderTar)));
            } catch (Exception e) {
                throw new DockerException(e);
            } finally {
                FileUtils.deleteQuietly(dockerFolderTar);
                FileUtils.deleteQuietly(tmpDockerContextFolder);
            }
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

    public System system() {
        return new System();
    }


    public class PullCommandBuilder {

        private String fromImage;
        private String fromSrc;
        private String repo;
        private String tag;
        private String registry;

        private PullCommandBuilder() {}

        public PullCommandBuilder image(String image) {
            this.fromImage = image;
            return this;
        }

        public PullCommandBuilder fromSrc(String src) {
            this.fromSrc = src;
            return this;
        }

        public PullCommandBuilder repository(String repo) {
            this.repo = repo;
            return this;
        }

        public PullCommandBuilder withTag(String tag) {
            this.tag = tag;
            return this;
        }

        public PullCommandBuilder fromRegistry(String registry) {
            this.registry = registry;
            return this;
        }

        public InputStream execute() {
            if( tag == null )
                tag = "";
            if( registry == null )
                registry = "";

            return imagesApi().createImage(fromImage, fromSrc, repo, tag, registry);
        }
    }

    public PullCommandBuilder createPullCommand() {
        return new PullCommandBuilder();
    }



    // Container functions-----------------------------------------------------
    public class Container {
        private final String containerId;

        private Container(String containerId) { this.containerId = containerId; }

        public void start() {
            containersApi().startContainer(containerId, null);
        }

        public void start(HostConfig hostConfig) {
            containersApi().startContainer(containerId, hostConfig);
        }

        public void stop() {
            containersApi().stopContainer(containerId, null);
        }

        public void stop(long secondsToWait) {
            containersApi().stopContainer(containerId, secondsToWait);
        }

        public void remove() {
            try {
                containersApi().removeContainer(containerId);
            } catch(NotFoundException ex) {
                log.warn("Remove container {} not found", containerId);
            }
        }

        public void remove(boolean includingVolumes) {
            containersApi().removeContainer(containerId, includingVolumes);
        }

        public int waitForContainer() {
            return containersApi().waitForContainer(containerId).StatusCode;
        }

        public List<FileChanges> getFilesystemChanges() {
            return containersApi(). getFilesystemChanges(containerId);
        }

        public void kill() {
            containersApi().killContainer(containerId);
        }

        public void restart() {
            containersApi().restartContainer(containerId, null);
        }

        public void restart(long secondsToWait) {
            containersApi().restartContainer(containerId, secondsToWait);
        }

        public ContainerInspectResponse inspect() {
            return containersApi().inspectContainer(containerId);
        }

        public CommitCommandBuilder createCommitCommand() {
            return new CommitCommandBuilder();
        }

        public InputStream log() {
            return containersApi().attachToContainer(containerId,true,false,false,true,true);
        }


        public final class CommitCommandBuilder {
            private String repo;
            private String tag;
            private String message;
            private String author;
            private String run;

            private CommitCommandBuilder() {
            }

            public CommitCommandBuilder repo(String repo) {
                this.repo = repo;
                return this;
            }

            public CommitCommandBuilder tag(String tag) {
                this.tag = tag;
                return this;
            }

            public CommitCommandBuilder message(String message) {
                this.message = message;
                return this;
            }

            public CommitCommandBuilder author(String author) {
                this.author = author;
                return this;
            }

            public CommitCommandBuilder run(String run) {
                this.run = run;
                return this;
            }

            public String execute() {
                return miscApi().commit(containerId, message,repo,tag,author, run).getId();
            }
        }

    }

    public class ContainerFinder
    {
        private boolean allContainers;
        private boolean latest = false;
        private int limit = -1;
        private boolean showSize;
        private String since;
        private String before;

        /**
         * Show all containers (including ones that have finished : I.E: docker list -a
         * @param allContainers
         * @return
         */
        public ContainerFinder allContainers(boolean allContainers) {
            this.allContainers = allContainers;
            return this;
        }

        public ContainerFinder latest(boolean latest) {
            this.latest = latest;
            return this;
        }

        public ContainerFinder limit(int limit) {
            this.limit = limit;
            return this;
        }

        public ContainerFinder showSize(boolean showSize) {
            this.showSize = showSize;
            return this;
        }

        public ContainerFinder since(String since) {
            this.since = since;
            return this;
        }

        public ContainerFinder before(String before) {
            this.before = before;
            return this;
        }

        public List<com.nirima.docker.client.model.Container> list() {
            return containersApi().listContainers(allContainers, limit,since,before,showSize);
        }
    }

    public class Containers {
        private Containers() {}

        public ContainerFinder finder() {
            return new ContainerFinder();
        }

        public ContainerCreateResponse create(ContainerConfig containerConfig) {
            return containersApi().createContainer(null, containerConfig);
        }
    }

    public Containers containers() {
        return new Containers();
    }

    public Container container(String containerId) {
        Preconditions.checkNotNull(containerId);
        return new Container(containerId);
    }

    // -------------------------------------------------------------------------------------------------
    public class ImageFinder {
        private String filter;
        private boolean allImages = false;

        public ImageFinder filter(String filter) {
            this.filter = filter;
            return this;
        }

        public ImageFinder allImages(boolean allImages) {
            this.allImages = allImages;
            return this;
        }

        public List<com.nirima.docker.client.model.Image> list() {
            return imagesApi().listImages(filter, allImages);
        }
    }

    public class Images {
        public List<com.nirima.docker.client.model.SearchItem> search(String term) {
            return imagesApi().searchForImage(term);
        }


        public void remove(List<String> images) throws DockerException {
            Preconditions.checkNotNull(images, "List of images can't be null");

            for (String imageName : images) {
                imagesApi().removeImage(imageName);
            }
        }

        public ImageFinder finder() {
            return new ImageFinder();
        }
    }

    public class Image {
        private final String imageId;

        private Image(String imageId) { this.imageId = imageId; }

        public void remove() {
            try {
                imagesApi().removeImage(imageId);
            }
            catch(NotFoundException ex) {
                log.warn("Remove Image {} not found", imageId);
            }
        }

        public ImageInspectResponse inspect() {
            return imagesApi().inspectImage(imageId);
        }

        public void tag(String name, boolean b) {
            imagesApi().tagImage(imageId, name, b);
        }

        public void push(String registry) {
            imagesApi().pushImageOnRegistry(imageId, registry);
        }
    }

    public Images images() { return new Images(); }

    public Image image(String imageId) {
        Preconditions.checkNotNull(imageId);
        return new Image(imageId);
    }


}
