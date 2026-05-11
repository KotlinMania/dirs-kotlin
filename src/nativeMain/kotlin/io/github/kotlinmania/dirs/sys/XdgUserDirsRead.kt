// port-lint: ignore - native filesystem actual for the readAll declared in XdgUserDirs.kt
package io.github.kotlinmania.dirs.sys

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.posix.feof
import platform.posix.fclose
import platform.posix.ferror
import platform.posix.fopen
import platform.posix.fread

@OptIn(ExperimentalForeignApi::class)
internal actual fun readAll(path: String): ByteArray? {
    val file = fopen(path, "rb") ?: return null
    try {
        val chunkSize = 4096
        val chunk = ByteArray(chunkSize)
        var collected = ByteArray(0)
        var done = false
        var failed = false
        while (!done) {
            val read = chunk.usePinned { pinned ->
                fread(pinned.addressOf(0), 1.toULong(), chunkSize.toULong(), file)
            }
            val readInt = read.toInt()
            if (readInt > 0) {
                val grown = ByteArray(collected.size + readInt)
                collected.copyInto(grown, 0)
                chunk.copyInto(grown, collected.size, 0, readInt)
                collected = grown
            }
            if (readInt < chunkSize) {
                if (ferror(file) != 0) {
                    failed = true
                }
                done = true
            }
        }
        return if (failed) null else collected
    } finally {
        fclose(file)
    }
}
