[![Stories in Ready](https://badge.waffle.io/amrhassan/graphique.png?label=ready&title=Ready)](https://waffle.io/amrhassan/graphique)
[![Build Status](https://travis-ci.org/amrhassan/graphique.svg)](https://travis-ci.org/amrhassan/graphique)

# Graphique #

An image management [microservice](http://microservices.io/patterns/microservices.html) written using the awesomeness of Scala, [Akka](http://akka.io/), [Spray](http://spray.io/) and [Thumbnailator](https://code.google.com/p/thumbnailator/).

It is built to handle:
* the low level book keeping of storing the actual image files
* the generation and caching of image thumbnails in arbitrary sizes and formats
* generating URLs for the managed images that are servable over HTTP

It exposes its functionality through a RESTful APIs.

Dependencies
============
* `sbt (=> 0.13.7)`

Runnint It
===========
1. `cd` into the directory where the the project is
2. `sbt run`
