package com.nirima.docker.api;

import com.nirima.docker.client.model.Image;
import com.nirima.docker.client.model.ImageInspectResponse;
import com.nirima.docker.client.model.SearchItem;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by magnayn on 02/02/2014.
 */
@Path("/images")
public interface ImagesClient {
    /**
     * List Images
     */

    @GET
    @Path("/json")
    List<Image> listImages(@DefaultValue("") @QueryParam("filter") String filter,
                           @QueryParam("all")boolean all);

    /*

Create an image

POST/images/create
Create an image, either by pull it from the registry or by importing it

Example request:

POST /images/create?image=base HTTP/1.1
Example response:

HTTP/1.1 200 OK
Content-Type: application/json

{"status":"Pulling..."}
{"status":"Pulling", "progress":"1 B/ 100 B", "progressDetail":{"current":1, "total":100}}
{"error":"Invalid..."}
...
When using this endpoint to pull an image from the registry, the X-Registry-Auth header can be used to include a base64-encoded AuthConfig object.

Query Parameters:

image – name of the image to pull
fromSrc – source to import, - means stdin
repo – repository
tag – tag
registry – the registry to pull from
Request Headers:

X-Registry-Auth – base64-encoded AuthConfig object
Status Codes:
200 – no error
500 – server error
*/

    //------------------------

    @POST
    @Path("/create")
    InputStream createImage(@QueryParam("fromImage") String fromImage,
                     @QueryParam("fromSrc")   String fromSrc,
                     @QueryParam("repo")      String repo,
                     @QueryParam("tag")       String tag,
                     @QueryParam("registry")  String registry
                     );

    // TEST...
    @POST
    @Path("/create")
    InputStream createImage(@QueryParam("image") String fromImage,
                            @QueryParam("fromSrc")   String fromSrc,
                            @QueryParam("repo")      String repo,
                            @QueryParam("tag")       String tag
    );

    /*
Insert a file in an image

POST /images/(name)/insert
Insert a file from url in the image name at path

Example request:

POST /images/test/insert?path=/usr&url=myurl HTTP/1.1
Example response:

HTTP/1.1 200 OK
Content-Type: application/json

{"status":"Inserting..."}
{"status":"Inserting", "progress":"1/? (n/a)", "progressDetail":{"current":1}}
{"error":"Invalid..."}
...
Status Codes:
200 – no error
500 – server error
*/

    //------------------------
    @POST
    @Path("/{name}/insert")
    void insertFile(@PathParam("name")String name);

    /*
Inspect an image

GET /images/(name)/json
Return low-level information on the image name

Example request:

GET /images/base/json HTTP/1.1
Example response:

HTTP/1.1 200 OK
Content-Type: application/json

{
     "id":"b750fe79269d2ec9a3c593ef05b4332b1d1a02a62b4accb2c21d589ff2f5f2dc",
     "parent":"27cf784147099545",
     "created":"2013-03-23T22:24:18.818426-07:00",
     "container":"3d67245a8d72ecf13f33dffac9f79dcdf70f75acb84d308770391510e0c23ad0",
     "container_config":
             {
                     "Hostname":"",
                     "User":"",
                     "Memory":0,
                     "MemorySwap":0,
                     "AttachStdin":false,
                     "AttachStdout":false,
                     "AttachStderr":false,
                     "PortSpecs":null,
                     "Tty":true,
                     "OpenStdin":true,
                     "StdinOnce":false,
                     "Env":null,
                     "Cmd": ["/bin/bash"]
                     ,"Dns":null,
                     "Image":"base",
                     "Volumes":null,
                     "VolumesFrom":"",
                     "WorkingDir":""
             },
     "Size": 6824592
}
Status Codes:
200 – no error
404 – no such image
500 – server error
*/

    //------------------------
    @GET
    @Path("/{name}/json")
    ImageInspectResponse inspectImage(@PathParam("name")String name);

    /*
Get the history of an image

GET /images/(name)/history
Return the history of the image name

Example request:

GET /images/base/history HTTP/1.1
Example response:

HTTP/1.1 200 OK
Content-Type: application/json

[
     {
             "Id":"b750fe79269d",
             "Created":1364102658,
             "CreatedBy":"/bin/bash"
     },
     {
             "Id":"27cf78414709",
             "Created":1364068391,
             "CreatedBy":""
     }
]
Status Codes:
200 – no error
404 – no such image
500 – server error
*/

    //------------------------
    @GET
    @Path("/{name}/history")
    List<Image> getImageHistory(@PathParam("name")String name);

    /*
Push an image on the registry

POST /images/(name)/push
Push the image name on the registry

Example request:

POST /images/test/push HTTP/1.1
Example response:

HTTP/1.1 200 OK
Content-Type: application/json

{"status":"Pushing..."}
{"status":"Pushing", "progress":"1/? (n/a)", "progressDetail":{"current":1}}}
{"error":"Invalid..."}
...
Query Parameters:

registry – the registry you wan to push, optional
Request Headers:

X-Registry-Auth – include a base64-encoded AuthConfig object.
Status Codes:
200 – no error
404 – no such image
500 – server error

*/

    //------------------------
    @POST
    @Path("/{name}/push")
    @Consumes("text/plain")
    InputStream pushImageOnRegistry(@PathParam("name")     String name,
                                     @QueryParam("registry")String registry
                                     );


    //------------------------

    /**
     * Tag an image into a repository
     * @param name
     * @param repository – The repository to tag in
     * @param force
     */
    @POST
    @Path("/{name}/tag")
    void tagImage(@PathParam("name")   String name,
                  @QueryParam("repo")  String repository,
                  @QueryParam("force") boolean force);

    /*
Remove an image

DELETE /images/(name)
Remove the image name from the filesystem

Example request:

DELETE /images/test HTTP/1.1
Example response:

HTTP/1.1 200 OK
Content-type: application/json

[
 {"Untagged":"3e2f21a89f"},
 {"Deleted":"3e2f21a89f"},
 {"Deleted":"53b4f83ac9"}
]
Status Codes:
200 – no error
404 – no such image
409 – conflict
500 – server error
*/

    //------------------------

    /**
     * Remove an image
     * @param name
     * @return
     */
    @DELETE
    @Path("{name}")
    @Produces(MediaType.TEXT_PLAIN)
    List<Map<String, String>> removeImage(@PathParam("name")String name,
                       @QueryParam("force") boolean force,
                       @QueryParam("noprune") boolean noprune);



    /**
     * Search images
     * @param term - term to search
     * @return
     */
    @GET
    @Path("/search")
    List<SearchItem> searchForImage(@QueryParam("term")String term);

    /**
     * Build an image from Dockerfile via stdin
     * @param inputStream a tar archive.
     */
    @POST
    @Path("/build")
    @Consumes("application/tar")
    InputStream buildImageFromStream(InputStream inputStream,
                                     @QueryParam("t") String repositoryName,
                                     @QueryParam("q") boolean quiet,
                                     @QueryParam("nocache") boolean nocache);
}
