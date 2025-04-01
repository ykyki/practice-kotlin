package practice.kotlin.lib.enummap

import java.util.*

public data class SafeEnumMap<E : Enum<E>, V> private constructor(
    private val map: EnumMap<E, V>
) : Map<E, V> by map {
    public constructor(enumClass: Class<E>, f: (E) -> V) : this(
        EnumMap<E, V>(enumClass).apply {
            enumClass.enumConstants.forEach { put(it, f(it)) }
        }
    )

    override fun get(key: E): V = map[key]!!

    public companion object {
        public inline fun <reified E : Enum<E>, V> create(noinline f: (E) -> V): SafeEnumMap<E, V> =
            SafeEnumMap(E::class.java, f)
    }
}
