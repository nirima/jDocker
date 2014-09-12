package com.nirima.docker.client;

import com.google.common.base.Preconditions;
import com.nirima.docker.jersey.NullReader;
import com.nirima.jersey.filter.Slf4jLoggingFilter;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.RequestEntityProcessing;
import org.glassfish.jersey.filter.LoggingFilter;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.io.Serializable;

/**
 * Base class for web clients.
 */
public abstract class DockerClientBase {
    protected MultivaluedMap<String, Object> headers;
    protected final String serverUrl;
    protected final WebTarget webTarget;
    protected final WebTarget webTargetChunked;

    protected DockerClientBase(String serverUrl, ClientConfig cc, ClientConfig ccChunked)
    {
        this.serverUrl = serverUrl;

        this.webTarget        = ClientBuilder.newClient(cc).target(serverUrl);
        this.webTargetChunked = ClientBuilder.newClient(ccChunked).target(serverUrl);
    }

    public enum Logging {
        NONE, SLF4J, JUL
    }

    public MultivaluedMap<String,Object> getHeaders() {
        if( headers == null ) {
            headers = new MultivaluedHashMap<String, Object>();
            headers.putSingle("X-Registry-Auth","docker");
        }
        return headers;
    }

    public static class Builder<T extends Builder> implements Serializable {

        protected String       serverUrl;
        protected Logging      logging = Logging.NONE;
        protected int connectTimeout = 10000;
        protected int readTimeout = -1;

        public T withUrl(String url) {
            Preconditions.checkNotNull(url);
            this.serverUrl = url;
            return (T)this;
        }

        public T connectTimeout(int ms) {
            connectTimeout = ms;
            return(T) this;
        }

        public T readTimeout(int ms) {
            readTimeout = ms;
            return (T)this;
        }
        public T withLogging(Logging logging) {
            Preconditions.checkNotNull(logging);
            this.logging = logging;
            return (T)this;
        }

        /**
         * Create a basic client config, not setting the processing type.
         * @return a ClientConfig that is mostly configured.
         */
        private ClientConfig createBaseClientConfig() {
            ClientConfig cc  = new ClientConfig();
            // Set some reasonable defaults
            cc.property(ClientProperties.CONNECT_TIMEOUT, connectTimeout);
            if( readTimeout != -1 )
                cc.property(ClientProperties.READ_TIMEOUT,    readTimeout);

            // Docker has an irritating habit of returning no data,
            // but saying the content type is text/plain.

            // MessageBodyReader not found for media type=text/plain; charset=utf-8, type=void, genericType=void
            cc.register(NullReader.class);

            registerLogging(cc);

            return cc;
        }

        protected ClientConfig getClientConfig() {
            ClientConfig cc  = createBaseClientConfig();

            //There is a bug in docker 1.2 where the body of chunked message is ignored on some commands.
            cc.property(ClientProperties.REQUEST_ENTITY_PROCESSING, RequestEntityProcessing.BUFFERED);

            return cc;
        }

        protected ClientConfig getClientConfigChunked() {
            ClientConfig cc  = createBaseClientConfig();

            // Chunked encoding - need this, particularly if we're uploading data otherwise we'll
            // rapidly run out of memory.
            cc.property(ClientProperties.REQUEST_ENTITY_PROCESSING, RequestEntityProcessing.CHUNKED);
            cc.property(ClientProperties.CHUNKED_ENCODING_SIZE, 1024*1024);

            return cc;
        }

        private void registerLogging(ClientConfig cc) {
            if( logging == Logging.JUL ) {
                LoggingFilter lf = new LoggingFilter(java.util.logging.Logger.getLogger(LoggingFilter.class.getName()), true);
                cc.register(lf);
            } else if( logging == Logging.SLF4J ) {
                cc.register(Slf4jLoggingFilter.builder().build() );
            }
        }
    }

}
