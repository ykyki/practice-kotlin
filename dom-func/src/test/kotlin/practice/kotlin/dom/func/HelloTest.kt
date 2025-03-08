package practice.kotlin.dom.func

import io.kotest.core.spec.style.FunSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify

class HelloTest : FunSpec({
    test("sayHello with default printer") {
        Hello.sayHello("Kotlin", 3) {
            print(it)
        }
    }

    test("sayHello calls printer n times when n is non-negative") {
        val printer = mockk<(String) -> Unit>()

        checkAll(
            20, // reduce the number of iterations to avoid slow test
            Arb.int(0, 1000), // too large n may cause a stack overflow because of mockk
            Arb.string(),
        ) { n, s ->
            // given
            every { printer.invoke(any()) } just Runs

            // when
            println("n = $n, s = $s")
            Hello.sayHello(s, n, printer)

            // then
            verify(exactly = n) { printer("Hello, $s!\n") }
            verify(exactly = n) { printer(any()) }

            // cleanup
            clearMocks(printer)
        }
    }

    test("sayHello does not call printer when n is negative") {
        checkAll(Arb.int(Int.MIN_VALUE, -1), Arb.string()) { n, s ->
            // given
            val printer = mockk<(String) -> Unit>()
            every { printer.invoke(any()) } just Runs

            // when
            println("n = $n, s = $s")
            Hello.sayHello(s, n, printer)

            // then
            verify(exactly = 0) { printer(any()) }
        }
    }
})
