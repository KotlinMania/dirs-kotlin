// port-lint: source wasm.rs
package io.github.kotlinmania.dirs

// Stub definitions to make things *compile*.

internal actual fun sysHomeDir(): String? = Wasm.homeDir()
internal actual fun sysCacheDir(): String? = Wasm.cacheDir()
internal actual fun sysConfigDir(): String? = Wasm.configDir()
internal actual fun sysConfigLocalDir(): String? = Wasm.configLocalDir()
internal actual fun sysDataDir(): String? = Wasm.dataDir()
internal actual fun sysDataLocalDir(): String? = Wasm.dataLocalDir()
internal actual fun sysPreferenceDir(): String? = Wasm.preferenceDir()
internal actual fun sysRuntimeDir(): String? = Wasm.runtimeDir()
internal actual fun sysExecutableDir(): String? = Wasm.executableDir()
internal actual fun sysStateDir(): String? = Wasm.stateDir()

internal actual fun sysAudioDir(): String? = Wasm.audioDir()
internal actual fun sysDesktopDir(): String? = Wasm.desktopDir()
internal actual fun sysDocumentDir(): String? = Wasm.documentDir()
internal actual fun sysDownloadDir(): String? = Wasm.downloadDir()
internal actual fun sysFontDir(): String? = Wasm.fontDir()
internal actual fun sysPictureDir(): String? = Wasm.pictureDir()
internal actual fun sysPublicDir(): String? = Wasm.publicDir()
internal actual fun sysTemplateDir(): String? = Wasm.templateDir()
internal actual fun sysVideoDir(): String? = Wasm.videoDir()

internal object Wasm {
    fun homeDir(): String? = null
    fun cacheDir(): String? = null
    fun configDir(): String? = null
    fun configLocalDir(): String? = null
    fun dataDir(): String? = null
    fun dataLocalDir(): String? = null
    fun preferenceDir(): String? = null
    fun runtimeDir(): String? = null
    fun executableDir(): String? = null
    fun stateDir(): String? = null

    fun audioDir(): String? = null
    fun desktopDir(): String? = null
    fun documentDir(): String? = null
    fun downloadDir(): String? = null
    fun fontDir(): String? = null
    fun pictureDir(): String? = null
    fun publicDir(): String? = null
    fun templateDir(): String? = null
    fun videoDir(): String? = null
}


