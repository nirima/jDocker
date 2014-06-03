package com.nirima.docker.api;

import com.nirima.docker.client.model.Event;
import com.nirima.docker.client.model.IdResponse;
import com.nirima.docker.client.model.Info;
import com.nirima.docker.client.model.Version;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

/**
 * @author Nigel Magnay [nigel.magnay@gmail.com]
 */
public interface MiscClient {

/*
2.3 Misc
Build an image from Dockerfile via stdin

POST /build
Build an image from Dockerfile via stdin

Example request:

POST /build HTTP/1.1

{{ STREAM }}
Example response:

HTTP/1.1 200 OK
Content-Type: application/json

{"stream":"Step 1..."}
{"stream":"..."}
{"error":"Error...", "errorDetail":{"code": 123, "message": "Error..."}}
The stream must be a tar archive compressed with one of the following algorithms: identity (no compression), gzip, bzip2, xz.

The archive must include a file called Dockerfile at its root. It may include any number of other files, which will be accessible in the build context (See the ADD build command).

Query Parameters:

t – repository name (and optionally a tag) to be applied to the resulting image in case of success
q – suppress verbose build output
nocache – do not use the cache when building the image
Request Headers:

Content-type – should be set to "application/tar".
X-Registry-Auth – base64-encoded AuthConfig object
Status Codes:
200 – no error
500 – server error
Check auth configuration
*/
    @POST
    @Path("/build")
    @Consumes("application/tar")
    @Produces(MediaType.TEXT_PLAIN)
    InputStream build(@QueryParam("t")String repositoryNameAndTag,
                      @QueryParam("q")boolean supressVerboseOutput,
                      @QueryParam("nocache")boolean suppressCache,
                      InputStream content);

    /*

POST/auth
Get the default username and email

Example request:

POST /auth HTTP/1.1
Content-Type: application/json

{
     "username":"hannibal",
     "password:"xxxx",
     "email":"hannibal@a-team.com",
     "serveraddress":"https://index.docker.io/v1/"
}
Example response:

HTTP/1.1 200 OK
Status Codes:
200 – no error
204 – no error
500 – server error
Display system-wide information
*/
    @POST
    @Path("/auth")
    Void auth();

  /*
GET /info
Display system-wide information

Example request:

GET /info HTTP/1.1
Example response:

HTTP/1.1 200 OK
Content-Type: application/json

{
     "Containers":11,
     "Images":16,
     "Debug":false,
     "NFd": 11,
     "NGoroutines":21,
     "MemoryLimit":true,
     "SwapLimit":false,
     "IPv4Forwarding":true
}
Status Codes:
200 – no error
500 – server error
Show the docker version information
*/
  @GET
  @Path("/info")
  Info info();

/*
GET /version
Show the docker version information

Example request:

GET /version HTTP/1.1
Example response:

HTTP/1.1 200 OK
Content-Type: application/json

{
     "Version":"0.2.2",
     "GitCommit":"5a2a5cc+CHANGES",
     "GoVersion":"go1.0.3"
}
Status Codes:
200 – no error
500 – server error

*/
    @GET
    @Path("/version")
    Version version();

    /*
Create a new image from a container’s changes

POST /commit
Create a new image from a container’s changes

Example request:

POST /commit?container=44c004db4b17&m=message&repo=myrepo HTTP/1.1
Example response:

HTTP/1.1 201 OK
    Content-Type: application/vnd.docker.raw-stream

{"Id":"596069db4bf5"}
Query Parameters:

container – source container
repo – repository
tag – tag
m – commit message
author – author (eg. “John Hannibal Smith <hannibal@a-team.com>”)
run – config automatically applied when the image is run. (ex: {“Cmd”: [“cat”, “/world”], “PortSpecs”:[“22”]})
Status Codes:
201 – no error
404 – no such container
500 – server error
Monitor Docker’s events
      */

    //        params.add("container", commitConfig.getContainer());
//        params.add("repo", commitConfig.getRepo());
//        params.add("tag", commitConfig.getTag());
//        params.add("m", commitConfig.getMessage());
//        params.add("author", commitConfig.getAuthor());
//        params.add("run", commitConfig.getRun());


    @POST
    @Path("/commit")
    IdResponse commit(@QueryParam("container")String container,
                @QueryParam("m")        String commitMessage,
                @QueryParam("repo")     String repository,
                @QueryParam("tag")      String tag,
                @QueryParam("author")   String author,
                @QueryParam("run")      String run );

    /*
GET /events
Get events from docker, either in real time via streaming, or via polling (using since)

Example request:

GET /events?since=1374067924
Example response:

HTTP/1.1 200 OK
Content-Type: application/json

{"status":"create","id":"dfdf82bd3881","from":"base:latest","time":1374067924}
{"status":"start","id":"dfdf82bd3881","from":"base:latest","time":1374067924}
{"status":"stop","id":"dfdf82bd3881","from":"base:latest","time":1374067966}
{"status":"destroy","id":"dfdf82bd3881","from":"base:latest","time":1374067970}
Query Parameters:

since – timestamp used for polling
Status Codes:
200 – no error
500 – server error
Get a tarball containing all images and tags in a repository
*/

    /**
     * Monitor Docker's events
     * @param since timestamp used for polling
     * @param until timestamp used for polling
     * @return stream of events
     */
    @GET
    @Path("/events")
    Response events(@QueryParam("since") Long since, @QueryParam("since") Long until);


    /*
GET /images/(name)/list
Get a tarball containing all images and metadata for the repository specified by name.

Example request

       GET /images/ubuntu/list

   **Example response**:

   .. sourcecode:: http

      HTTP/1.1 200 OK
Content-Type: application/x-tar

Binary data stream
    :statuscode 200: no error
    :statuscode 500: server error
Load a tarball with a set of images and tags into docker

*/

    @GET
    @Path("/images/{name}/list")
    byte[] getTarball(@PathParam("name")String name);

    /*
POST /images/load
Load a set of images and tags into the docker repository.

Example request

    POST /images/load

  Tarball in body

**Example response**:

.. sourcecode:: http

   HTTP/1.1 200 OK

 :statuscode 200: no error
 :statuscode 500: server error

 */
    @POST
    @Path("/images/load")
    Void postTarball(byte[] data);

    /**
     * Ping the docker server.
     * @since 1.11
     */
    @GET
    @Path("/_ping")
    String ping();
}
