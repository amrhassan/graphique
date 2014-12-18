package graphique.backends.localbackend

import java.nio.file
import java.nio.file.Path
import graphique.backends.Paths

/**
 * The single authoritative point for where files should be stored, in the local backend.
 *
 * @param storageLocation the root path of all files
 */
private[localbackend] class FilePaths(storageLocation: Path) extends Paths(storageLocation)