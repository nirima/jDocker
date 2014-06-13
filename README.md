# jDocker

Fluent Java API client for [Docker](http://docs.docker.io/ "Docker")

Supports Docker Client API v1.8+, Docker Server version 0.8+

## Build with Maven

This is a fork from docker-java (https://github.com/kpelykh/docker-java), but has been adjusted fairly significantly.

If you do not care for some of the dependencies that this version uses, then you may find docker-java to be preferable.

*This is a Work In Progress.*

Changes are :

- use of Jersey / (fasterxml) Jackson, JAX-RS for accesing rest services.
- direct dependency on Guava
- significantly different wrapped API ("Fluent" style)


###### Prerequisites:

* Java 1.7+
* Maven 3.0.5
* Docker daemon running


By default maven will run tests during build process. Tests are using localhost instance of Docker, make sure that
you have Docker running, or the tests.

*Since version 0.6, Docker is using unix socket for communication, however java client works over TCP/IP, so you need to
make sure that your Docker server is listening on TCP/IP port.*



