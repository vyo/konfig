package io.github.vyo.konfig.provider

import java.net.URL

internal interface KonfigurationProvider {
    val format: KonfigurationFormat
    fun load(url: URL): Map<String, Any>
}