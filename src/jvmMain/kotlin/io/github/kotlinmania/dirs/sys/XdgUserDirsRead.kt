// port-lint: ignore - JVM filesystem actual for the readAll declared in XdgUserDirs.kt
package io.github.kotlinmania.dirs.sys

internal actual fun readAll(path: String): ByteArray? =
    try {
        java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(path))
    } catch (_: Throwable) {
        null
    }
