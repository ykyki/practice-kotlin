package practice.kotlin.dom.func.ch07.modeling

import arrow.core.NonEmptyList

public sealed class Cart(
    public open val cartId: String,
) {
    public companion object {
        public fun empty(cartId: String): CartEmpty = CartEmpty(cartId)
    }

    public fun totalPrice(): Int = when (this) {
        is CartEmpty -> 0
        is CartActive -> items.sumOf { it.price * it.quantity }
        is CartPaid -> items.sumOf { it.price * it.quantity }
    }

    internal abstract fun prettyString(): String
}

public data class CartEmpty internal constructor(
    override val cartId: String,
) : Cart(
    cartId
) {
    public fun addItem(items: NonEmptyList<Item>): CartActive = CartActive(cartId, items)

    override fun prettyString(): String = "CartEmpty(cartId=$cartId)"
}

public data class CartActive internal constructor(
    override val cartId: String,
    val items: NonEmptyList<Item>,
) : Cart(
    cartId
) {
    override fun prettyString(): String = "CartActive(cartId=$cartId, items=$items)"

    public fun addItem(items: List<Item>): CartActive = CartActive(cartId, this.items + items)

    internal fun flush(): CartEmpty = CartEmpty(cartId)

    public fun pay(): CartPaid = CartPaid(cartId, items)
}

public data class CartPaid internal constructor(
    override val cartId: String,
    val items: List<Item>,
) : Cart(
    cartId
) {
    override fun prettyString(): String = "CartPaid(cartId=$cartId, items=$items)"
}

public data class Item(
    val itemId: String,
    val quantity: Int,
    val price: Int,
)
