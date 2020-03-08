package io.github.vyo.konfig.provider

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.vyo.konfig.DEFAULT_CONFIG_FILE_JSON
import io.github.vyo.konfig.Konfig
import io.github.vyo.konfig.flattenRecursively
import java.net.URL

internal class JSONProvider : KonfigurationProvider {
    override val format: KonfigurationFormat = KonfigurationFormat.JSON

    override fun load(url: URL): Map<String, Any> {
        return url.openStream().use {
            it ?: throw RuntimeException("couldn't load '$url'")
            return@use (ObjectMapper().readValue(it, Map::class.java) as Map<String, Any>).flattenRecursively()
        }
    }
}