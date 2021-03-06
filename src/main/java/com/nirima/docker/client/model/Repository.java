package com.nirima.docker.client.model;

import com.google.common.base.Objects;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * A repository or image name.
 */
public class Repository {
    public final String name;

    /**
     * Name may be eg. 'busybox' or '10.0.0.1:5000/fred'
     * @param name Repository name
     */
    public Repository(String name) {
        this.name = name;
    }

    /**
     * Return the URL portion (repository).
     * Note that this might not actually BE a repository location.
     * @return
     * @throws MalformedURLException
     */
    public URL getURL() throws MalformedURLException {
        return new URL("http://" + name);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("name", name)
                .toString();
    }


    public String getPath() {
        if( !name.contains("/") )
            return name;

        return name.substring(name.indexOf("/") + 1 );
    }
}
