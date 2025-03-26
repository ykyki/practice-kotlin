package practice.kotlin.lib.validation

import practice.kotlin.lib.curry.curry
import practice.kotlin.lib.validation.Validation.Companion.apply
import practice.kotlin.lib.validation.Validation.Companion.unit

public inline fun <A, B, ERR : Any> zip(
    fa: Validation<A, ERR>,
    crossinline f: (A) -> B,
): Validation<B, ERR> = Validation.map(fa) { f(it) }

public inline fun <A, B, C, ERR : Any> zip(
    fa: Validation<A, ERR>,
    fb: Validation<B, ERR>,
    crossinline f: (A, B) -> C,
): Validation<C, ERR> {
    // contract {
    //     callsInPlace(f, InvocationKind.EXACTLY_ONCE)
    // }

    return unit<(A) -> (B) -> C, ERR>(curry(f))
        .apply(fa)
        .apply(fb)
}

public inline fun <A, B, C, D, ERR : Any> zip(
    fa: Validation<A, ERR>,
    fb: Validation<B, ERR>,
    fc: Validation<C, ERR>,
    crossinline f: (A, B, C) -> D,
): Validation<D, ERR> = unit<(A) -> (B) -> (C) -> D, ERR>(curry(f))
    .apply(fa)
    .apply(fb)
    .apply(fc)

public inline fun <A, B, C, D, E, ERR : Any> zip(
    fa: Validation<A, ERR>,
    fb: Validation<B, ERR>,
    fc: Validation<C, ERR>,
    fd: Validation<D, ERR>,
    crossinline f: (A, B, C, D) -> E,
): Validation<E, ERR> = unit<(A) -> (B) -> (C) -> (D) -> E, ERR>(curry(f))
    .apply(fa)
    .apply(fb)
    .apply(fc)
    .apply(fd)

@Suppress("LongParameterList")
public inline fun <A, B, C, D, E, F, ERR : Any> zip(
    fa: Validation<A, ERR>,
    fb: Validation<B, ERR>,
    fc: Validation<C, ERR>,
    fd: Validation<D, ERR>,
    fe: Validation<E, ERR>,
    crossinline f: (A, B, C, D, E) -> F,
): Validation<F, ERR> = unit<(A) -> (B) -> (C) -> (D) -> (E) -> F, ERR>(curry(f))
    .apply(fa)
    .apply(fb)
    .apply(fc)
    .apply(fd)
    .apply(fe)
