package practice.kotlin.lib.validation

import com.github.michaelbull.result.Result

public sealed class Validation<out A, out E : Any> {
    @Suppress("TooManyFunctions")
    public companion object {
        public fun <A> ok(a: A): Validation<A, Nothing> = Ok(a)
        public fun <E : Any> err(e: E): Validation<Nothing, E> = Err(e)

        public fun <A, E : Any> unit(a: A): Validation<A, E> = ok(a)

        public fun <A, B, E : Any> (Validation<(A) -> B, E>).apply(
            fa: Validation<A, E>,
        ): Validation<B, E> = when (this) {
            is Ok -> when (fa) {
                is Ok -> ok(this.value(fa.value))
                is Err -> fa
            }

            is Err -> when (fa) {
                is Ok -> this
                is Err -> Err(this.head, this.tail + fa.errors())
            }
        }

        public fun <A, B, E : Any> map(
            fa: Validation<A, E>,
            f: (A) -> B
        ): Validation<B, E> = when (fa) {
            is Ok -> ok(f(fa.value))
            is Err -> fa
        }

        public fun <A, B, C, E : Any> map2(
            fa: Validation<A, E>,
            fb: Validation<B, E>,
            f: (A, B) -> C
        ): Validation<C, E> =
            unit<(A) -> (B) -> C, E> { a -> { b -> f(a, b) } }
                .apply(fa)
                .apply(fb)

        public fun <A, B, C, D, E : Any> map3(
            fa: Validation<A, E>,
            fb: Validation<B, E>,
            fc: Validation<C, E>,
            f: (A, B, C) -> D
        ): Validation<D, E> =
            unit<(A) -> (B) -> (C) -> D, E> { a -> { b -> { c -> f(a, b, c) } } }
                .apply(fa)
                .apply(fb)
                .apply(fc)

        public fun <A, B, C, D, E, Err : Any> map4(
            fa: Validation<A, Err>,
            fb: Validation<B, Err>,
            fc: Validation<C, Err>,
            fd: Validation<D, Err>,
            f: (A, B, C, D) -> E
        ): Validation<E, Err> =
            unit<(A) -> (B) -> (C) -> (D) -> E, Err> { a -> { b -> { c -> { d -> f(a, b, c, d) } } } }
                .apply(fa)
                .apply(fb)
                .apply(fc)
                .apply(fd)

        @Suppress("LongParameterList")
        public fun <A, B, C, D, E, F, Err : Any> map5(
            fa: Validation<A, Err>,
            fb: Validation<B, Err>,
            fc: Validation<C, Err>,
            fd: Validation<D, Err>,
            fe: Validation<E, Err>,
            f: (A, B, C, D, E) -> F
        ): Validation<F, Err> =
            @Suppress("ParameterListWrapping", "Wrapping", "MaximumLineLength", "MaxLineLength", "ArgumentListWrapping")
            unit<(A) -> (B) -> (C) -> (D) -> (E) -> F, Err> { a -> { b -> { c -> { d -> { e -> f(a, b, c, d, e) } } } } } // @formatter:off
                .apply(fa)
                .apply(fb)
                .apply(fc)
                .apply(fd)
                .apply(fe)

        public fun <A, B, E : Any> foldToConst(
            fa: Validation<A, E>,
            vararg fbs: Validation<B, E>
        ): Validation<A, E> = fbs.fold(fa) { acc, fb -> map2(acc, fb) { a, _ -> a } }

        public fun <A, B, E : Any> traverse(
            la: List<A>,
            f: (A) -> Validation<B, E>,
        ): Validation<List<B>, E> =
            la.fold(unit(mutableListOf<B>())) { acc, a ->
                map2(acc, f(a)) { bs, b ->
                    bs.apply { add(b) }
                }
            }
    }

    public fun isOk(): Boolean = this is Ok
    public fun isErr(): Boolean = this is Err

    public fun <B> map(f: (A) -> B): Validation<B, E> = map(this, f)
}

public fun <A, E : Any> Result<A, E>.toValidation() = when {
    isOk -> Validation.ok(value)
    else -> Validation.err(error)
}

public data class Ok<A>(val value: A) : Validation<A, Nothing>()

public data class Err<E : Any>(
    internal val head: E,
    internal val tail: List<E> = emptyList(),
) : Validation<Nothing, E>() {
    public fun errors(): List<E> = listOf(head) + tail
}
