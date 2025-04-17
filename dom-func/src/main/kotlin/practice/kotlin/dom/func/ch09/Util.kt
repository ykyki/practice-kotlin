package practice.kotlin.dom.func.ch09

fun <T> predicateToPassthru(
    errorMsg: String,
    f: (T) -> Boolean,
    x: T
) =
    if (f(x)) {
        x
    } else {
        throw IllegalArgumentException(errorMsg)
    }

@JvmInline
value class String50 private constructor(val value: String) {
    companion object {
        @Suppress("MagicNumber")
        fun create(str: String): String50 {
            require(str.length <= 50) { "String50 must be less than 50 characters" }
            return String50(str)
        }

        fun createOption(str: String): String50? {
            return if (str.isNotEmpty()) {
                create(str)
            } else {
                null
            }
        }
    }
}
