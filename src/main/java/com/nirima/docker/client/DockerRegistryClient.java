package com.nirima.docker.client;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterators;
import com.nirima.docker.api.ContainersClient;
import com.nirima.docker.api.ImagesClient;
import com.nirima.docker.api.MiscClient;
import com.nirima.docker.api.RegistryClient;
import com.nirima.docker.client.command.BuildCommandBuilder;
import com.nirima.docker.client.command.PushCommandBuilder;
import com.nirima.docker.client.model.ContainerConfig;
import com.nirima.docker.client.model.ContainerCreateResponse;
import com.nirima.docker.client.model.ContainerInspectResponse;
import com.nirima.docker.client.model.EventStreamItem;
import com.nirima.docker.client.model.FileChanges;
import com.nirima.docker.client.model.HostConfig;
import com.nirima.docker.client.model.ImageAction;
import com.nirima.docker.client.model.ImageInspectResponse;
import com.nirima.docker.client.model.Info;
import com.nirima.docker.client.model.Version;
import com.nirima.docker.jersey.NullReader;
import com.nirima.jersey.filter.Slf4jLoggingFilter;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.RequestEntityProcessing;
import org.glassfish.jersey.client.proxy.WebResourceFactory;
import org.glassfish.jersey.filter.LoggingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.util.logging.resources.logging;

import javax.annotation.Nullable;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Top-level docker registry client interface.
 */
public class DockerRegistryClient extends DockerClientBase  implements Serializable {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends DockerClientBase.Builder<Builder> {

        public Builder fromClient (DockerRegistryClient client) {
            this.serverUrl = client.serverUrl;
            connectTimeout = (Integer)client.webTarget.getConfiguration().getProperty(ClientProperties.CONNECT_TIMEOUT);
            readTimeout = (Integer)client.webTarget.getConfiguration().getProperty(ClientProperties.CONNECT_TIMEOUT);
            return this;
        }

        public DockerRegistryClient build() {
            Preconditions.checkNotNull(serverUrl);
            return new DockerRegistryClient(serverUrl, getClientConfig());
        }

        public Builder withUrl(URL url) {
            this.serverUrl = "http://" + url.getHost() + ":" + url.getPort();
            return this;
        }
    }

    private DockerRegistryClient(String serverUrl, ClientConfig cc)
    {
        super(serverUrl, cc);

    }

    public RegistryClient registryApi() {
        return WebResourceFactory.newResource(RegistryClient.class, webTarget);
    }

}
