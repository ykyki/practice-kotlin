package practice.kotlin.dom.func.ch09.funtion

import practice.kotlin.dom.func.ch09.Address
import practice.kotlin.dom.func.ch09.CheckAddressExists
import practice.kotlin.dom.func.ch09.CheckProductCodeExists
import practice.kotlin.dom.func.ch09.City
import practice.kotlin.dom.func.ch09.CustomerInfo
import practice.kotlin.dom.func.ch09.EmailAddress
import practice.kotlin.dom.func.ch09.KilogramQuantity
import practice.kotlin.dom.func.ch09.OrderId
import practice.kotlin.dom.func.ch09.OrderLineId
import practice.kotlin.dom.func.ch09.OrderQuantity
import practice.kotlin.dom.func.ch09.PersonName
import practice.kotlin.dom.func.ch09.ProductCode
import practice.kotlin.dom.func.ch09.String50
import practice.kotlin.dom.func.ch09.UnitQuantity
import practice.kotlin.dom.func.ch09.UnvalidatedAddress
import practice.kotlin.dom.func.ch09.UnvalidatedCustomer
import practice.kotlin.dom.func.ch09.UnvalidatedOrder
import practice.kotlin.dom.func.ch09.UnvalidatedOrderLine
import practice.kotlin.dom.func.ch09.ValidateOrder
import practice.kotlin.dom.func.ch09.ValidateOrderFun
import practice.kotlin.dom.func.ch09.ValidatedOrder
import practice.kotlin.dom.func.ch09.ValidatedOrderLine
import practice.kotlin.dom.func.ch09.ZipCode
import practice.kotlin.dom.func.ch09.predicateToPassthru
import practice.kotlin.lib.curry.curry
import java.math.BigDecimal

/**
 * 本に合わせて変数として定義するならこっち
 */
val validateOrderFun: ValidateOrderFun =
    curry {
            checkProductCodeExists: CheckProductCodeExists,
            checkAddressExists: CheckAddressExists,
            unvalidatedOrder: UnvalidatedOrder,
        ->
        val orderId: OrderId =
            unvalidatedOrder.orderId
                .let(OrderId::create)

        val customerInfo =
            unvalidatedOrder.customerInfo
                .let(::toCustomerInfo)

        val shippingAddress =
            unvalidatedOrder.shippingAddress
                .let(curry(::toAddress)(checkAddressExists)) // 部分適用っぽく書くなら
        // .let { toAddress(checkAddressExists, it) } // Kotlinっぽく単純に書くなら

        val billingAddress =
            unvalidatedOrder.customerInfo
                .let(::toBillingAddress)

        val lines =
            unvalidatedOrder.lines
                .map { it.toValidatedOrderLine(checkProductCodeExists) }

        ValidatedOrder(
            orderId,
            customerInfo,
            shippingAddress,
            billingAddress,
            lines,
        )
    }

/**
 * Spring DI Containerを使う実用的な実装はこっちになりそう
 */
// @Service
class ValidateOrderImpl : ValidateOrder {
    // 言語仕様上しかたないが, 書き方がちょっとぎこちない
    override fun invoke(
        checkProductCodeExists: CheckProductCodeExists
    ): (CheckAddressExists) -> (UnvalidatedOrder) -> ValidatedOrder = curry {
            checkAddressExists: CheckAddressExists,
            unvalidatedOrder: UnvalidatedOrder,
        ->
        unvalidatedOrder.run {
            ValidatedOrder(
                orderId = orderId.let(OrderId::create),
                customerInfo = customerInfo.let(::toCustomerInfo),
                shippingAddress = shippingAddress.let(curry(::toAddress)(checkAddressExists)),
                billingAddress = customerInfo.let(::toBillingAddress),
                lines = lines.map { it.toValidatedOrderLine(checkProductCodeExists) },
            )
        }
    }
}

private fun toCustomerInfo(
    customer: UnvalidatedCustomer,
): CustomerInfo {
    val firstName = customer.firstName.let(String50.Companion::create)
    val lastName = customer.lastName.let(String50.Companion::create)
    val emailAddress = customer.emailAddress.let(EmailAddress.Companion::create)

    val name = PersonName(firstName, lastName)

    return CustomerInfo(name, emailAddress)
}

@Suppress("UNUSED_PARAMETER")
private fun toAddress(
    checkAddressExists: CheckAddressExists,
    address: UnvalidatedAddress,
): Address {
    val checkedAddress = checkAddressExists(address)

    return checkedAddress.run {
        Address(
            addressLine1 = addressLine1.let(String50::create),
            addressLine2 = addressLine2?.let(String50::createOption),
            addressLine3 = addressLine3?.let(String50::createOption),
            addressLine4 = addressLine4?.let(String50::createOption),
            city = city.let(City::create),
            zipCode = zipCode.let(ZipCode::create),
        )
    }
}

private fun UnvalidatedOrderLine.toValidatedOrderLine(
    checkProductCodeExists: CheckProductCodeExists,
): ValidatedOrderLine {
    val productCode = productCode.let(curry(::toProduceCode)(checkProductCodeExists))

    return ValidatedOrderLine(
        orderLineId = orderLineId.let(OrderLineId::create),
        productCode = productCode,
        quantity = quantity.let(curry(::toOrderQuantity)(productCode)),
    )
}

@Suppress("UNUSED_PARAMETER")
private fun toProduceCode(
    checkProductCodeExists: CheckProductCodeExists,
    productCode: String,
): ProductCode {
    val checkProduct = { p: ProductCode ->
        predicateToPassthru(
            errorMsg = "Invalid product code: $p",
            f = checkProductCodeExists,
            x = p,
        )
    }

    return productCode
        .let(ProductCode::create)
        .let(checkProduct)
    // .also { // also を使えば簡単にKotlinっぽく書ける
    //     require(checkProductCodeExists(it)) { "ProductCode is not right." }
    // }
}

@Suppress("UNUSED_PARAMETER")
private fun toOrderQuantity(
    productCode: ProductCode,
    quantity: BigDecimal,
): OrderQuantity = when (productCode) {
    is ProductCode.Widget ->
        quantity
            .let(BigDecimal::toInt)
            .let(UnitQuantity::create)
            .let(OrderQuantity::Unit)

    is ProductCode.Gizmo ->
        quantity
            .let(KilogramQuantity::create)
            .let(OrderQuantity::Kilogram)
}

@Suppress("UNUSED_PARAMETER")
private fun toBillingAddress(
    customer: UnvalidatedCustomer,
): Address {
    TODO()
}
