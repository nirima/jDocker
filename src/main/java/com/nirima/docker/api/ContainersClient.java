package com.nirima.docker.api;

import com.nirima.docker.client.model.*;

import javax.annotation.Nullable;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.io.InputStream;
import java.util.List;

/**
 * Created by magnayn on 02/02/2014.
 */
@Path("/containers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ContainersClient {

    /**
     * List containers
     *
     * @param all    1/True/true or 0/False/false, Show all containers. Only running containers are shown by default
     * @param limit  Show limit last created containers, include non-running ones.
     * @param since  Show only containers created since Id, include non-running ones.
     * @param before Show only containers created before Id, include non-running ones.
     * @param size    1/True/true or 0/False/false, Show the containers sizes
     * @return
     */
    @GET
    @Path("/json")
    List<Container> listContainers(@QueryParam("all")boolean all,
                              @QueryParam("limit")int limit,
                              @DefaultValue("") @QueryParam("since")String since,
                              @DefaultValue("") @QueryParam("before")String before,
                              @QueryParam("size")boolean size);


    //--------------
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/create")
    ContainerCreateResponse createContainer(@QueryParam("name")String name,
                                            ContainerConfig config);

/*



GET/containers/(id)/json
Return low-level information on the container id

Example request:

GET /containers/4fa6e0f0c678/json HTTP/1.1
Example response:

HTTP/1.1 200 OK
Content-Type: application/json

{
             "Id": "4fa6e0f0c6786287e131c3852c58a2e01cc697a68231826813597e4994f1d6e2",
             "Created": "2013-05-07T14:51:42.041847+02:00",
             "Path": "date",
             "Args": [],
             "Config": {
                     "Hostname": "4fa6e0f0c678",
                     "User": "",
                     "Memory": 0,
                     "MemorySwap": 0,
                     "AttachStdin": false,
                     "AttachStdout": true,
                     "AttachStderr": true,
                     "PortSpecs": null,
                     "Tty": false,
                     "OpenStdin": false,
                     "StdinOnce": false,
                     "Env": null,
                     "Cmd": [
                             "date"
                     ],
                     "Dns": null,
                     "Image": "base",
                     "Volumes": {},
                     "VolumesFrom": "",
                     "WorkingDir":""

             },
             "State": {
                     "Running": false,
                     "Pid": 0,
                     "ExitCode": 0,
                     "StartedAt": "2013-05-07T14:51:42.087658+02:01360",
                     "Ghost": false
             },
             "Image": "b750fe79269d2ec9a3c593ef05b4332b1d1a02a62b4accb2c21d589ff2f5f2dc",
             "NetworkSettings": {
                     "IpAddress": "",
                     "IpPrefixLen": 0,
                     "Gateway": "",
                     "Bridge": "",
                     "PortMapping": null
             },
             "SysInitPath": "/home/kitty/go/src/github.com/dotcloud/docker/bin/docker",
             "ResolvConfPath": "/etc/resolv.conf",
             "Volumes": {},
             "HostConfig": {
                 "Binds": null,
                 "ContainerIDFile": "",
                 "LxcConf": [],
                 "Privileged": false,
                 "PortBindings": {
                    "80/tcp": [
                        {
                            "HostIp": "0.0.0.0",
                            "HostPort": "49153"
                        }
                    ]
                 },
                 "Links": null,
                 "PublishAllPorts": false
             }
}
Status Codes:
200 – no error
404 – no such container
500 – server error
*/

    //--------------

    /**
     * Inspect a container
     *
     * @param id - id of the container
     * @return
     */
    @GET
    @Path("/{id}/json")
    ContainerInspectResponse inspectContainer(@PathParam("id")String id);


/*
List processes running inside a container

GET /containers/(id)/top
List processes running inside the container id

Example request:

GET /containers/4fa6e0f0c678/top HTTP/1.1
Example response:

HTTP/1.1 200 OK
Content-Type: application/json

{
     "Titles":[
             "USER",
             "PID",
             "%CPU",
             "%MEM",
             "VSZ",
             "RSS",
             "TTY",
             "STAT",
             "START",
             "TIME",
             "COMMAND"
             ],
     "Processes":[
             ["root","20147","0.0","0.1","18060","1864","pts/4","S","10:06","0:00","bash"],
             ["root","20271","0.0","0.0","4312","352","pts/4","S+","10:07","0:00","sleep","10"]
     ]
}
Query Parameters:

ps_args – ps arguments to use (eg. aux)
Status Codes:
200 – no error
404 – no such container
500 – server error
*/

    //--------------
    @GET
    @Path("/{id}/top")
    TopResponse top(@PathParam("id")String containerId);

/*
Inspect changes on a container’s filesystem

GET /containers/(id)/changes
Inspect changes on container id ‘s filesystem

Example request:

GET /containers/4fa6e0f0c678/changes HTTP/1.1
Example response:

HTTP/1.1 200 OK
Content-Type: application/json

[
     {
             "Path":"/dev",
             "Kind":0
     },
     {
             "Path":"/dev/kmsg",
             "Kind":1
     },
     {
             "Path":"/test",
             "Kind":1
     }
]
Status Codes:
200 – no error
404 – no such container
500 – server error
*/

    //--------------
    @GET
    @Path("/{id}/changes")
    List<FileChanges> getFilesystemChanges(@PathParam("id")String containerId);

/*
Export a container

GET /containers/(id)/export
Export the contents of container id

Example request:

GET /containers/4fa6e0f0c678/export HTTP/1.1
Example response:

HTTP/1.1 200 OK
Content-Type: application/octet-stream

{{ STREAM }}
Status Codes:
200 – no error
404 – no such container
500 – server error
*/

    //--------------
    @GET
    @Path("/{id}/export")
    Object exportContainer(@PathParam("id")String containerId);


    //--------------

    /**
     * Start a container
     *
     * @param id - the container ID
     * @param hostConfig - the container’s host configuration (optional)
     * @return - a null string - this may change to void in the future.
     */
    @POST
    @Path("/{id}/start")
    @Produces(MediaType.TEXT_PLAIN)
    String startContainer(@PathParam("id") String id,
                          HostConfig hostConfig);

/*
Stop a container

POST /containers/(id)/stop
Stop the container id

Example request:

POST /containers/e90e34656806/stop?t=5 HTTP/1.1
Example response:

HTTP/1.1 204 OK
Query Parameters:

t – number of seconds to wait before killing the container
Status Codes:
204 – no error
404 – no such container
500 – server error
*/

    //--------------


    @POST
    @Path("/{id}/stop")
    @Produces(MediaType.TEXT_PLAIN)
    String stopContainer(@PathParam("id")String id,
                       @QueryParam("t")Long secondsToWait);



/*
Restart a container

POST /containers/(id)/restart
Restart the container id

Example request:

POST /containers/e90e34656806/restart?t=5 HTTP/1.1
Example response:

HTTP/1.1 204 OK
Query Parameters:

t – number of seconds to wait before killing the container
Status Codes:
204 – no error
404 – no such container
500 – server error
*/

    //--------------



    @POST
    @Path("/{id}/restart")
    @Produces(MediaType.TEXT_PLAIN)
    String restartContainer(@PathParam("id")String id,
                          @QueryParam("t")Long secondsToWait);

/*
Kill a container

POST /containers/(id)/kill
Kill the container id

Example request:

POST /containers/e90e34656806/kill HTTP/1.1
Example response:

HTTP/1.1 204 OK
Status Codes:
204 – no error
404 – no such container
500 – server error
*/

    //--------------

    /**
     * Kill a container
     * @param id
     * @return an empty string (may change to void)
     */
    @POST
    @Path("/{id}/kill")
    @Produces(MediaType.TEXT_PLAIN)
    String killContainer(@PathParam("id")String id);

/*
Attach to a container

POST /containers/(id)/attach
Attach to the container id

Example request:

POST /containers/16253994b7c4/attach?logs=1&stream=0&stdout=1 HTTP/1.1
Example response:

HTTP/1.1 200 OK
Content-Type: application/vnd.docker.raw-stream

{{ STREAM }}
Query Parameters:

logs – 1/True/true or 0/False/false, return logs. Default false
stream – 1/True/true or 0/False/false, return stream. Default false
stdin – 1/True/true or 0/False/false, if stream=true, attach to stdin. Default false
stdout – 1/True/true or 0/False/false, if logs=true, return stdout log, if stream=true, attach to stdout. Default false
stderr – 1/True/true or 0/False/false, if logs=true, return stderr log, if stream=true, attach to stderr. Default false
Status Codes:
200 – no error
400 – bad parameter
404 – no such container
500 – server error
Stream details:

When using the TTY setting is enabled in POST /containers/create, the stream is the raw data from the process PTY and client’s stdin. When the TTY is disabled, then the stream is multiplexed to separate stdout and stderr.

The format is a Header and a Payload (frame).

HEADER

The header will contain the information on which stream write the stream (stdout or stderr). It also contain the size of the associated frame encoded on the last 4 bytes (uint32).

It is encoded on the first 8 bytes like this:

header := [8]byte{STREAM_TYPE, 0, 0, 0, SIZE1, SIZE2, SIZE3, SIZE4}
STREAM_TYPE can be:

0: stdin (will be writen on stdout)
1: stdout
2: stderr
SIZE1, SIZE2, SIZE3, SIZE4 are the 4 bytes of the uint32 size encoded as big endian.

PAYLOAD

The payload is the raw stream.

IMPLEMENTATION

The simplest way to implement the Attach protocol is the following:

Read 8 bytes
chose stdout or stderr depending on the first byte
Extract the frame size from the last 4 byets
Read the extracted size and output it on the correct output
Goto 1)
*/

    //--------------
    @POST
    @Path("/{id}/attach")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    InputStream attachToContainer(@PathParam("id")String id,
                           @QueryParam("logs")boolean returnLogs,
                           @QueryParam("stream")boolean returnStream,
                           @QueryParam("stdin")boolean attachStdin,
                           @QueryParam("stdout")boolean attachStdout,
                           @QueryParam("stderr")boolean attachStderr
                          );

/*
Wait a container

POST /containers/(id)/wait
Block until container id stops, then returns the exit code

Example request:

POST /containers/16253994b7c4/wait HTTP/1.1
Example response:

HTTP/1.1 200 OK
Content-Type: application/json

{"StatusCode":0}
Status Codes:
200 – no error
404 – no such container
500 – server error
*/

    //--------------
    @POST
    @Path("/{id}/wait")
    StatusCodeResponse waitForContainer(@PathParam("id")String id);

/*
Remove a container

DELETE/containers/(id)
Remove the container id from the filesystem

Example request:

DELETE /containers/16253994b7c4?v=1 HTTP/1.1
Example response:

HTTP/1.1 204 OK
Query Parameters:

v – 1/True/true or 0/False/false, Remove the volumes associated to the container. Default false
Status Codes:
204 – no error
400 – bad parameter
404 – no such container
500 – server error
*/

    //--------------
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    void removeContainer(@PathParam("id")String id);

    /**
     * Remove the container ID from the filesystem
     * @param id
     * @param removeVolumes Remove the volumes associated to the container
     * @param force Removes the container
    even if it was running.
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    void removeContainer(@PathParam("id")String id,
                         @QueryParam("v")boolean removeVolumes,
                         @QueryParam("force")boolean force);

/*
Copy files or folders from a container

POST /containers/(id)/copy
Copy files or folders of container id

Example request:

POST /containers/4fa6e0f0c678/copy HTTP/1.1
Content-Type: application/json

{
     "Resource":"test.txt"
}
Example response:

HTTP/1.1 200 OK
Content-Type: application/octet-stream

{{ STREAM }}
Status Codes:
200 – no error
404 – no such container
500 – server error
     */
    @POST
    @Path("/{id}/copy")
    void copy(@PathParam("id")String id);

    /**
     * Get stdout and stderr logs from the container ID
     */
    @GET
    @Path("/{id}/logs")
    InputStream logs(@PathParam("id")String id,
                @QueryParam("follow")boolean follow,
                @QueryParam("stdout")boolean stdout,
                @QueryParam("stderr")boolean stderr,
                @QueryParam("timestamps")boolean timestamps
                );
}
