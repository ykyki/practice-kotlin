package practice.kotlin.lib.curry

public inline fun <A, B, C> curry(crossinline f: (A, B) -> C): (A) -> (B) -> C {
    return { a -> { b -> f(a, b) } }
}

public inline fun <A, B, C, D> curry(crossinline f: (A, B, C) -> D): (A) -> (B) -> (C) -> D {
    return { a -> { b -> { c -> f(a, b, c) } } }
}

public inline fun <A, B, C, D, E> curry(crossinline f: (A, B, C, D) -> E): (A) -> (B) -> (C) -> (D) -> E {
    return { a -> { b -> { c -> { d -> f(a, b, c, d) } } } }
}

public inline fun <A, B, C, D, E, F> curry(
    crossinline f: (A, B, C, D, E) -> F
): (A) -> (B) -> (C) -> (D) -> (E) -> F {
    return { a -> { b -> { c -> { d -> { e -> f(a, b, c, d, e) } } } } }
}
