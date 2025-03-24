@file:Suppress("MatchingDeclarationName", "MagicNumber")

package practice.kotlin.lib.validation

import java.time.Instant
import java.time.format.DateTimeParseException

@JvmInline
internal value class FormatError(val value: String)

internal fun main() {
    // val user = User.tryFrom("Alice", 12, "2021-01-01T00:00:00Z")
    val user = User.tryFrom("Ali ce", -12, "2021-01-01T00:00:00T+09:00a")
    println(user)
}

internal data class User(
    val name: UserName,
    val age: UserAge,
    val updatedAt: UserUpdatedAt,
) {
    companion object {
        fun tryFrom(
            name: String,
            age: Int,
            updatedAt: String,
        ) =
            Validation.map3(
                UserName.tryFrom(name),
                UserAge.tryFrom(age),
                UserUpdatedAt.tryFrom(updatedAt),
                ::User,
            )
    }
}

internal data class UserName(val valur: String) {
    internal companion object {
        private const val NAME = "username"

        fun tryFrom(s: String) =
            Validation.map3(
                s.shouldBeLongerThan(3, NAME),
                s.shouldBeShorterThan(20, NAME),
                s.shouldNotContainWhitespace(NAME),
            ) { _, _, _ -> UserName(s) }
    }
}

internal data class UserAge(val value: Int) {
    internal companion object {
        private const val NAME = "age"

        fun tryFrom(n: Int) =
            Validation.foldToConst(
                Validation.ok(UserAge(n)),
                n.shouldBeGreaterThan(0, NAME),
                n.shouldBeLessThan(150, NAME),
            )
        // Validation.map2(
        //     n.shouldBeGreaterThan(0, NAME),
        //     n.shouldBeLessThan(150, NAME),
        // ) { _, _ -> UserAge(n) }
    }
}

internal data class UserUpdatedAt(val value: Instant) {
    internal companion object {
        private const val NAME = "updated_at"

        fun tryFrom(s: String) =
            s.shouldBeValidTimestamp(NAME).map { UserUpdatedAt(it) }
    }
}

internal fun String.shouldNotBeEmpty(
    name: String = "input"
): Validation<String, FormatError> = when {
    isNotEmpty() -> Validation.ok(this)
    else -> Validation.err(FormatError("$name should not be empty, but it is"))
}

internal fun String.shouldBeLongerThan(
    n: Int,
    name: String = "input"
): Validation<String, FormatError> = when {
    length > n -> Validation.ok(this)
    else -> Validation.err(FormatError("$name should be longer than $n, but its length is $length"))
}

internal fun String.shouldBeShorterThan(
    n: Int,
    name: String = "input"
): Validation<String, FormatError> = when {
    length < n -> Validation.ok(this)
    else -> Validation.err(FormatError("$name should be shorter than $n, but its length is $length"))
}

internal fun String.shouldNotContainWhitespace(
    name: String = "input"
): Validation<String, FormatError> = when {
    contains(Regex("\\s")) -> Validation.err(FormatError("$name should not contain whitespace, but it does"))
    else -> Validation.ok(this)
}

internal fun String.shouldBeValidTimestamp(
    name: String = "input"
): Validation<Instant, FormatError> =
    try {
        Validation.ok(Instant.parse(this))
    } catch (_: DateTimeParseException) {
        Validation.err(FormatError("$name should be a valid timestamp, but it is not"))
    }

internal fun Int.shouldBeLessThan(
    n: Int,
    name: String = "input"
): Validation<Int, FormatError> = when {
    this < n -> Validation.ok(this)
    else -> Validation.err(FormatError("$name should be less than $n, but it is not"))
}

internal fun Int.shouldBeGreaterThan(
    n: Int,
    name: String = "input"
): Validation<Int, FormatError> = when {
    this > n -> Validation.ok(this)
    else -> Validation.err(FormatError("$name should be greater than $n, but it is not"))
}
