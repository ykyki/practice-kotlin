package practice.kotlin.lib.tryfrom

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import java.util.regex.Pattern

public interface TryFrom<Self, in From, out E> {
    public fun tryFrom(from: From): Result<Self, E>
}

public infix fun <Self, From, E> From.tryInto(v: TryFrom<Self, From, E>) = v.tryFrom(this)

@JvmInline
internal value class ErrorString(val value: String)

internal class Email(
    val value: String,
) {
    companion object : TryFrom<Email, String, ErrorString> {
        @Suppress("MaxLineLength")
        private val EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$"
        )

        override fun tryFrom(from: String): Result<Email, ErrorString> = when {
            EMAIL_PATTERN.matcher(from).matches() -> Ok(Email(from))
            else -> Err(ErrorString("Invalid email"))
        }
    }
}

// Example usage
@Suppress("unused")
private val e1 = Email.tryFrom("foo")

@Suppress("unused")
private val e2 = "foo".tryInto(Email)
