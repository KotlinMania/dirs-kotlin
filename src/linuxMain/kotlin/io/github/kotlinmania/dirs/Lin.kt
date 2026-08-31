// port-lint: source lin.rs
package io.github.kotlinmania.dirs

import io.github.kotlinmania.dirs.sys.single
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.getenv

internal actual fun sysHomeDir(): String? = Lin.homeDir()
internal actual fun sysCacheDir(): String? = Lin.cacheDir()
internal actual fun sysConfigDir(): String? = Lin.configDir()
internal actual fun sysConfigLocalDir(): String? = Lin.configLocalDir()
internal actual fun sysDataDir(): String? = Lin.dataDir()
internal actual fun sysDataLocalDir(): String? = Lin.dataLocalDir()
internal actual fun sysPreferenceDir(): String? = Lin.preferenceDir()
internal actual fun sysRuntimeDir(): String? = Lin.runtimeDir()
internal actual fun sysStateDir(): String? = Lin.stateDir()
internal actual fun sysExecutableDir(): String? = Lin.executableDir()

internal actual fun sysAudioDir(): String? = Lin.audioDir()
internal actual fun sysDesktopDir(): String? = Lin.desktopDir()
internal actual fun sysDocumentDir(): String? = Lin.documentDir()
internal actual fun sysDownloadDir(): String? = Lin.downloadDir()
internal actual fun sysFontDir(): String? = Lin.fontDir()
internal actual fun sysPictureDir(): String? = Lin.pictureDir()
internal actual fun sysPublicDir(): String? = Lin.publicDir()
internal actual fun sysTemplateDir(): String? = Lin.templateDir()
internal actual fun sysVideoDir(): String? = Lin.videoDir()

internal object Lin {
    fun homeDir(): String? = posixHome()

    fun cacheDir(): String? =
        xdgAbsoluteOrHome("XDG_CACHE_HOME", ".cache")

    fun configDir(): String? =
        xdgAbsoluteOrHome("XDG_CONFIG_HOME", ".config")

    fun configLocalDir(): String? = configDir()

    fun dataDir(): String? =
        xdgAbsoluteOrHome("XDG_DATA_HOME", ".local/share")

    fun dataLocalDir(): String? = dataDir()

    fun preferenceDir(): String? = configDir()

    fun runtimeDir(): String? =
        envAbsolutePath("XDG_RUNTIME_DIR")

    fun stateDir(): String? =
        xdgAbsoluteOrHome("XDG_STATE_HOME", ".local/state")

    fun executableDir(): String? =
        xdgAbsoluteOrHome("XDG_BIN_HOME", ".local/bin")

    fun audioDir(): String? = userDir("MUSIC")
    fun desktopDir(): String? = userDir("DESKTOP")
    fun documentDir(): String? = userDir("DOCUMENTS")
    fun downloadDir(): String? = userDir("DOWNLOAD")
    fun fontDir(): String? = dataDir()?.let { joinPath(it, "fonts") }
    fun pictureDir(): String? = userDir("PICTURES")
    fun publicDir(): String? = userDir("PUBLICSHARE")
    fun templateDir(): String? = userDir("TEMPLATES")
    fun videoDir(): String? = userDir("VIDEOS")
}


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

