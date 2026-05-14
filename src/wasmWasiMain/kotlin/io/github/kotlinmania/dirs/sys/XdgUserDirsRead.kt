// port-lint: ignore - wasmWasi actual for the readAll declared in XdgUserDirs.kt; XDG dirs are a
// POSIX/Linux concept and the wasi-preview1 sandbox does not expose them.
package io.github.kotlinmania.dirs.sys

internal actual fun readAll(path: String): ByteArray? = null
