// port-lint: source lib.rs
package io.github.kotlinmania.dirs

import kotlin.test.Test

class LibTest {
    @Test
    fun testDirs() {
        println("homeDir:        ${homeDir()}")
        println()
        println("cacheDir:       ${cacheDir()}")
        println("configDir:      ${configDir()}")
        println("dataDir:        ${dataDir()}")
        println("dataLocalDir:   ${dataLocalDir()}")
        println("executableDir:  ${executableDir()}")
        println("preferenceDir:  ${preferenceDir()}")
        println("runtimeDir:     ${runtimeDir()}")
        println("stateDir:       ${stateDir()}")
        println()
        println("audioDir:       ${audioDir()}")
        println("desktopDir:     ${desktopDir()}")
        println("documentDir:    ${documentDir()}")
        println("downloadDir:    ${downloadDir()}")
        println("fontDir:        ${fontDir()}")
        println("pictureDir:     ${pictureDir()}")
        println("publicDir:      ${publicDir()}")
        println("templateDir:    ${templateDir()}")
        println("videoDir:       ${videoDir()}")
    }
}
