// port-lint: source dirs-sys/src/xdg_user_dirs.rs
package io.github.kotlinmania.dirs.sys

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class XdgUserDirsTest {

    @Test
    fun testTrimBlank() {
        assertEquals("x", trimBlank(b("x")).asString())
        assertEquals("", trimBlank(b(" \t  ")).asString())
        assertEquals("hello there", trimBlank(b(" \t hello there \t ")).asString())
        assertEquals("\r\n", trimBlank(b("\r\n")).asString())
    }

    @Test
    fun testSplitOnce() {
        assertNull(splitOnce(b("a b c"), '='.code.toByte()))
        val kv = splitOnce(b("before=after"), '='.code.toByte())
        assertEquals("before", kv!!.first.asString())
        assertEquals("after", kv.second.asString())
    }

    @Test
    fun testShellUnescape() {
        assertEquals("abc", shellUnescape(b("abc")).asString())
        assertEquals("x\\y\$z`", shellUnescape(b("x\\\\y\\\$z\\`")).asString())
    }

    @Test
    fun testParseEmpty() {
        assertEquals(emptyMap(), parseUserDirs("/root/", null, ByteArray(0)).toMap())
        assertEquals(emptyMap(), parseUserDirs("/root/", "MUSIC", ByteArray(0)).toMap())
    }

    @Test
    fun testAbsolutePathIsAccepted() {
        val expected = mapOf("MUSIC" to "/media/music")
        val bytes = b("XDG_MUSIC_DIR=\"/media/music\"")
        assertEquals(expected, parseUserDirs("/home/john", null, bytes).toMap())
        assertEquals(expected, parseUserDirs("/home/john", "MUSIC", bytes).toMap())
    }

    @Test
    fun testRelativePathIsRejected() {
        val expected = emptyMap<String, String>()
        val bytes = b("XDG_MUSIC_DIR=\"music\"")
        assertEquals(expected, parseUserDirs("/home/john", null, bytes).toMap())
        assertEquals(expected, parseUserDirs("/home/john", "MUSIC", bytes).toMap())
    }

    @Test
    fun testRelativeToHome() {
        val expected = mapOf("MUSIC" to "/home/john/Music")
        val bytes = b("XDG_MUSIC_DIR=\"\$HOME/Music\"")
        assertEquals(expected, parseUserDirs("/home/john", null, bytes).toMap())
        assertEquals(expected, parseUserDirs("/home/john", "MUSIC", bytes).toMap())
    }

    @Test
    fun testDisabledDirectory() {
        val expected = emptyMap<String, String>()
        val bytes = b("XDG_MUSIC_DIR=\"\$HOME/\"")
        assertEquals(expected, parseUserDirs("/home/john", null, bytes).toMap())
        assertEquals(expected, parseUserDirs("/home/john", "MUSIC", bytes).toMap())
    }

    @Test
    fun testParseUserDirs() {
        val expected = mapOf(
            "DESKTOP" to "/home/bob/Desktop",
            "DOWNLOAD" to "/home/bob/Downloads",
            "PICTURES" to "/home/eve/pics",
        )

        val bytes = b(
            """
            # This file is written by xdg-user-dirs-update
            # If you want to change or add directories, just edit the line you're
            # interested in. All local changes will be retained on the next run.
            # Format is XDG_xxx_DIR="${'$'}HOME/yyy", where yyy is a shell-escaped
            # homedir-relative path, or XDG_xxx_DIR="/yyy", where /yyy is an
            # absolute path. No other format is supported.
            XDG_DESKTOP_DIR="${'$'}HOME/Desktop"
            XDG_DOWNLOAD_DIR="${'$'}HOME/Downloads"
            XDG_TEMPLATES_DIR=""
            XDG_PUBLICSHARE_DIR="${'$'}HOME"
            XDG_DOCUMENTS_DIR="${'$'}HOME/"
            XDG_PICTURES_DIR="/home/eve/pics"
            XDG_VIDEOS_DIR="${'$'}HOxyzME/Videos"
            """.trimIndent(),
        )

        assertEquals(expected, parseUserDirs("/home/bob", null, bytes).toMap())

        assertEquals(
            mapOf("DESKTOP" to "/home/bob/Desktop"),
            parseUserDirs("/home/bob", "DESKTOP", bytes).toMap(),
        )

        assertEquals(
            mapOf("PICTURES" to "/home/eve/pics"),
            parseUserDirs("/home/bob", "PICTURES", bytes).toMap(),
        )

        val empty = emptyMap<String, String>()
        assertEquals(empty, parseUserDirs("/home/bob", "TEMPLATES", bytes).toMap())
        assertEquals(empty, parseUserDirs("/home/bob", "PUBLICSHARE", bytes).toMap())
        assertEquals(empty, parseUserDirs("/home/bob", "DOCUMENTS", bytes).toMap())
        assertEquals(empty, parseUserDirs("/home/bob", "VIDEOS", bytes).toMap())
    }

    private fun b(s: String): ByteArray = s.encodeToByteArray()
    private fun ByteArray.asString(): String = decodeToString()
}
