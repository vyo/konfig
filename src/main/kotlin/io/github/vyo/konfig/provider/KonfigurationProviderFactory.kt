package io.github.vyo.konfig.provider

internal object KonfigurationProviderFactory {
    private fun build(format: KonfigurationFormat): KonfigurationProvider {
        return when (format) {
            KonfigurationFormat.YML -> YMLProvider()
            KonfigurationFormat.JSON -> JSONProvider()
            KonfigurationFormat.PROPERTIES -> PropertiesProvider()
        }
    }

    fun build(): Map<String, Any> {
        return build(
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
