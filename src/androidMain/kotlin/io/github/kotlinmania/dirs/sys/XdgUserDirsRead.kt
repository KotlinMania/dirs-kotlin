// port-lint: ignore - Android actual for the readAll declared in XdgUserDirs.kt; the workspace
// forbids java.* imports, so this corresponds to upstream's "io::Error -> None" branch.
package io.github.kotlinmania.dirs.sys

internal actual fun readAll(path: String): ByteArray? = null
