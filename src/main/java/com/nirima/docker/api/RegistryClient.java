package com.nirima.docker.api;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import java.io.InputStream;

/**
 * Created by magnayn on 04/06/2014.
 */
@Path("/v1")
public interface RegistryClient {

    //==================================================================================================================
    // Images
    //==================================================================================================================

    /**
     * Get image layer for a given image_id
     */
    @GET
    @Path("/images/{imageId}/layer")
    InputStream getImageLayer();

    /**
     * Put image layer for a given image_id
     * @param is
     */
    @PUT
    @Path("/images/{imageId}/layer")
    void getImageLayer(InputStream is);

    //==================================================================================================================
    // Image
    //==================================================================================================================

    /**
     * Put image for a given image_id.
     */

    @PUT
    @Path("/images/{imageId}/json")
    void putImage();


    //==================================================================================================================
    // Ancestry
    //==================================================================================================================
    @GET
    @Path("/images/{imageId}/ancestry")
    InputStream getImageAncestry(String imageId);


    //==================================================================================================================
    // Tags
    //==================================================================================================================

    /**
     * Get all of the tags for the given repo.
     */
    @GET
    @Path("/repositories/{namespace}/{repository}/tags")
    InputStream getRepositoryTags();

    /**
     * Get a tag for the given repo.
     */
    @GET
    @Path("/repositories/{namespace}/{repository}/tags/{tag}")
    InputStream getRepositoryTag();

    /**
     * Delete the tag for the repo
     */
    @DELETE
    @Path("/repositories/{namespace}/{repository}/tags/{tag}")
    InputStream deleteRepositoryTag();

    /**
     * Put a tag for the given repo.
     */
    @PUT
    @Path("/repositories/{namespace}/{repository}/tags/{tag}")
    InputStream putRepositoryTag();
    //==================================================================================================================
    // Repositories
    //==================================================================================================================

    /**
     * Delete a repository
     */
    @DELETE
    @Path("/repositories/{namespace}/{repository}")
    void deleteRepository();

    //==================================================================================================================
    // Status
    //==================================================================================================================
    @GET
    @Path("/_ping")
    void ping();

    //==================================================================================================================
    // Authorization
    //==================================================================================================================






}
