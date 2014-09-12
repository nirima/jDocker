package com.nirima.docker.client;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterators;
import com.nirima.docker.api.ContainersClient;
import com.nirima.docker.api.ImagesClient;
import com.nirima.docker.api.MiscClient;
import com.nirima.docker.client.command.BuildCommandBuilder;
import com.nirima.docker.client.command.PushCommandBuilder;
import com.nirima.docker.client.model.ContainerConfig;
import com.nirima.docker.client.model.ContainerCreateResponse;
import com.nirima.docker.client.model.ContainerInspectResponse;
import com.nirima.docker.client.model.EventStreamItem;
import com.nirima.docker.client.model.FileChanges;
import com.nirima.docker.client.model.HostConfig;
import com.nirima.docker.client.model.Identifier;
import com.nirima.docker.client.model.ImageAction;
import com.nirima.docker.client.model.ImageInspectResponse;
import com.nirima.docker.client.model.Info;
import com.nirima.docker.client.model.Version;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.proxy.WebResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Form;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Top-level docker client interface.
 */
public class DockerClient extends DockerClientBase implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(DockerClient.class);

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends DockerClientBase.Builder<Builder> {

        public Builder fromClient (DockerClient client) {
            this.serverUrl = client.serverUrl;
            connectTimeout = (Integer)client.webTarget.getConfiguration().getProperty(ClientProperties.CONNECT_TIMEOUT);
            readTimeout = (Integer)client.webTarget.getConfiguration().getProperty(ClientProperties.CONNECT_TIMEOUT);
            return this;
        }

        public DockerClient build() {
            Preconditions.checkNotNull(serverUrl);
            return new DockerClient(serverUrl, getClientConfig(), getClientConfigChunked());
        }
    }

    public DockerClient(String serverUrl, ClientConfig clientConfig, ClientConfig clientConfigChunked) {
        super(serverUrl, clientConfig, clientConfigChunked);
    }

    // Remote APIs------------------------------------------------------------------
    public ImagesClient imagesApi() {
        return WebResourceFactory.newResource(ImagesClient.class, webTarget, false, getHeaders(),
                Collections.<Cookie>emptyList(), new Form());

    }

    public ContainersClient containersApi() {
        return WebResourceFactory.newResource(ContainersClient.class, webTarget);
    }

    public MiscClient miscApi() {
        return WebResourceFactory.newResource(MiscClient.class, webTargetChunked);
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

        /**
         * @deprecated  Use BuildCommand
         */
        public Collection<EventStreamItem> build(File dockerFolder, String tag) throws DockerException, IOException {
            Preconditions.checkNotNull(dockerFolder, "Folder is null");
            Preconditions.checkArgument(dockerFolder.exists(), "Folder %s doesn't exist", dockerFolder);
            Preconditions.checkState(new File(dockerFolder, "Dockerfile").exists(), "Dockerfile doesn't exist in " + dockerFolder);

            return createBuildCommand().dockerFile(new File(dockerFolder, "Dockerfile")).tag(tag).execute().getItems();
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

        public PullCommandBuilder image(Identifier identifier) {
            repo = identifier.repository.name;
            tag  = identifier.tag.orNull();
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
         /*   if( tag == null )
                tag = "";
            if( registry == null )
                registry = "";
           */
            return imagesApi().createImage(fromImage, fromSrc, repo, tag, registry);
        }
    }

    public PullCommandBuilder createPullCommand() {
        return new PullCommandBuilder();
    }

    public BuildCommandBuilder createBuildCommand() { return new BuildCommandBuilder(this); }


    public PushCommandBuilder createPushCommand() {
        return new PushCommandBuilder(this);
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
            containersApi().removeContainer(containerId, includingVolumes, false);
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

        public InputStream log() throws IOException {
            InputStream inputStream = containersApi().attachToContainer(containerId,true,false,false,true,true);
            // Skip the header.
            inputStream.read( new byte[8] );
            return inputStream;
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


        public void remove(Collection<String> images) throws DockerException {
            Preconditions.checkNotNull(images, "List of images can't be null");

            for (String imageName : images) {
                imagesApi().removeImage(imageName, false, false);
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
                imagesApi().removeImage(imageId, false, false);
            }
            catch(NotFoundException ex) {
                log.warn("Remove Image {} not found", imageId);
            }
        }

        public RemoveCommand removeCommand() {
            return new RemoveCommand();
        }

        public class RemoveCommand {
            boolean force = false;
            boolean noprune = false;

            public RemoveCommand force(boolean force) {
                this.force = force;
                return this;
            }

            public RemoveCommand noPrune(boolean noprune) {
                this.noprune = noprune;
                return this;
            }

            public Collection<ImageAction> execute() {
                try {
                    List<Map<String, String>> items = imagesApi().removeImage(imageId, force, noprune);
                    return Collections2.transform(items, new Function<Map<String, String>, ImageAction>() {
                        @Override
                        public ImageAction apply(@Nullable Map<String, String> input) {

                            Map.Entry<String, String> entry = Iterators.get(input.entrySet().iterator(), 0);
                            return new ImageAction( ImageAction.ActionType.valueOf(entry.getKey()), entry.getValue());
                        }
                    });
                }
                catch(NotFoundException ex) {
                    log.warn("Remove Image {} not found", imageId);
                    return Collections.emptySet();
                }
            }
        }

        public ImageInspectResponse inspect() {
            return imagesApi().inspectImage(imageId);
        }

        public void tag(String name, boolean force) {
            imagesApi().tagImage(imageId, name, force);
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
