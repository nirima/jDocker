package com.nirima.docker.api;

import com.nirima.docker.client.model.SearchResult;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.io.InputStream;
import java.util.Map;

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
    InputStream getImageLayer(@PathParam("imageId")String imageId);

    /**
     * Put image layer for a given image_id
     * @param is
     */
    @PUT
    @Path("/images/{imageId}/layer")
    void getImageLayer(@PathParam("imageId")String imageId, InputStream is);

    //==================================================================================================================
    // Image
    //==================================================================================================================

    /**
     * Put image for a given image_id.
     */

    @PUT
    @Path("/images/{imageId}/json")
    void putImage(@PathParam("imageId")String imageId);


    //==================================================================================================================
    // Ancestry
    //==================================================================================================================
    @GET
    @Path("/images/{imageId}/ancestry")
    InputStream getImageAncestry(@PathParam("imageId")String imageId);


    //==================================================================================================================
    // Tags
    //==================================================================================================================

    /**
     * Get all of the tags for the given repo.
     */
    @GET
    @Path("/repositories/{namespace}/{repository}/tags")
    Map<String, String> getRepositoryTags(@PathParam("namespace")String namespace, @PathParam("repository")String repository);

    /**
     * Get a tag for the given repo.
     */
    @GET
    @Path("/repositories/{namespace}/{repository}/tags/{tag}")
    String getRepositoryTag(@PathParam("namespace")String namespace, @PathParam("repository")String repository, @PathParam("tag")String tag);

    /**
     * Delete the tag for the repo
     */
    @DELETE
    @Path("/repositories/{namespace}/{repository}/tags/{tag}")
    String deleteRepositoryTag(@PathParam("namespace")String namespace, @PathParam("repository")String repository, @PathParam("tag")String tag);

    /**
     * Put a tag for the given repo.
     */
    @PUT
    @Path("/repositories/{namespace}/{repository}/tags/{tag}")
    String putRepositoryTag(@PathParam("namespace")String namespace, @PathParam("repository")String repository, @PathParam("tag")String tag);
    //==================================================================================================================
    // Repositories
    //==================================================================================================================

    /**
     * Delete a repository
     */
    @DELETE
    @Path("/repositories/{namespace}/{repository}")
    String deleteRepository(@PathParam("namespace")String namespace, @PathParam("repository")String repository);

    //==================================================================================================================
    // Status
    //==================================================================================================================
    @GET
    @Path("/_ping")
    void ping();

    //==================================================================================================================
    // Authorization
    //==================================================================================================================


    //==================================================================================================================
    // Search
    //==================================================================================================================
    @GET
    @Path("/search")
    SearchResult search(@QueryParam("q")String query);






}
