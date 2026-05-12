// port-lint: source src/lib.rs
package io.github.kotlinmania.dirs

/**
 * The _dirs_ crate is
 *
 * - a tiny library with a minimal API (18 public functions)
 * - that provides the platform-specific, user-accessible locations
 * - for finding and storing configuration, cache and other data
 * - on Linux, Redox, Windows (≥ Vista) and macOS.
 *
 * The library provides the location of these directories by leveraging the mechanisms defined by
 *
 * - the [XDG base directory](https://standards.freedesktop.org/basedir-spec/basedir-spec-latest.html) and the [XDG user directory](https://www.freedesktop.org/wiki/Software/xdg-user-dirs/) specifications on Linux,
 * - the [Known Folder](https://msdn.microsoft.com/en-us/library/windows/desktop/bb776911(v=vs.85).aspx) system on Windows, and
 * - the [Standard Directories](https://developer.apple.com/library/content/documentation/FileManagement/Conceptual/FileSystemProgrammingGuide/FileSystemOverview/FileSystemOverview.html#//apple_ref/doc/uid/TP40010672-CH2-SW6) on macOS.
 *
 * Upstream Rust returns `Option<PathBuf>`. The Kotlin port returns a nullable [String]
 * (filesystem path) because Kotlin Multiplatform has no portable `PathBuf` type.
 */

/**
 * Returns the path to the user's home directory.
 *
 * The returned value depends on the operating system and is either a non-null value from the
 * following table, or `null`.
 *
 * |Platform | Value                | Example        |
 * | ------- | -------------------- | -------------- |
 * | Linux   | `$HOME`              | /home/alice    |
 * | macOS   | `$HOME`              | /Users/Alice   |
 * | Windows | `{FOLDERID_Profile}` | C:\Users\Alice |
 *
 * ### Linux and macOS:
 *
 * - Use `$HOME` if it is set and not empty.
 * - If `$HOME` is not set or empty, then the platform fallback for determining the home
 *   directory of the current user is consulted (POSIX `getpwuid_r` upstream; Kotlin Multiplatform
 *   does not portably expose it, so the fallback returns `null`).
 *
 * ### Windows:
 *
 * Upstream retrieves the user profile folder using `SHGetKnownFolderPath`. The Kotlin port falls
 * back to the `USERPROFILE` environment variable, which Windows ships exporting for the same
 * folder.
 *
 * All the examples on this page mentioning `$HOME` use this behavior.
 */
public fun homeDir(): String? = sysHomeDir()

/**
 * Returns the path to the user's cache directory.
 *
 * The returned value depends on the operating system and is either a non-null value from the
 * following table, or `null`.
 *
 * |Platform | Value                               | Example                      |
 * | ------- | ----------------------------------- | ---------------------------- |
 * | Linux   | `$XDG_CACHE_HOME` or `$HOME`/.cache | /home/alice/.cache           |
 * | macOS   | `$HOME`/Library/Caches              | /Users/Alice/Library/Caches  |
 * | Windows | `{FOLDERID_LocalAppData}`           | C:\Users\Alice\AppData\Local |
 */
public fun cacheDir(): String? = sysCacheDir()

/**
 * Returns the path to the user's config directory.
 *
 * |Platform | Value                                 | Example                                  |
 * | ------- | ------------------------------------- | ---------------------------------------- |
 * | Linux   | `$XDG_CONFIG_HOME` or `$HOME`/.config | /home/alice/.config                      |
 * | macOS   | `$HOME`/Library/Application Support   | /Users/Alice/Library/Application Support |
 * | Windows | `{FOLDERID_RoamingAppData}`           | C:\Users\Alice\AppData\Roaming           |
 */
public fun configDir(): String? = sysConfigDir()

/**
 * Returns the path to the user's local config directory.
 *
 * |Platform | Value                                 | Example                                  |
 * | ------- | ------------------------------------- | ---------------------------------------- |
 * | Linux   | `$XDG_CONFIG_HOME` or `$HOME`/.config | /home/alice/.config                      |
 * | macOS   | `$HOME`/Library/Application Support   | /Users/Alice/Library/Application Support |
 * | Windows | `{FOLDERID_LocalAppData}`             | C:\Users\Alice\AppData\Local             |
 */
public fun configLocalDir(): String? = sysConfigLocalDir()

