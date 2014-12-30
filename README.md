[![Stories in Ready](https://badge.waffle.io/amrhassan/graphique.png?label=ready&title=Ready)](https://waffle.io/amrhassan/graphique)
[![Build Status](https://travis-ci.org/amrhassan/graphique.svg)](https://travis-ci.org/amrhassan/graphique)

# Graphique #

An image management [microservice](http://microservices.io/patterns/microservices.html) built using the awesomeness of Scala, [Akka](http://akka.io/) and [Spray](http://spray.io/).

It is built to handle:
* the low level book keeping of storing the actual image files
* the generation and caching of image variants in arbitrary sizes and formats
* generating publicly-servable URLs for the managed images

Graphique stores and hosts its own images over HTTP by default and it can use a different storage and file serving system like AWS S3, if configured to do so. It exposes its functionality through [RESTful APIs](https://github.com/amrhassan/graphique/wiki/API-Documentation).

The actual image processing heavy lifting is done by the awesome [Thumbnailator](https://code.google.com/p/thumbnailator/).

### Latest Release ###
Download the latest release [from here](https://github.com/amrhassan/graphique/releases/latest).

### Dependencies ###
* `jdk 8`
* `sbt 0.13`

### Installation ###
The associated `Makefile` can install the application by executing the `install` target directly:
```
make install
```

### Running From Source ###
To run the service directly from its source code location, execute the following in the root of the source code tree:
```bash
sbt run
```

### Example Usage ###

**Submitting an image**
```bash
[amr@marvin ~]$ http POST http://localhost:8980/images < an_image.jpg 

HTTP/1.1 201 Created
Content-Length: 0
Date: Wed, 24 Dec 2014 14:12:26 GMT
Location: /image/137a07962e49a58b6161ace95bb1b07d.jpg
Server: spray-can/1.3.2
```

**Creating an image variant**
```bash
[amr@marvin ~]$ http PATCH http://localhost:8980/image/137a07962e49a58b6161ace95bb1b07d.jpg?size-within=120x120

HTTP/1.1 200 OK
Content-Length: 106
Content-Type: application/json; charset=UTF-8
Date: Wed, 24 Dec 2014 14:13:47 GMT
Server: spray-can/1.3.2

{
    "url": "http://localhost:9806/137a07962e49a58b6161ace95bb1b07d-295696c3647869abf69783925c9616d7.jpg"
}
```

### Client Implementations ###
* For the JVM: [graphique-client-java](https://github.com/amrhassan/graphique-client-java)
* For Python: [graphique-client-python](https://github.com/amrhassan/graphique-client-python)

Configuration
=============
Graphique uses the XDG BaseDirectory specification for looking up configuration files named `graphique/application.conf`. The configuration files are written in the [HOCON syntax](https://github.com/typesafehub/config/blob/master/HOCON.md). You can override any of the specified config parameters from the internal [configuration file](/src/main/resources/application.conf).

### Backends ###
Graphique builds its functionality on top primitive operations provided by one of its Backend implementations. 

**The Local Backend**

This backend enables Graphique to store its images on the local filesystem and it 
spins up an internal http server to serve the requested images.

**The AWS S3 Backend**

This backend enables Graphique to store its images on an S3 bucket as public files and delegate the image serving
to S3.
