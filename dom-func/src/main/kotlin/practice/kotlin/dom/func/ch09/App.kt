package practice.kotlin.dom.func.ch09

import practice.kotlin.dom.func.ch09.funtion.acknowledgeOrderFun
import practice.kotlin.dom.func.ch09.funtion.createEventsFun
import practice.kotlin.dom.func.ch09.funtion.priceOrderFun
import practice.kotlin.dom.func.ch09.funtion.validateOrderFun
import practice.kotlin.dom.func.ch09.workflow.PlaceOrderWorkflowImpl
import java.math.BigDecimal

fun main() {
    val unvalidatedOrder = UnvalidatedOrder(
        orderId = "12345",
        customerInfo = UnvalidatedCustomer(
            firstName = "John",
            lastName = "Doe",
            emailAddress = ""
        ),
        shippingAddress = object : UnvalidatedAddress {},
        lines = listOf(
            UnvalidatedOrderLine(
                orderLineId = "67890",
                productCode = "ABC123",
                quantity = BigDecimal(2)
            ),
            UnvalidatedOrderLine(
                orderLineId = "67891",
                productCode = "XYZ789",
                quantity = BigDecimal(1)
            )
        ),
    )

    val events = placeOrderWorkflow(unvalidatedOrder)

    println("Events: $events")
}

val checkProductCodeExists: CheckProductCodeExists = TODO()
val checkAddressExists: CheckAddressExists = TODO()
val getProductPrice: GetProductPrice = TODO()
val createOrderAcknowledgementLetter: CreateOrderAcknowledgementLetter = TODO()
val sendOrderAcknowledgement: SendOrderAcknowledgement = TODO()

val placeOrderWorkflow: (UnvalidatedOrder) -> List<PlaceOrderEvent> = { unvalidatedOrder ->
    val placeOrderFun: PlaceOrderFun =
        PlaceOrderWorkflowImpl().invoke(
            checkAddressExists,
        )(
            checkProductCodeExists,
        )(
            getProductPrice,
        )(
            createOrderAcknowledgementLetter,
        )(
            sendOrderAcknowledgement,
        )

    val events = placeOrderFun(
        validateOrderFun
        // ValidateOrderImpl()
    )(
        priceOrderFun
        // PricedOrderImpl()
    )(
        acknowledgeOrderFun
        // AcknowledgeOrderImpl()
    )(
        createEventsFun
        // CreateEventsImpl()
    )(
        unvalidatedOrder,
    )

    events
}