/**
 * Returns the path to the user's data directory.
 *
 * |Platform | Value                                    | Example                                  |
 * | ------- | ---------------------------------------- | ---------------------------------------- |
 * | Linux   | `$XDG_DATA_HOME` or `$HOME`/.local/share | /home/alice/.local/share                 |
 * | macOS   | `$HOME`/Library/Application Support      | /Users/Alice/Library/Application Support |
 * | Windows | `{FOLDERID_RoamingAppData}`              | C:\Users\Alice\AppData\Roaming           |
 */
public fun dataDir(): String? = sysDataDir()

/**
 * Returns the path to the user's local data directory.
 *
 * |Platform | Value                                    | Example                                  |
 * | ------- | ---------------------------------------- | ---------------------------------------- |
 * | Linux   | `$XDG_DATA_HOME` or `$HOME`/.local/share | /home/alice/.local/share                 |
 * | macOS   | `$HOME`/Library/Application Support      | /Users/Alice/Library/Application Support |
 * | Windows | `{FOLDERID_LocalAppData}`                | C:\Users\Alice\AppData\Local             |
 */
public fun dataLocalDir(): String? = sysDataLocalDir()

/**
 * Returns the path to the user's executable directory.
 *
 * |Platform | Value                                                            | Example                |
 * | ------- | ---------------------------------------------------------------- | ---------------------- |
 * | Linux   | `$XDG_BIN_HOME` or `$XDG_DATA_HOME`/../bin or `$HOME`/.local/bin | /home/alice/.local/bin |
 * | macOS   | –                                                                | –                      |
 * | Windows | –                                                                | –                      |
 */
public fun executableDir(): String? = sysExecutableDir()

/**
 * Returns the path to the user's preference directory.
 *
 * |Platform | Value                                 | Example                          |
 * | ------- | ------------------------------------- | -------------------------------- |
 * | Linux   | `$XDG_CONFIG_HOME` or `$HOME`/.config | /home/alice/.config              |
 * | macOS   | `$HOME`/Library/Preferences           | /Users/Alice/Library/Preferences |
 * | Windows | `{FOLDERID_RoamingAppData}`           | C:\Users\Alice\AppData\Roaming   |
 */
public fun preferenceDir(): String? = sysPreferenceDir()

/**
 * Returns the path to the user's runtime directory.
 *
 * The runtime directory contains transient, non-essential data (like sockets or named pipes) that
 * is expected to be cleared when the user's session ends.
 *
 * |Platform | Value              | Example         |
 * | ------- | ------------------ | --------------- |
 * | Linux   | `$XDG_RUNTIME_DIR` | /run/user/1001/ |
 * | macOS   | –                  | –               |
 * | Windows | –                  | –               |
 */
public fun runtimeDir(): String? = sysRuntimeDir()

/**
 * Returns the path to the user's state directory.
 *
 * The state directory contains data that should be retained between sessions (unlike the runtime
 * directory), but may not be important/portable enough to be synchronized across machines (unlike
 * the config/preferences/data directories).
 *
 * |Platform | Value                                     | Example                  |
 * | ------- | ----------------------------------------- | ------------------------ |
 * | Linux   | `$XDG_STATE_HOME` or `$HOME`/.local/state | /home/alice/.local/state |
 * | macOS   | –                                         | –                        |
 * | Windows | –                                         | –                        |
 */
public fun stateDir(): String? = sysStateDir()

/**
 * Returns the path to the user's audio directory.
 *
 * |Platform | Value              | Example              |
 * | ------- | ------------------ | -------------------- |
 * | Linux   | `XDG_MUSIC_DIR`    | /home/alice/Music    |
 * | macOS   | `$HOME`/Music      | /Users/Alice/Music   |
 * | Windows | `{FOLDERID_Music}` | C:\Users\Alice\Music |
 */
public fun audioDir(): String? = sysAudioDir()

/**
 * Returns the path to the user's desktop directory.
 *
 * |Platform | Value                | Example                |
 * | ------- | -------------------- | ---------------------- |
 * | Linux   | `XDG_DESKTOP_DIR`    | /home/alice/Desktop    |
 * | macOS   | `$HOME`/Desktop      | /Users/Alice/Desktop   |
 * | Windows | `{FOLDERID_Desktop}` | C:\Users\Alice\Desktop |
 */
public fun desktopDir(): String? = sysDesktopDir()

