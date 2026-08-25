// port-lint: source mac.rs
package io.github.kotlinmania.dirs

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.getenv

internal actual fun sysHomeDir(): String? = Mac.homeDir()
internal actual fun sysCacheDir(): String? = Mac.cacheDir()
internal actual fun sysConfigDir(): String? = Mac.configDir()
internal actual fun sysConfigLocalDir(): String? = Mac.configLocalDir()
internal actual fun sysDataDir(): String? = Mac.dataDir()
internal actual fun sysDataLocalDir(): String? = Mac.dataLocalDir()
internal actual fun sysPreferenceDir(): String? = Mac.preferenceDir()
internal actual fun sysExecutableDir(): String? = Mac.executableDir()
internal actual fun sysRuntimeDir(): String? = Mac.runtimeDir()
internal actual fun sysStateDir(): String? = Mac.stateDir()

internal actual fun sysAudioDir(): String? = Mac.audioDir()
internal actual fun sysDesktopDir(): String? = Mac.desktopDir()
internal actual fun sysDocumentDir(): String? = Mac.documentDir()
internal actual fun sysDownloadDir(): String? = Mac.downloadDir()
internal actual fun sysFontDir(): String? = Mac.fontDir()
internal actual fun sysPictureDir(): String? = Mac.pictureDir()
internal actual fun sysPublicDir(): String? = Mac.publicDir()
internal actual fun sysTemplateDir(): String? = Mac.templateDir()
internal actual fun sysVideoDir(): String? = Mac.videoDir()

internal object Mac {
    fun homeDir(): String? = posixHome()

    fun appSupportDir(): String? = homeDir()?.let { joinPath(it, "Library/Application Support") }

    fun cacheDir(): String? = homeDir()?.let { joinPath(it, "Library/Caches") }
    fun configDir(): String? = appSupportDir()
    fun configLocalDir(): String? = appSupportDir()
    fun dataDir(): String? = appSupportDir()
    fun dataLocalDir(): String? = appSupportDir()
    fun preferenceDir(): String? = homeDir()?.let { joinPath(it, "Library/Preferences") }
    fun executableDir(): String? = null
    fun runtimeDir(): String? = null
    fun stateDir(): String? = null

    fun audioDir(): String? = homeDir()?.let { joinPath(it, "Music") }
    fun desktopDir(): String? = homeDir()?.let { joinPath(it, "Desktop") }
    fun documentDir(): String? = homeDir()?.let { joinPath(it, "Documents") }
    fun downloadDir(): String? = homeDir()?.let { joinPath(it, "Downloads") }
    fun fontDir(): String? = homeDir()?.let { joinPath(it, "Library/Fonts") }
    fun pictureDir(): String? = homeDir()?.let { joinPath(it, "Pictures") }
    fun publicDir(): String? = homeDir()?.let { joinPath(it, "Public") }
    fun templateDir(): String? = null
    fun videoDir(): String? = homeDir()?.let { joinPath(it, "Movies") }
}

@OptIn(ExperimentalForeignApi::class)
private fun posixHome(): String? {
    val raw = getenv("HOME")?.toKString() ?: return null
    return if (raw.isEmpty()) null else raw
}

private fun joinPath(base: String, child: String): String =
    if (base.endsWith('/')) base + child else "$base/$child"


