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

Running
=======
1. `cd` into the directory where the the project is
2. `sbt run`

API Documentation
=================

### Image Submission ###
| `POST` | `/images/`  |
| ------ | :---------- |

Submits a - possibly new - image to the server and retrieves the identifying image tag for this
submitted image.

**Parameters**
The request body as the image file content.

**Responses**

* `201 CREATED`
  - Response header `Location: /image/{image-tag}` contains the just created image tag and URL

* `400 Bad Request`
  - The submitted content is not a valid image

### Image URL retrieval ###
| `GET`  | `/image/{image-tag}`  |
| ------ | :-------------------- |

Retrieves the publicly-serveable image URL for the specified `image-tag`. This call does not make sure
that this particular image variant exists so it's up to the caller to make sure that it only uses it with
image variants that she knows it exists for sure.

**Parameters**
* `size-within` QUERY parameter:
    - Optional
    - Specifies that this image variant should fit within this specified size
    - Format: `WIDTHxHEIGHT` where `WIDTH` and `HEIGHT` are integer pixel counts
* `format` QUERY parameter:
    - Optional
    - Specifies the format of this image variant
    - Possible values:
        * `png`
        * `jpeg`
        * `jpeg(quality)` where `quality` is a real number

**Responses**

* `200 OK`
  - Response body is an `application/json` object and it has the following schema:
```
{
  "url": "XXXXXXXXXXXXXXXXXXXXXXXXX"
}
```

### Image Variant Creation ###

| `PATCH`  | `/image/{image-tag}`  |
| -------  | :-------------------- |

Retrieves the publicly-serveable image URL for the specified `image-tag`. This call makes sure that this
image variant exists before returning its representation.

**Parameters**
* `size-within` QUERY parameter:
    - Optional
    - Specifies that this image variant should fit within this specified size
    - Format: `WIDTHxHEIGHT` where `WIDTH` and `HEIGHT` are integer pixel counts
* `format` QUERY parameter:
    - Optional
    - Specifies the format of this image variant
    - Possible values:
        * `png`
        * `jpeg`
        * `jpeg(quality)` where `quality` is a real number

**Responses**

* `200 OK`
  - Response body is an `application/json` object and it has the following schema:
```
{
  "url": "XXXXXXXXXXXXXXXXXXXXXXXXX"
}
```

* `404 NOT FOUND`
  - The requested image is not available

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
