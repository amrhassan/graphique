package graphique.backends.localbackend

import java.nio.file.{Paths, Path}

import com.typesafe.config.Config
import graphique.backends._
import graphique.util.MapDbCache

import scala.collection.parallel.mutable

/**
 * A filesystem-backed backend implementation.
 *
 * This implementation stores its image files on the local filesystem, and serves its images itself
 * over HTTP.
 *
 * To create an instance, use the factory method in the LocalBackend object.
 */
class LocalBackend private(rawImages: RawImageManager, images: ImageManager)
  extends Backend(rawImages, images, UrlCache.noCache)

object LocalBackend {

  /**
   * A factory method for a LocalBackend.
   *
   * @param storageLocation the location where the submitted images are stored
   * @param hostname the hostname to use in generated servable image URLs
   * @param httpPort the port on which the internal HTTP server will listen to
   */
  def apply(storageLocation: Path, hostname: String, httpPort: Int): LocalBackend = {
    require(httpPort > 0, "httpPort must be a non negative number")

    val filePaths = new FilePaths(storageLocation)
    val io = new LocalIO

    val requestedImageCache = new RequestedImageCache(io, filePaths)
    val urlProvider = new LocalUrlProvider(hostname, httpPort, filePaths)

    val rawImages = new RawImageManager(filePaths, io)
    val images = new ImageManager(requestedImageCache, urlProvider)

    new LocalBackend(rawImages, images)
  }

  /**
   * A factory method that extracts its parameters from the provide Config.
   */
  def apply(config: Config): LocalBackend = {
    apply(
      storageLocation = Paths.get(config.getString("storageLocation")),
      hostname = config.getString("imageHttpHostname"),
      httpPort = config.getInt("imageHttpPort")
    )
  }
}
