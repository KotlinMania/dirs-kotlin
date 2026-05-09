// port-lint: source dirs-sys/src/xdg_user_dirs.rs
package io.github.kotlinmania.dirs.sys

/** Returns all XDG user directories obtained from $(XDG_CONFIG_HOME)/user-dirs.dirs. */
internal fun all(homeDirPath: String, userDirFilePath: String): MutableMap<String, String> {
    val bytes = readAll(userDirFilePath) ?: ByteArray(0)
    return parseUserDirs(homeDirPath, null, bytes)
}

/** Returns a single XDG user directory obtained from $(XDG_CONFIG_HOME)/user-dirs.dirs. */
internal fun single(
    homeDirPath: String,
    userDirFilePath: String,
    userDirName: String,
): MutableMap<String, String> {
    val bytes = readAll(userDirFilePath) ?: ByteArray(0)
    return parseUserDirs(homeDirPath, userDirName, bytes)
}

internal fun parseUserDirs(
    homeDir: String,
    userDir: String?,
    bytes: ByteArray,
): MutableMap<String, String> {
    val userDirs = LinkedHashMap<String, String>()

    val newline = '\n'.code.toByte()
    var lineStart = 0
    var idx = 0
    while (idx <= bytes.size) {
        val atEnd = idx == bytes.size
        if (!atEnd && bytes[idx] != newline) {
            idx++
            continue
        }
        val line = bytes.copyOfRange(lineStart, idx)
        idx++
        lineStart = idx

        var singleDirFound = false
        val kv = splitOnce(line, '='.code.toByte()) ?: continue
        var key = kv.first
        var value = kv.second

        key = trimBlank(key)
        val keyStr: String = if (
            startsWith(key, BYTES_XDG) && endsWith(key, BYTES_DIR_SUFFIX)
        ) {
            val sliced = key.copyOfRange(4, key.size - 4)
            val decoded = decodeUtf8(sliced) ?: continue
            when {
                userDir != null && userDir == decoded -> {
                    singleDirFound = true
                    decoded
                }
                userDir == null -> decoded
                else -> continue
            }
        } else {
            continue
        }

        // xdg-user-dirs-update uses double quotes and we don't support anything else.
        value = trimBlank(value)
        var unquoted = if (
            startsWith(value, BYTES_QUOTE) && endsWith(value, BYTES_QUOTE) && value.size >= 2
        ) {
            value.copyOfRange(1, value.size - 1)
        } else {
            continue
        }

        // Path should be either relative to the home directory or absolute.
        val isRelative: Boolean = when {
            byteArraysEqual(unquoted, BYTES_HOME_SLASH) -> {
                // "Note: To disable a directory, point it to the homedir."
                // Source: https://www.freedesktop.org/wiki/Software/xdg-user-dirs/
                // Additionally directory is reassigned to homedir when removed.
                continue
            }
            startsWith(unquoted, BYTES_HOME_SLASH) -> {
                unquoted = unquoted.copyOfRange(BYTES_HOME_SLASH.size, unquoted.size)
                true
            }
            startsWith(unquoted, BYTES_SLASH) -> false
            else -> continue
        }

        val rawPath = bytesToOsString(shellUnescape(unquoted))

        val path: String = if (isRelative) {
            joinPath(homeDir, rawPath)
        } else {
            rawPath
        }

        userDirs[keyStr] = path
        if (singleDirFound) {
            break
        }
    }

    return userDirs
}

/** Returns bytes before and after first occurrence of separator. */
internal fun splitOnce(bytes: ByteArray, separator: Byte): Pair<ByteArray, ByteArray>? {
    var i = 0
    while (i < bytes.size) {
        if (bytes[i] == separator) {
            return Pair(bytes.copyOfRange(0, i), bytes.copyOfRange(i + 1, bytes.size))
        }
        i++
    }
    return null
}

/** Returns a slice with leading and trailing blank characters removed. */
internal fun trimBlank(bytes: ByteArray): ByteArray {
    // Trim leading blank characters.
    var start = 0
    while (start < bytes.size && (bytes[start] == BYTE_SPACE || bytes[start] == BYTE_TAB)) {
        start++
    }
    val head = bytes.copyOfRange(start, bytes.size)

    // Trim trailing blank characters.
    var end = head.size
    while (end > 0 && (head[end - 1] == BYTE_SPACE || head[end - 1] == BYTE_TAB)) {
        end--
    }
    return head.copyOfRange(0, end)
}

