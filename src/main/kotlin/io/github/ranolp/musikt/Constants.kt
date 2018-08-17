package io.github.ranolp.musikt

import java.nio.file.Paths

internal val MOBILE_UA = "Mozilla/5.0 (Linux; U; Android 4.4.2; en-us; SCH-I535 Build/KOT49H) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30"

internal val SOURCE_CACHE_FOLDER = Paths.get("cache/sources").toAbsolutePath()!!

// Regular Expressions

internal val YOUTUBE_SHORT = Regex("(?:https?:)?(?://)?youtu\\.be/(?<url>\\w+)")
internal val YOUTUBE_LONG = Regex("(?:https?:)?(?://)?(?:www\\.)?youtube\\.com/watch\\?(?:.+&)*v=(?<url>\\w+)(?:&.+)*")

internal val SOUNDCLOUD = Regex("(?:https?:)?(?://)?soundcloud\\.com/(?<user>\\w+)/(?<name>[\\w-]+)/?")


// API keys
internal val SOUNDCLOUD_KEY = "QyPi1UIiAXHektIfaZyKDQSp25ZaerWL"
