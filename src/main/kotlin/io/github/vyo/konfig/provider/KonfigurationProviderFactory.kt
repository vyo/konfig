package io.github.vyo.konfig.provider

import java.io.File
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

private const val KONFIG_URL = "KONFIG_URL"

private data class ExternalKonfig(
    val url: URL,
    val format: KonfigurationFormat
)

internal object KonfigurationProviderFactory {

    private val CUSTOM_KONFIG: ExternalKonfig? = System.getenv(KONFIG_URL)
        ?.let {
            try {
                URL(it).let { url ->
                    when {
                        "file" == url.protocol && url.path.isNullOrEmpty() -> File(
                            it.replaceFirst(
                                "file://",
                                ""
                            )
                        ).toURI().toURL()

                        else -> url
                    }
                }
            } catch (e: MalformedURLException) {
                println("Failed to load external konfig: ${e.message}")
                null
            }
        }
        ?.let {
            try {
                ExternalKonfig(
                    it,
                    KonfigurationFormat.from(it.file?.substringAfterLast('.') ?: "")
                )
            } catch (e: KonfigurationParsingException) {
                println(e)
                null
            }
        }

    private fun build(format: KonfigurationFormat) = when (format) {
        KonfigurationFormat.YML -> YMLProvider()
        KonfigurationFormat.JSON -> JSONProvider()
        KonfigurationFormat.PROPERTIES -> PropertiesProvider()
    }

    fun build(): Map<String, Any> = when (CUSTOM_KONFIG) {
        is ExternalKonfig -> CUSTOM_KONFIG.let { external ->
            try {
                build(CUSTOM_KONFIG.format)
                    .load(CUSTOM_KONFIG.url)
                    .also {
                        println("Loaded external konfig: $external")
                    }
            } catch (e: IOException) {
                println("Failed to load external konfig: ${e.message} ($external)")
                throw e
            }
        }
        else -> build(
            KonfigurationFormat
                .availableDefaults()
                .first()
        )
            .load(
                KonfigurationFormat
                    .availableDefaults()
                    .first()
                    .defaultURL()!!
            )
    }
}
