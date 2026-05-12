// port-lint: source src/lin.rs
package io.github.kotlinmania.dirs

import io.github.kotlinmania.dirs.sys.single
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.getenv

internal actual fun sysHomeDir(): String? = posixHome()

internal actual fun sysCacheDir(): String? =
    xdgAbsoluteOrHome("XDG_CACHE_HOME", ".cache")

internal actual fun sysConfigDir(): String? =
    xdgAbsoluteOrHome("XDG_CONFIG_HOME", ".config")

internal actual fun sysConfigLocalDir(): String? = sysConfigDir()

internal actual fun sysDataDir(): String? =
    xdgAbsoluteOrHome("XDG_DATA_HOME", ".local/share")

internal actual fun sysDataLocalDir(): String? = sysDataDir()

internal actual fun sysPreferenceDir(): String? = sysConfigDir()

internal actual fun sysRuntimeDir(): String? =
    envAbsolutePath("XDG_RUNTIME_DIR")

internal actual fun sysStateDir(): String? =
    xdgAbsoluteOrHome("XDG_STATE_HOME", ".local/state")

internal actual fun sysExecutableDir(): String? =
    xdgAbsoluteOrHome("XDG_BIN_HOME", ".local/bin")

internal actual fun sysAudioDir(): String? = userDir("MUSIC")
internal actual fun sysDesktopDir(): String? = userDir("DESKTOP")
internal actual fun sysDocumentDir(): String? = userDir("DOCUMENTS")
internal actual fun sysDownloadDir(): String? = userDir("DOWNLOAD")
internal actual fun sysFontDir(): String? = sysDataDir()?.let { joinPath(it, "fonts") }
internal actual fun sysPictureDir(): String? = userDir("PICTURES")
internal actual fun sysPublicDir(): String? = userDir("PUBLICSHARE")
internal actual fun sysTemplateDir(): String? = userDir("TEMPLATES")
internal actual fun sysVideoDir(): String? = userDir("VIDEOS")

@OptIn(ExperimentalForeignApi::class)
private fun posixHome(): String? {
    val raw = getenv("HOME")?.toKString() ?: return null
    return if (raw.isEmpty()) null else raw
}

@OptIn(ExperimentalForeignApi::class)
private fun envAbsolutePath(name: String): String? {
    val raw = getenv(name)?.toKString() ?: return null
    return if (isAbsolutePath(raw)) raw else null
}

private fun xdgAbsoluteOrHome(envName: String, homeRelative: String): String? {
    envAbsolutePath(envName)?.let { return it }
    return posixHome()?.let { joinPath(it, homeRelative) }
}

private fun userDir(name: String): String? {
    val home = posixHome() ?: return null
    val userDirFile = joinPath(userDirFileDirectory(home), "user-dirs.dirs")
    return single(home, userDirFile, name).remove(name)
}

private fun userDirFileDirectory(home: String): String =
    envAbsolutePath("XDG_CONFIG_HOME") ?: joinPath(home, ".config")

private fun isAbsolutePath(path: String): Boolean =
    path.isNotEmpty() && path.startsWith('/')

private fun joinPath(base: String, child: String): String =
    if (base.endsWith('/')) base + child else "$base/$child"
