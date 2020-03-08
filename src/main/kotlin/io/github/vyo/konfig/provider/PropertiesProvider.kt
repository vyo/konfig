package io.github.vyo.konfig.provider

import io.github.vyo.konfig.nestRecursively
import io.github.vyo.konfig.typed
import java.net.URL
import java.util.Properties

internal class PropertiesProvider : KonfigurationProvider {
    override val format: KonfigurationFormat = KonfigurationFormat.PROPERTIES

    override fun load(url: URL): Map<String, Any> {
        return url.openStream().use { stream ->
            stream ?: throw RuntimeException("couldn't load '$url'")
            return@use Properties()
                .also { it.load(stream) }
                .map { Pair(it.key as String, (it.value as String).typed()) }
                .toMap()
                .nestRecursively()
        }
    }
}