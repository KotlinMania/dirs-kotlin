// port-lint: ignore - Wasm-JS actual for the readAll declared in XdgUserDirs.kt; the Wasm-JS target
// has no portable filesystem and corresponds to upstream's "io::Error -> None" branch.
package io.github.kotlinmania.dirs.sys

internal actual fun readAll(path: String): ByteArray? = null
