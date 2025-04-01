package practice.kotlin.lib.enummap

import kotlin.enums.enumEntries

internal fun main() {
    val map: SafeEnumMap<Direction, String> = SafeEnumMap.create { direction ->
        when (direction) {
            Direction.NORTH -> "N"
            Direction.SOUTH -> "S"
            Direction.EAST -> "E"
            Direction.WEST -> "W"
        }
    }

    val a = map[Direction.NORTH]
    requireNotNull(a)

    for (d in enumEntries<Direction>()) {
        println("Direction: $d, Value: ${map[d]}")
    }

    for ((k, v) in map) {
        println("Key: $k, Value: $v")
    }
}

internal enum class Direction {
    SOUTH,
    WEST,
    NORTH,
    EAST,
}
