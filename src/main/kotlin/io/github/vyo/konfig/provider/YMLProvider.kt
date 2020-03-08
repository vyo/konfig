package io.github.vyo.konfig.provider

import io.github.vyo.konfig.flattenRecursively
import org.yaml.snakeyaml.Yaml
import java.net.URL

internal class YMLProvider : KonfigurationProvider {
    override val format: KonfigurationFormat = KonfigurationFormat.YML

    override fun load(url: URL): Map<String, Any> {
        return url.openStream().use {
            it ?: throw RuntimeException("couldn't load '$url'")
            return@use Yaml().load<Map<String, Any>>(it).flattenRecursively()
        }
    }
}