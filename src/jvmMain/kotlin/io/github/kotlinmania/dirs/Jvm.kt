// port-lint: ignore - JVM dispatches to the upstream platform rules in lin.rs, mac.rs, and win.rs.
package io.github.kotlinmania.dirs

import io.github.kotlinmania.dirs.sys.single

internal actual fun sysHomeDir(): String? = when (hostFamily()) {
    HostFamily.Windows -> winEnv("USERPROFILE")
    HostFamily.Mac,
    HostFamily.Unix,
    -> posixHome()
}

internal actual fun sysCacheDir(): String? = when (hostFamily()) {
    HostFamily.Windows -> sysDataLocalDir()
    HostFamily.Mac -> posixHome()?.let { joinPosixPath(it, "Library/Caches") }
    HostFamily.Unix -> xdgAbsoluteOrHome("XDG_CACHE_HOME", ".cache")
}

internal actual fun sysConfigDir(): String? = when (hostFamily()) {
    HostFamily.Windows -> winEnv("APPDATA")
    HostFamily.Mac -> appSupportDir()
    HostFamily.Unix -> xdgAbsoluteOrHome("XDG_CONFIG_HOME", ".config")
}

internal actual fun sysConfigLocalDir(): String? = when (hostFamily()) {
    HostFamily.Windows -> winEnv("LOCALAPPDATA")
    HostFamily.Mac -> appSupportDir()
    HostFamily.Unix -> sysConfigDir()
}

internal actual fun sysDataDir(): String? = when (hostFamily()) {
    HostFamily.Windows -> winEnv("APPDATA")
    HostFamily.Mac -> appSupportDir()
    HostFamily.Unix -> xdgAbsoluteOrHome("XDG_DATA_HOME", ".local/share")
}

internal actual fun sysDataLocalDir(): String? = when (hostFamily()) {
    HostFamily.Windows -> winEnv("LOCALAPPDATA")
    HostFamily.Mac -> appSupportDir()
    HostFamily.Unix -> sysDataDir()
}

internal actual fun sysExecutableDir(): String? = when (hostFamily()) {
    HostFamily.Windows,
    HostFamily.Mac,
    -> null
    HostFamily.Unix -> xdgAbsoluteOrHome("XDG_BIN_HOME", ".local/bin")
}

internal actual fun sysPreferenceDir(): String? = when (hostFamily()) {
    HostFamily.Windows -> winEnv("LOCALAPPDATA")
    HostFamily.Mac -> posixHome()?.let { joinPosixPath(it, "Library/Preferences") }
    HostFamily.Unix -> sysConfigDir()
}

internal actual fun sysRuntimeDir(): String? = when (hostFamily()) {
    HostFamily.Windows,
    HostFamily.Mac,
    -> null
    HostFamily.Unix -> envAbsolutePath("XDG_RUNTIME_DIR")
}

internal actual fun sysStateDir(): String? = when (hostFamily()) {
    HostFamily.Windows,
    HostFamily.Mac,
    -> null
    HostFamily.Unix -> xdgAbsoluteOrHome("XDG_STATE_HOME", ".local/state")
}

internal actual fun sysAudioDir(): String? = when (hostFamily()) {
    HostFamily.Windows -> sysHomeDir()?.let { joinWindowsPath(it, "Music") }
    HostFamily.Mac -> posixHome()?.let { joinPosixPath(it, "Music") }
    HostFamily.Unix -> userDir("MUSIC")
}

internal actual fun sysDesktopDir(): String? = when (hostFamily()) {
    HostFamily.Windows -> sysHomeDir()?.let { joinWindowsPath(it, "Desktop") }
    HostFamily.Mac -> posixHome()?.let { joinPosixPath(it, "Desktop") }
    HostFamily.Unix -> userDir("DESKTOP")
}

internal actual fun sysDocumentDir(): String? = when (hostFamily()) {
    HostFamily.Windows -> sysHomeDir()?.let { joinWindowsPath(it, "Documents") }
    HostFamily.Mac -> posixHome()?.let { joinPosixPath(it, "Documents") }
    HostFamily.Unix -> userDir("DOCUMENTS")
}

