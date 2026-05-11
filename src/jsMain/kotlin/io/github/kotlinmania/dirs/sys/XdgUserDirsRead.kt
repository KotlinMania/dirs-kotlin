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

// `require('fs')` as a literal here would be parsed by webpack's static analyzer and pulled
// into the browser bundle, where `fs` is unresolvable — `jsBrowserTest` fails with
// `Module not found: Error: Can't resolve 'fs'`. An `eval('require')` form trips webpack's
// eval-source-map devtool, which wraps eval() calls and mangles the embedded ternary at bundle
// time. `(new Function('return require'))()` is opaque to both the static analyzer AND the
// eval-aware plugins, so the lookup only fires at runtime in environments that actually
// expose `require` (Node). See workspace CLAUDE.md "Hiding require('fs') from webpack".
private fun jsRequireFsOrNull(): dynamic = js(
    "(function(){ try { var rq = (new Function('return typeof require === \"function\" ? require : null'))(); if (!rq) return null; return rq('fs'); } catch (e) { return null; } })()"
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
