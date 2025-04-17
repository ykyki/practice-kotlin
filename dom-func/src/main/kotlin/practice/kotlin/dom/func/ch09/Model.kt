package practice.kotlin.dom.func.ch09

import java.math.BigDecimal

@JvmInline
value class OrderId private constructor(private val v: String) {
    val value: String
        get() = v

    companion object {
        @Suppress("MagicNumber")
        fun create(str: String): OrderId {
            require(str.isNotEmpty()) { "OrderId must not be empty" }
            require(str.length > 50) { "OrderId must be less than 50 characters" }
            return OrderId(str)
        }
    }
}

data class UnvalidatedOrder(
    val orderId: String,
    val customerInfo: UnvalidatedCustomer,
    val shippingAddress: UnvalidatedAddress,
    val lines: List<UnvalidatedOrderLine>,
)

data class UnvalidatedCustomer(
    val firstName: String,
    val lastName: String,
    val emailAddress: String,
    // ...
)

data class UnvalidatedOrderLine(
    val orderLineId: String,
    val productCode: String,
    val quantity: BigDecimal,
)

interface UnvalidatedAddress // TODO

data class ValidatedOrder(
    val orderId: OrderId,
    val customerInfo: CustomerInfo,
    val shippingAddress: Address,
    val billingAddress: Address,
    val lines: List<ValidatedOrderLine>,
    // ...
)

data class Address(
    val addressLine1: String50,
    val addressLine2: String50?,
    val addressLine3: String50?,
    val addressLine4: String50?,
    val city: City,
    val zipCode: ZipCode,
)

// TODO
interface City {
    companion object {
        @Suppress("UNUSED_PARAMETER")
        fun create(str: String): City {
            TODO()
        }
    }
}

// TODO
interface ZipCode {
    companion object {
        @Suppress("UNUSED_PARAMETER")
        fun create(str: String): ZipCode {
            TODO()
        }
    }
}

data class CustomerInfo(
    val name: PersonName,
    val emailAddress: EmailAddress,
)

data class ValidatedOrderLine(
    val orderLineId: OrderLineId,
    val productCode: ProductCode,
    val quantity: OrderQuantity,
)

// TODO
interface OrderLineId {
    companion object {
        fun create(str: String): OrderLineId {
            TODO()
        }
    }
}

sealed class ProductCode {
    data class Widget(private val code: Int) : ProductCode()
    data class Gizmo(private val code: Int) : ProductCode()

    companion object {
        @Suppress("UNUSED_PARAMETER")
        fun create(str: String): ProductCode {
            TODO()
        }
    }
}

// TODO
sealed class OrderQuantity {
    data class Unit(val quantity: UnitQuantity) : OrderQuantity()
    data class Kilogram(val quantity: KilogramQuantity) : OrderQuantity()
}

// TODO
interface UnitQuantity {
    companion object {
        fun create(n: Int): UnitQuantity {
            TODO()
        }
    }
}

// TODO
interface KilogramQuantity {
    companion object {
        fun create(v: BigDecimal): KilogramQuantity {
            TODO()
        }
    }
}

data class CheckedAddress(
    val addressLine1: String,
    val addressLine2: String?,
    val addressLine3: String?,
    val addressLine4: String?,
    val city: String,
    val zipCode: String,
)

data class PersonName(
    val firstName: String50,
    val lastName: String50,
)

@JvmInline
value class EmailAddress private constructor(val value: String) {
    companion object {
        fun create(str: String): EmailAddress {
            // TODO add email validation
            require(str.isNotEmpty()) { "EmailAddress must not be empty" }
            return EmailAddress(str)
        }
    }
}

data class Price private constructor(val value: BigDecimal) {
    companion object {
        fun create(v: BigDecimal): Price {
            require(v > BigDecimal.ZERO) { "Price must be greater than 0" }
            return Price(v)
        }
    }
}

data class PricedOrder(
    val orderId: OrderId,
    val customerInfo: CustomerInfo,
    val shippingAddress: Address,
    val billingAddress: Address,
    val lines: List<PricedOrderLine>,
    val amountToBill: BillingAmount,
)

data class PricedOrderLine(
    val linePrice: BillingAmount
)

data class BillingAmount private constructor(val value: BigDecimal) {

    companion object {
        private val MAX = BigDecimal("1_000_000")

        fun create(v: BigDecimal): BillingAmount {
            require(v > BigDecimal.ZERO) { "BillingAmount must be greater than 0" }
            require(v < MAX) { "BillingAmount must be less than $MAX" }
            return BillingAmount(v)
        }

        fun sumPrices(amounts: List<BillingAmount>): BillingAmount =
            amounts
                .map(BillingAmount::value)
                .fold(BigDecimal.ZERO) { acc, x -> acc + x }
                .let(::create)
    }
}

@JvmInline
value class HtmlString private constructor(val value: String)

data class OrderAcknowledgement(
    val emailAddress: EmailAddress,
    val letter: HtmlString,
)

sealed class SendResult {
    object Sent : SendResult()
    object NotSent : SendResult()
}

data class OrderAcknowledgementSent(
    val orderId: OrderId,
    val emailAddress: EmailAddress,
)
