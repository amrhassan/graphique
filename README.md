[![Stories in Ready](https://badge.waffle.io/amrhassan/graphique.png?label=ready&title=Ready)](https://waffle.io/amrhassan/graphique)
[![Build Status](https://travis-ci.org/amrhassan/graphique.svg)](https://travis-ci.org/amrhassan/graphique)

# Graphique #

An image management [microservice](http://microservices.io/patterns/microservices.html) written using the awesomeness of Scala, [Akka](http://akka.io/), [Spray](http://spray.io/) and [Thumbnailator](https://code.google.com/p/thumbnailator/).

It is built to handle:
* the low level book keeping of storing the actual image files
* the generation and caching of image variants in arbitrary sizes and formats
* generating publicly-servable URLs for the managed images

It exposes its functionality through [RESTful APIs](https://github.com/amrhassan/graphique/wiki/API-Documentation).

Dependencies
============
* `sbt (=> 0.13.7)`

Running
=======
1. `cd` into the directory where the the project is
2. `sbt run`

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
