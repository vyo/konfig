package io.github.vyo.konfig.provider

import io.github.vyo.konfig.DEFAULT_CONFIG_FILE_JSON
import io.github.vyo.konfig.DEFAULT_CONFIG_FILE_PROPERTIES
import io.github.vyo.konfig.DEFAULT_CONFIG_FILE_YAML
import io.github.vyo.konfig.DEFAULT_CONFIG_FILE_YML
import io.github.vyo.konfig.Konfig
import java.net.URL

sealed class KonfigurationException(message: String) : RuntimeException(message)
class KonfigurationParsingException(message: String) : KonfigurationException(message)

internal enum class KonfigurationFormat(val extension: String) {
    YML("yml"),
    JSON("json"),
    PROPERTIES("properties");

    fun defaultURL(): URL? {
        return when (this) {
            YML -> Konfig::class.java.classLoader?.getResource(DEFAULT_CONFIG_FILE_YML)
            JSON -> Konfig::class.java.classLoader?.getResource(DEFAULT_CONFIG_FILE_JSON)
            PROPERTIES -> Konfig::class.java.classLoader?.getResource(DEFAULT_CONFIG_FILE_PROPERTIES)
        }
    }

    companion object {
        fun from(extension: String): KonfigurationFormat {
            return when (extension.toLowerCase()) {
                YML.extension -> YML
                JSON.extension -> JSON
                PROPERTIES.extension -> PROPERTIES
                "yaml" -> YML
                else -> throw KonfigurationParsingException("unknown format: $extension")
            }
        }

        fun availableDefaults(): Set<KonfigurationFormat> {
            val formats = listOf(
                YMLProvider(),
                JSONProvider(),
                PropertiesProvider()
            )
                .map { it.format }
                .filter { it.defaultURL() != null }
                .toSortedSet()

            if (formats.isEmpty()) {
                throw RuntimeException(
                    "couldn't load any one of ${listOf(
                        DEFAULT_CONFIG_FILE_YML,
                        DEFAULT_CONFIG_FILE_YAML,
                        DEFAULT_CONFIG_FILE_JSON,
                        DEFAULT_CONFIG_FILE_PROPERTIES
                    )}"
                )
            } else {
                return formats
            }
        }
    }
}