/**
 * Returns the path to the user's document directory.
 *
 * |Platform | Value                  | Example                  |
 * | ------- | ---------------------- | ------------------------ |
 * | Linux   | `XDG_DOCUMENTS_DIR`    | /home/alice/Documents    |
 * | macOS   | `$HOME`/Documents      | /Users/Alice/Documents   |
 * | Windows | `{FOLDERID_Documents}` | C:\Users\Alice\Documents |
 */
public fun documentDir(): String? = sysDocumentDir()

/**
 * Returns the path to the user's download directory.
 *
 * |Platform | Value                  | Example                  |
 * | ------- | ---------------------- | ------------------------ |
 * | Linux   | `XDG_DOWNLOAD_DIR`     | /home/alice/Downloads    |
 * | macOS   | `$HOME`/Downloads      | /Users/Alice/Downloads   |
 * | Windows | `{FOLDERID_Downloads}` | C:\Users\Alice\Downloads |
 */
public fun downloadDir(): String? = sysDownloadDir()

/**
 * Returns the path to the user's font directory.
 *
 * |Platform | Value                                                | Example                        |
 * | ------- | ---------------------------------------------------- | ------------------------------ |
 * | Linux   | `$XDG_DATA_HOME`/fonts or `$HOME`/.local/share/fonts | /home/alice/.local/share/fonts |
 * | macOS   | `$HOME/Library/Fonts`                                | /Users/Alice/Library/Fonts     |
 * | Windows | –                                                    | –                              |
 */
public fun fontDir(): String? = sysFontDir()

/**
 * Returns the path to the user's picture directory.
 *
 * |Platform | Value                 | Example                 |
 * | ------- | --------------------- | ----------------------- |
 * | Linux   | `XDG_PICTURES_DIR`    | /home/alice/Pictures    |
 * | macOS   | `$HOME`/Pictures      | /Users/Alice/Pictures   |
 * | Windows | `{FOLDERID_Pictures}` | C:\Users\Alice\Pictures |
 */
public fun pictureDir(): String? = sysPictureDir()

/**
 * Returns the path to the user's public directory.
 *
 * |Platform | Value                 | Example             |
 * | ------- | --------------------- | ------------------- |
 * | Linux   | `XDG_PUBLICSHARE_DIR` | /home/alice/Public  |
 * | macOS   | `$HOME`/Public        | /Users/Alice/Public |
 * | Windows | `{FOLDERID_Public}`   | C:\Users\Public     |
 */
public fun publicDir(): String? = sysPublicDir()

/**
 * Returns the path to the user's template directory.
 *
 * |Platform | Value                  | Example                                                    |
 * | ------- | ---------------------- | ---------------------------------------------------------- |
 * | Linux   | `XDG_TEMPLATES_DIR`    | /home/alice/Templates                                      |
 * | macOS   | –                      | –                                                          |
 * | Windows | `{FOLDERID_Templates}` | C:\Users\Alice\AppData\Roaming\Microsoft\Windows\Templates |
 */
public fun templateDir(): String? = sysTemplateDir()

/**
 * Returns the path to the user's video directory.
 *
 * |Platform | Value               | Example               |
 * | ------- | ------------------- | --------------------- |
 * | Linux   | `XDG_VIDEOS_DIR`    | /home/alice/Videos    |
 * | macOS   | `$HOME`/Movies      | /Users/Alice/Movies   |
 * | Windows | `{FOLDERID_Videos}` | C:\Users\Alice\Videos |
 */
public fun videoDir(): String? = sysVideoDir()

internal expect fun sysHomeDir(): String?
internal expect fun sysCacheDir(): String?
internal expect fun sysConfigDir(): String?
internal expect fun sysConfigLocalDir(): String?
internal expect fun sysDataDir(): String?
internal expect fun sysDataLocalDir(): String?
internal expect fun sysExecutableDir(): String?
internal expect fun sysPreferenceDir(): String?
internal expect fun sysRuntimeDir(): String?
internal expect fun sysStateDir(): String?
internal expect fun sysAudioDir(): String?
internal expect fun sysDesktopDir(): String?
internal expect fun sysDocumentDir(): String?
internal expect fun sysDownloadDir(): String?
internal expect fun sysFontDir(): String?
internal expect fun sysPictureDir(): String?
internal expect fun sysPublicDir(): String?
internal expect fun sysTemplateDir(): String?
internal expect fun sysVideoDir(): String?
