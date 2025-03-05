package practice.kotlin.dom.func

import io.kotest.core.spec.style.FunSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll
import io.mockk.mockk
import io.mockk.verify

class HelloTest : FunSpec({
    test("sayHello with default printer") {
        Hello.sayHello("Kotlin", 3) {
            print(it)
        }
    }

    test("sayHello calls printer n times") {
        checkAll(Arb.int(0..10)) { n ->
            // given
            val name = "Kotlin"
            val printerMock = mockk<(String) -> Unit>(relaxed = true)
            println("n = $n")

            // when
            Hello.sayHello(name, n, printerMock)

            // then
            verify(exactly = n) { printerMock("Hello, $name!\n") }
        }
    }
})
