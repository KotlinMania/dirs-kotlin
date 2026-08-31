// port-lint: source win.rs
package io.github.kotlinmania.dirs

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.getenv

internal actual fun sysHomeDir(): String? = Win.homeDir()
internal actual fun sysCacheDir(): String? = Win.cacheDir()
internal actual fun sysConfigDir(): String? = Win.configDir()
internal actual fun sysConfigLocalDir(): String? = Win.configLocalDir()
internal actual fun sysDataDir(): String? = Win.dataDir()
internal actual fun sysDataLocalDir(): String? = Win.dataLocalDir()
internal actual fun sysExecutableDir(): String? = Win.executableDir()
internal actual fun sysPreferenceDir(): String? = Win.preferenceDir()
internal actual fun sysRuntimeDir(): String? = Win.runtimeDir()
internal actual fun sysStateDir(): String? = Win.stateDir()

internal actual fun sysAudioDir(): String? = Win.audioDir()
internal actual fun sysDesktopDir(): String? = Win.desktopDir()
internal actual fun sysDocumentDir(): String? = Win.documentDir()
internal actual fun sysDownloadDir(): String? = Win.downloadDir()
internal actual fun sysFontDir(): String? = Win.fontDir()
internal actual fun sysPictureDir(): String? = Win.pictureDir()
internal actual fun sysPublicDir(): String? = Win.publicDir()
internal actual fun sysTemplateDir(): String? = Win.templateDir()
internal actual fun sysVideoDir(): String? = Win.videoDir()

internal object Win {
    fun homeDir(): String? = winEnv("USERPROFILE")

    fun cacheDir(): String? = dataLocalDir()
    fun configDir(): String? = winEnv("APPDATA")
    fun configLocalDir(): String? = winEnv("LOCALAPPDATA")
    fun dataDir(): String? = winEnv("APPDATA")
    fun dataLocalDir(): String? = winEnv("LOCALAPPDATA")
    fun executableDir(): String? = null
    fun preferenceDir(): String? = winEnv("LOCALAPPDATA")
    fun runtimeDir(): String? = null
    fun stateDir(): String? = null

    fun audioDir(): String? = homeDir()?.let { joinPath(it, "Music") }
    fun desktopDir(): String? = homeDir()?.let { joinPath(it, "Desktop") }
    fun documentDir(): String? = homeDir()?.let { joinPath(it, "Documents") }
    fun downloadDir(): String? = homeDir()?.let { joinPath(it, "Downloads") }
    fun fontDir(): String? = null
    fun pictureDir(): String? = homeDir()?.let { joinPath(it, "Pictures") }
    fun publicDir(): String? = homeDir()?.let { joinPath(it, "Public") }
    fun templateDir(): String? =
        configDir()?.let { joinPath(it, "Microsoft\\Windows\\Templates") }
    fun videoDir(): String? = homeDir()?.let { joinPath(it, "Videos") }
}

@OptIn(ExperimentalForeignApi::class)
private fun winEnv(name: String): String? {
    val raw = getenv(name)?.toKString() ?: return null
    return if (raw.isEmpty()) null else raw
}

private fun joinPath(base: String, child: String): String =
    if (base.endsWith('\\') || base.endsWith('/')) base + child else "$base\\$child"


