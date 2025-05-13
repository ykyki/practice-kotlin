package practice.kotlin.dom.func.ch09.funtion

import practice.kotlin.dom.func.ch09.BillingAmount
import practice.kotlin.dom.func.ch09.GetProductPrice
import practice.kotlin.dom.func.ch09.PriceOrder
import practice.kotlin.dom.func.ch09.PriceOrderFun
import practice.kotlin.dom.func.ch09.PricedOrder
import practice.kotlin.dom.func.ch09.PricedOrderLine
import practice.kotlin.dom.func.ch09.ValidatedOrder
import practice.kotlin.dom.func.ch09.ValidatedOrderLine

val priceOrderFun: PriceOrderFun = PricedOrderImpl()

class PricedOrderImpl : PriceOrder {
    override fun invoke(getProductPrice: GetProductPrice): (ValidatedOrder) -> PricedOrder = {
            validatedOrder: ValidatedOrder,
        ->
        val lines =
            validatedOrder.lines
                .map { it.toPricedOrderLine(getProductPrice) }

        val amountToBill =
            lines
                .map(PricedOrderLine::linePrice)
                .let(BillingAmount::sumPrices)

        PricedOrder(
            orderId = validatedOrder.orderId,
            customerInfo = validatedOrder.customerInfo,
            shippingAddress = validatedOrder.shippingAddress,
            billingAddress = validatedOrder.billingAddress,
            lines = lines,
            amountToBill = amountToBill,
        )
    }
}

@Suppress("UNUSED_PARAMETER")
fun ValidatedOrderLine.toPricedOrderLine(getProductPrice: GetProductPrice): PricedOrderLine {
    TODO()
}
