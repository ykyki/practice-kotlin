package practice.kotlin.dom.func.ch07.modeling

import arrow.core.nonEmptyListOf
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class CartTest : FunSpec({
    test("totalPrice") {
        // given
        val cart = Cart
            .empty("C001")
            .also { println(it.prettyString()) }
            .addItem(
                nonEmptyListOf(
                    Item("I001", 1, 100),
                    Item("I002", 2, 200),
                )
            )
            .also { println(it.prettyString()) }
            .flush()
            .also { println(it.prettyString()) }
            .addItem(
                nonEmptyListOf(
                    Item("I003", 3, 300),
                )
            )
            .also { println(it.prettyString()) }
            .addItem(
                nonEmptyListOf(
                    Item("I004", 4, 400),
                )
            )
            .also { println(it.prettyString()) }
            .pay()
            .also { println(it.prettyString()) }

        // when
        val totalPrice = cart.totalPrice()

        // then
        totalPrice shouldBe 2500
    }
})