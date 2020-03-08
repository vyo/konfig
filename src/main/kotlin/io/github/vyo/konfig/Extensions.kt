package io.github.vyo.konfig

inline fun <reified A> String.konfig(): A {
    return Konfig[this] as A
}

internal fun String.typed(): Any {
    val list = this.contains(',')
    val number = Regex("\\d+").matches(this)
    val long = number && this.toLong() !in Int.MIN_VALUE..Int.MAX_VALUE
    val int = number && this.toLong() in Int.MIN_VALUE..Int.MAX_VALUE
    val float = Regex("\\d+\\.\\d+").matches(this)
    val bool = this.equals(true.toString(), true) || this.equals(false.toString(), true)

    return when {
        list -> this.split(',').toList().map(String::typed)
        long -> this.toLong()
        int -> this.toInt()
        float -> this.toDouble()
        bool -> this.toBoolean()
        else -> this
    }
}

internal fun Map<String, Any>.flattenRecursively(): Map<String, Any> {
    return this + this.map {
        when (it.value) {
            // wrap non-Map entry in a list
            !is Map<*, *> -> listOf(Pair(it.key, it.value))
            // transform Map-entry to a list of its own entries,
            // extending their key by a depth-dependent prefix
            // also, do this recursively for this Map-entry's  own Map-entries
            else -> @Suppress("UNCHECKED_CAST") (it.value as Map<String, Any>).entries
                .map { sub: Map.Entry<String, Any> ->
                    Pair(
                        "${it.key}.${sub.key}",
                        sub.value
                    )
                }.toMap().flattenRecursively().toList()
        }
        // get rid of the wrapper lists from the previous step
    }.flatten().toMap()
}

internal fun Map<String, Any>.nestRecursively(): Map<String, Any> {
    return this + this.keys
        // start
        // create a set of top level prefixes for this flattened mapping
        .filter { Regex(".+\\..+").matches(it) }
        .map { it.substringBefore('.') }
        .toSet()
        // end
        // start
        // group map entries by common prefix and
        // transform each list of those into a map of its own
        .map { prefix ->
            Pair(
                prefix,
                this.entries.partition {
                    it.key.startsWith(prefix)
                }.first.map {
                    Pair(
                        it.key.replaceBefore('.', "").substring(1),
                        it.value
                    )
                }.toMap().nestRecursively()
            )
        }.toMap()
}