internal actual fun sysDownloadDir(): String? = when (hostFamily()) {
    HostFamily.Windows -> sysHomeDir()?.let { joinWindowsPath(it, "Downloads") }
    HostFamily.Mac -> posixHome()?.let { joinPosixPath(it, "Downloads") }
    HostFamily.Unix -> userDir("DOWNLOAD")
}

internal actual fun sysFontDir(): String? = when (hostFamily()) {
    HostFamily.Windows -> null
    HostFamily.Mac -> posixHome()?.let { joinPosixPath(it, "Library/Fonts") }
    HostFamily.Unix -> sysDataDir()?.let { joinPosixPath(it, "fonts") }
}

internal actual fun sysPictureDir(): String? = when (hostFamily()) {
    HostFamily.Windows -> sysHomeDir()?.let { joinWindowsPath(it, "Pictures") }
    HostFamily.Mac -> posixHome()?.let { joinPosixPath(it, "Pictures") }
    HostFamily.Unix -> userDir("PICTURES")
}

internal actual fun sysPublicDir(): String? = when (hostFamily()) {
    HostFamily.Windows -> sysHomeDir()?.let { joinWindowsPath(it, "Public") }
    HostFamily.Mac -> posixHome()?.let { joinPosixPath(it, "Public") }
    HostFamily.Unix -> userDir("PUBLICSHARE")
}

internal actual fun sysTemplateDir(): String? = when (hostFamily()) {
    HostFamily.Windows -> sysConfigDir()?.let { joinWindowsPath(it, "Microsoft\\Windows\\Templates") }
    HostFamily.Mac -> null
    HostFamily.Unix -> userDir("TEMPLATES")
}

internal actual fun sysVideoDir(): String? = when (hostFamily()) {
    HostFamily.Windows -> sysHomeDir()?.let { joinWindowsPath(it, "Videos") }
    HostFamily.Mac -> posixHome()?.let { joinPosixPath(it, "Movies") }
    HostFamily.Unix -> userDir("VIDEOS")
}

private enum class HostFamily {
    Windows,
    Mac,
    Unix,
}

private fun hostFamily(): HostFamily {
    val osName = System.getProperty("os.name").orEmpty().lowercase()
    return when {
        osName.startsWith("windows") -> HostFamily.Windows
        osName.startsWith("mac") || osName == "darwin" -> HostFamily.Mac
        else -> HostFamily.Unix
    }
}

private fun appSupportDir(): String? =
    posixHome()?.let { joinPosixPath(it, "Library/Application Support") }

private fun posixHome(): String? = env("HOME")

private fun envAbsolutePath(name: String): String? {
    val raw = env(name) ?: return null
    return if (isAbsolutePath(raw)) raw else null
}

private fun xdgAbsoluteOrHome(envName: String, homeRelative: String): String? {
    envAbsolutePath(envName)?.let { return it }
    return posixHome()?.let { joinPosixPath(it, homeRelative) }
}

private fun userDir(name: String): String? {
    val home = posixHome() ?: return null
    val userDirFile = joinPosixPath(userDirFileDirectory(home), "user-dirs.dirs")
    return single(home, userDirFile, name).remove(name)
}

private fun userDirFileDirectory(home: String): String =
    envAbsolutePath("XDG_CONFIG_HOME") ?: joinPosixPath(home, ".config")

private fun winEnv(name: String): String? = env(name)

private fun env(name: String): String? {
    val raw = try {
        System.getenv(name)
    } catch (_: SecurityException) {
        null
    }
    return raw?.takeIf { it.isNotEmpty() }
}

private fun isAbsolutePath(path: String): Boolean =
    path.isNotEmpty() && path.startsWith('/')

private fun joinPosixPath(base: String, child: String): String =
    if (base.endsWith('/')) base + child else "$base/$child"

private fun joinWindowsPath(base: String, child: String): String =
    if (base.endsWith('\\') || base.endsWith('/')) base + child else "$base\\$child"
