// port-lint: source lin.rs
package io.github.kotlinmania.dirs

import kotlin.test.Test

class LinTest {
    @Test
    fun testFileUserDirsExists() {
        val userDirsFile = joinPath(checkNotNull(configDir()), "user-dirs.dirs")
        println("$userDirsFile exists: ${fileExists(userDirsFile)}")
    }

    private fun joinPath(base: String, child: String): String =
        if (base.endsWith('/')) base + child else "$base/$child"

    private fun fileExists(path: String): Boolean =
        platform.posix.access(path, platform.posix.F_OK) == 0
}
