package practice.kotlin.dom.func.ch07.modeling

import arrow.core.NonEmptyList

sealed class Cart protected constructor(
    open val cartId: String,
) {
    companion object {
        fun empty(cartId: String): CartEmpty = CartEmpty(cartId)
    }

    fun totalPrice(): Int = when (this) {
        is CartEmpty -> 0
        is CartActive -> items.sumOf { it.price * it.quantity }
        is CartPaid -> items.sumOf { it.price * it.quantity }
    }

    abstract fun prettyString(): String
}

data class CartEmpty internal constructor(
    override val cartId: String,
) : Cart(
    cartId
) {
    fun addItem(items: NonEmptyList<Item>): CartActive = CartActive(cartId, items)

    override fun prettyString(): String = "CartEmpty(cartId=$cartId)"
}

data class CartActive internal constructor(
    override val cartId: String,
    val items: NonEmptyList<Item>,
) : Cart(
    cartId
) {
    override fun prettyString(): String = "CartActive(cartId=$cartId, items=$items)"

    fun addItem(items: List<Item>): CartActive = CartActive(cartId, this.items + items)

    fun flush(): CartEmpty = CartEmpty(cartId)

    fun pay(): CartPaid = CartPaid(cartId, items)
}

data class CartPaid internal constructor(
    override val cartId: String,
    val items: List<Item>,
) : Cart(
    cartId
) {
    override fun prettyString(): String = "CartPaid(cartId=$cartId, items=$items)"
}

data class Item(
    val itemId: String,
    val quantity: Int,
    val price: Int,
)
