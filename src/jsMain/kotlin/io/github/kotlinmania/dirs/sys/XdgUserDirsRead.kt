// port-lint: ignore - JS actual for the readAll declared in XdgUserDirs.kt; uses Node fs when available.
package io.github.kotlinmania.dirs.sys

internal actual fun readAll(path: String): ByteArray? {
    val nodeFs = jsRequireFsOrNull() ?: return null
    return try {
        readFileSync(nodeFs, path)
    } catch (_: Throwable) {
        null
    }
}

private fun jsRequireFsOrNull(): dynamic = js(
    "(typeof require === 'function') ? (function(){ try { return require('fs'); } catch (e) { return null; } })() : null"
)

private fun readFileSync(nodeFs: dynamic, path: String): ByteArray? {
    val buffer: dynamic = nodeFs.readFileSync(path)
    val length = (buffer.length as Int)
    val arr = ByteArray(length)
    var i = 0
    while (i < length) {
        arr[i] = (buffer[i] as Int).toByte()
        i++
    }
    return arr
}
