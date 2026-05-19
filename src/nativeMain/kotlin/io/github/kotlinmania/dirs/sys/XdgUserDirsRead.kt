// port-lint: ignore - native filesystem actual for the readAll declared in XdgUserDirs.kt
package io.github.kotlinmania.dirs.sys

import kotlinx.cinterop.ExperimentalForeignApi
import platform.posix.EOF
import platform.posix.fclose
import platform.posix.ferror
import platform.posix.fgetc
import platform.posix.fopen

@OptIn(ExperimentalForeignApi::class)
internal actual fun readAll(path: String): ByteArray? {
    val file = fopen(path, "rb") ?: return null
    try {
        val collected = ArrayList<Byte>()
        while (true) {
            val value = fgetc(file)
            if (value == EOF) {
                return if (ferror(file) == 0) {
                    ByteArray(collected.size) { index -> collected[index] }
                } else {
                    null
                }
            }
            collected += value.toByte()
        }
    } finally {
        fclose(file)
    }
}
