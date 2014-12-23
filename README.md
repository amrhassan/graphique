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

### Installation ###
The associated `Makefile` can install the application by executing the `install` target directly:
```
make install
```

### Running From Source ###
To run the service directly from its source code location, execute the following in the root of the source code tree:
```
sbt run
```

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
