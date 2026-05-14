// port-lint: source src/mac.rs
package io.github.kotlinmania.dirs

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.getenv

internal actual fun sysHomeDir(): String? = posixHome()

private fun appSupportDir(): String? = posixHome()?.let { joinPath(it, "Library/Application Support") }

internal actual fun sysCacheDir(): String? = posixHome()?.let { joinPath(it, "Library/Caches") }
internal actual fun sysConfigDir(): String? = appSupportDir()
internal actual fun sysConfigLocalDir(): String? = appSupportDir()
internal actual fun sysDataDir(): String? = appSupportDir()
internal actual fun sysDataLocalDir(): String? = appSupportDir()
internal actual fun sysPreferenceDir(): String? = posixHome()?.let { joinPath(it, "Library/Preferences") }
internal actual fun sysExecutableDir(): String? = null
internal actual fun sysRuntimeDir(): String? = null
internal actual fun sysStateDir(): String? = null

internal actual fun sysAudioDir(): String? = posixHome()?.let { joinPath(it, "Music") }
internal actual fun sysDesktopDir(): String? = posixHome()?.let { joinPath(it, "Desktop") }
internal actual fun sysDocumentDir(): String? = posixHome()?.let { joinPath(it, "Documents") }
internal actual fun sysDownloadDir(): String? = posixHome()?.let { joinPath(it, "Downloads") }
internal actual fun sysFontDir(): String? = posixHome()?.let { joinPath(it, "Library/Fonts") }
internal actual fun sysPictureDir(): String? = posixHome()?.let { joinPath(it, "Pictures") }
internal actual fun sysPublicDir(): String? = posixHome()?.let { joinPath(it, "Public") }
internal actual fun sysTemplateDir(): String? = null
internal actual fun sysVideoDir(): String? = posixHome()?.let { joinPath(it, "Movies") }

@OptIn(ExperimentalForeignApi::class)
private fun posixHome(): String? {
    val raw = getenv("HOME")?.toKString() ?: return null
    return if (raw.isEmpty()) null else raw
}

private fun joinPath(base: String, child: String): String =
    if (base.endsWith('/')) base + child else "$base/$child"