/** Unescape bytes escaped with POSIX shell double-quotes rules (as used by xdg-user-dirs-update). */
internal fun shellUnescape(escaped: ByteArray): ByteArray {
    // We assume that byte string was created by xdg-user-dirs-update which
    // escapes all characters that might potentially have special meaning,
    // so there is no need to check if backslash is actually followed by
    // $ ` " \ or a newline.

    val unescaped = ArrayList<Byte>(escaped.size)
    var i = 0
    while (i < escaped.size) {
        val b = escaped[i]
        i++
        if (b == BYTE_BACKSLASH) {
            if (i < escaped.size) {
                unescaped.add(escaped[i])
                i++
            }
        } else {
            unescaped.add(b)
        }
    }

    return ByteArray(unescaped.size) { unescaped[it] }
}

/**
 * Reads the entire contents of a file into a byte array. Returns null when the file cannot be
 * read on the host platform (corresponds to upstream returning an io::Error in Rust).
 */
internal expect fun readAll(path: String): ByteArray?

private val BYTES_XDG = byteArrayOf('X'.code.toByte(), 'D'.code.toByte(), 'G'.code.toByte(), '_'.code.toByte())
private val BYTES_DIR_SUFFIX = byteArrayOf('_'.code.toByte(), 'D'.code.toByte(), 'I'.code.toByte(), 'R'.code.toByte())
private val BYTES_QUOTE = byteArrayOf('"'.code.toByte())
private val BYTES_HOME_SLASH = byteArrayOf(
    '$'.code.toByte(), 'H'.code.toByte(), 'O'.code.toByte(), 'M'.code.toByte(),
    'E'.code.toByte(), '/'.code.toByte(),
)
private val BYTES_SLASH = byteArrayOf('/'.code.toByte())
private const val BYTE_SPACE: Byte = 0x20
private const val BYTE_TAB: Byte = 0x09
private const val BYTE_BACKSLASH: Byte = 0x5C

private fun startsWith(haystack: ByteArray, needle: ByteArray): Boolean {
    if (haystack.size < needle.size) return false
    for (i in needle.indices) {
        if (haystack[i] != needle[i]) return false
    }
    return true
}

private fun endsWith(haystack: ByteArray, needle: ByteArray): Boolean {
    if (haystack.size < needle.size) return false
    val offset = haystack.size - needle.size
    for (i in needle.indices) {
        if (haystack[offset + i] != needle[i]) return false
    }
    return true
}

private fun byteArraysEqual(a: ByteArray, b: ByteArray): Boolean {
    if (a.size != b.size) return false
    for (i in a.indices) if (a[i] != b[i]) return false
    return true
}

private fun decodeUtf8(bytes: ByteArray): String? {
    // The upstream uses str::from_utf8 and discards the line on error; mirror that.
    if (!isValidUtf8(bytes)) return null
    return bytes.decodeToString()
}

private fun isValidUtf8(bytes: ByteArray): Boolean {
    var i = 0
    while (i < bytes.size) {
        val b = bytes[i].toInt() and 0xFF
        val width: Int = when {
            b < 0x80 -> 1
            b in 0xC2..0xDF -> 2
            b in 0xE0..0xEF -> 3
            b in 0xF0..0xF4 -> 4
            else -> return false
        }
        if (i + width > bytes.size) return false
        var j = 1
        while (j < width) {
            val cont = bytes[i + j].toInt() and 0xFF
            if (cont and 0xC0 != 0x80) return false
            j++
        }
        i += width
    }
    return true
}

private fun bytesToOsString(bytes: ByteArray): String {
    // Upstream uses OsString::from_vec which preserves arbitrary bytes on Unix. Lossy decoding to
    // UTF-8 is the closest portable Kotlin approximation; xdg-user-dirs paths are UTF-8 in practice.
    return bytes.decodeToString()
}

private fun joinPath(base: String, child: String): String {
    if (child.startsWith('/')) return child
    return if (base.endsWith('/')) base + child else "$base/$child"
}
