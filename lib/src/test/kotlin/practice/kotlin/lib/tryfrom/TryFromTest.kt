package practice.kotlin.lib.tryfrom

import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.email
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class TryFromTest : FunSpec({
    context("tryFrom returns expected ok/err") {
        withData(
            "email@google.com" to true,
            " email@google.com " to false,
            "foo" to false,
            "foo@bar.co.jp" to true,
            "foo@bar.co.jp@baz" to false,
            "" to false,
        ) { (input, expected) ->
            // when
            val result = input.tryInto(Email)

            // then
            result.isOk shouldBe expected
        }
    }

    test("tryFrom returns Ok when the input is a valid email") {
        checkAll(Arb.email()) { email ->
            // when
            val result = Email.tryFrom(email)

            // then
            result.isOk shouldBe true
        }
    }

    xtest("tryFrom returns Err for almost all string") {
        @OptIn(ExperimentalKotest::class)
        checkAll(
            PropTestConfig(maxFailure = 20, iterations = 1_000),
            Arb.string()
        ) { str ->
            // when
            val result = Email.tryFrom(str)

            // then
            result.isErr shouldBe true
        }
    }

    context("tryFrom returns Err when") {
        test("the input contains no '@'") {
            checkAll(Arb.string().filter { !it.contains("@") }) { str ->
                // when
                val result = str.tryInto(Email)

                // then
                result.isErr shouldBe true
            }
        }

        test("the input contains a space") {
            checkAll(Arb.string().filter { it.contains(" ") }) { str ->
                // when
                val result = str.tryInto(Email)

                // then
                result.isErr shouldBe true
            }
        }
    }
})

