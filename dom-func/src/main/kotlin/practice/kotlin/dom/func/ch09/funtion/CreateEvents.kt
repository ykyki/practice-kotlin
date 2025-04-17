package practice.kotlin.dom.func.ch09.funtion

import practice.kotlin.dom.func.ch09.CreateEvents
import practice.kotlin.dom.func.ch09.OrderAcknowledgementSent
import practice.kotlin.dom.func.ch09.PlaceOrderEvent
import practice.kotlin.dom.func.ch09.PricedOrder
import java.math.BigDecimal

val createEvents: CreateEvents = CreateEventsImpl()

class CreateEventsImpl : CreateEvents {
    override fun invoke(
        pricedOrder: PricedOrder,
    ): (OrderAcknowledgementSent?) -> List<PlaceOrderEvent> = {
            orderAcknowledgementSent: OrderAcknowledgementSent?,
        ->
        val event1 = pricedOrder.run(PlaceOrderEvent::OrderPlaced)

        val event2Opt =
            orderAcknowledgementSent
                ?.run(PlaceOrderEvent::AcknowledgementSent)

        val event3Opt = pricedOrder.toBillableOrderPlaced()

        listOfNotNull(
            event1,
            event2Opt,
            event3Opt,
        )
    }
}

private fun PricedOrder.toBillableOrderPlaced(): PlaceOrderEvent.BillableOrderPlaced? =
    if (amountToBill.value > BigDecimal.ZERO) {
        PlaceOrderEvent.BillableOrderPlaced(
            orderId = orderId,
            billingAddress = billingAddress,
            amountToBill = amountToBill,
        )
    } else {
        null
    }
