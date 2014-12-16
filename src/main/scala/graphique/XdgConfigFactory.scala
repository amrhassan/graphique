package graphique

import java.nio.file.{Path, Paths}

import com.typesafe.config.{Config, ConfigFactory}


object XdgConfigFactory {

  private val ApplicationName = "graphique"

  private def applicationLevelConfig: Config = ConfigFactory.load()

  private def userLevelConfig: Config = {

    def xdgConfigHomeFallback = Paths.get(System.getProperties.getProperty("user.home")) resolve ".config"
    val xdgConfigHome = Paths.get(Option(System.getenv("XDG_CONFIG_HOME")).getOrElse(xdgConfigHomeFallback.toString))

    ConfigFactory.parseFile((xdgConfigHome resolve s"$ApplicationName/application.conf").toFile)
  }

  private def systemLevelConfig: Config = {

    val xdgConfigDirs = Option(System.getenv("XDG_CONFIG_DIRS")).getOrElse("/etc/xdg")

    def mergeFrom(paths: List[Path], configSoFar: Config): Config =
      if (paths.isEmpty)
        configSoFar
      else {
        val headConfigPath = paths.head resolve s"$ApplicationName/application.conf"
        mergeFrom(paths.tail, ConfigFactory.parseFile(headConfigPath.toFile) withFallback configSoFar)
      }

    mergeFrom(xdgConfigDirs.split(":").toList map (Paths.get(_)), ConfigFactory.empty())
  }

  /**
   * Loads configuration parameters from `application.conf` files in directories and order described
   * by the XDG BaseDirectory specification, merged with the configuration parameters acquired via
   * the regular `com.typesafe.config.ConfigFactory` as a fallback.
   */
  def load(): Config = (userLevelConfig withFallback systemLevelConfig) withFallback applicationLevelConfig
}