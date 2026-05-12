// port-lint: source src/win.rs
package io.github.kotlinmania.dirs

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.getenv

internal actual fun sysHomeDir(): String? = winEnv("USERPROFILE")

internal actual fun sysCacheDir(): String? = sysDataLocalDir()
internal actual fun sysConfigDir(): String? = winEnv("APPDATA")
internal actual fun sysConfigLocalDir(): String? = winEnv("LOCALAPPDATA")
internal actual fun sysDataDir(): String? = winEnv("APPDATA")
internal actual fun sysDataLocalDir(): String? = winEnv("LOCALAPPDATA")
internal actual fun sysExecutableDir(): String? = null
internal actual fun sysPreferenceDir(): String? = winEnv("LOCALAPPDATA")
internal actual fun sysRuntimeDir(): String? = null
internal actual fun sysStateDir(): String? = null

internal actual fun sysAudioDir(): String? = sysHomeDir()?.let { joinPath(it, "Music") }
internal actual fun sysDesktopDir(): String? = sysHomeDir()?.let { joinPath(it, "Desktop") }
internal actual fun sysDocumentDir(): String? = sysHomeDir()?.let { joinPath(it, "Documents") }
internal actual fun sysDownloadDir(): String? = sysHomeDir()?.let { joinPath(it, "Downloads") }
internal actual fun sysFontDir(): String? = null
internal actual fun sysPictureDir(): String? = sysHomeDir()?.let { joinPath(it, "Pictures") }
internal actual fun sysPublicDir(): String? = sysHomeDir()?.let { joinPath(it, "Public") }
internal actual fun sysTemplateDir(): String? =
    sysConfigDir()?.let { joinPath(it, "Microsoft\\Windows\\Templates") }
internal actual fun sysVideoDir(): String? = sysHomeDir()?.let { joinPath(it, "Videos") }

@OptIn(ExperimentalForeignApi::class)
private fun winEnv(name: String): String? {
    val raw = getenv(name)?.toKString() ?: return null
    return if (raw.isEmpty()) null else raw
}

private fun joinPath(base: String, child: String): String =
    if (base.endsWith('\\') || base.endsWith('/')) base + child else "$base\\$child"
