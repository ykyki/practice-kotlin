package practice.kotlin.dom.func.ch09.funtion

import practice.kotlin.dom.func.ch09.AcknowledgeOrder
import practice.kotlin.dom.func.ch09.CreateOrderAcknowledgementLetter
import practice.kotlin.dom.func.ch09.OrderAcknowledgement
import practice.kotlin.dom.func.ch09.OrderAcknowledgementSent
import practice.kotlin.dom.func.ch09.PricedOrder
import practice.kotlin.dom.func.ch09.SendOrderAcknowledgement
import practice.kotlin.dom.func.ch09.SendResult
import practice.kotlin.lib.curry.curry

val acknowledgeOrderFun: AcknowledgeOrder = AcknowledgeOrderImpl()

class AcknowledgeOrderImpl : AcknowledgeOrder {
    override fun invoke(
        createOrderAcknowledgementLetter: CreateOrderAcknowledgementLetter
    ): (SendOrderAcknowledgement) -> (PricedOrder) -> OrderAcknowledgementSent? = curry {
            sendOrderAcknowledgement: SendOrderAcknowledgement,
            pricedOrder: PricedOrder,
        ->
        val letter = createOrderAcknowledgementLetter(pricedOrder)

        val acknowledgement = OrderAcknowledgement(
            emailAddress = pricedOrder.customerInfo.emailAddress,
            letter = letter,
        )

        when (sendOrderAcknowledgement(acknowledgement)) {
            SendResult.Sent -> OrderAcknowledgementSent(
                orderId = pricedOrder.orderId,
                emailAddress = pricedOrder.customerInfo.emailAddress,
            )

            SendResult.NotSent -> null
        }
    }
}
