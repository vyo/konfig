package io.github.vyo.konfig

import io.github.vyo.konfig.provider.KonfigurationProviderFactory
import kotlin.reflect.KProperty

const val DEFAULT_CONFIG_FILE_YML = "konfig.yml"
const val DEFAULT_CONFIG_FILE_YAML = "konfig.yaml"
const val DEFAULT_CONFIG_FILE_JSON = "konfig.json"
const val DEFAULT_CONFIG_FILE_PROPERTIES = "konfig.properties"

data class KonfigValue(val key: String) {
    inline operator fun <reified A> getValue(thisRef: Any?, property: KProperty<*>): A {
        return Konfig[key] as A
    }
}

object Konfig : Map<String, Any> by KonfigurationProviderFactory.build() {
    fun nested(path: String): KonfigValue {
        return KonfigValue(path)
    }

    override fun toString(): String {
        return map { (k, v) -> Pair(k, v) }.toMap().toString()
    }
}

typealias NestedKonfig = Map<String, Any>
