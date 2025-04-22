package practice.kotlin.dom.func.ch09.workflow

import practice.kotlin.dom.func.ch09.AcknowledgeOrder
import practice.kotlin.dom.func.ch09.CheckAddressExists
import practice.kotlin.dom.func.ch09.CheckProductCodeExists
import practice.kotlin.dom.func.ch09.CreateEvents
import practice.kotlin.dom.func.ch09.CreateOrderAcknowledgementLetter
import practice.kotlin.dom.func.ch09.GetProductPrice
import practice.kotlin.dom.func.ch09.PlaceOrderFun
import practice.kotlin.dom.func.ch09.PlaceOrderWorkflow
import practice.kotlin.dom.func.ch09.PriceOrder
import practice.kotlin.dom.func.ch09.SendOrderAcknowledgement
import practice.kotlin.dom.func.ch09.UnvalidatedOrder
import practice.kotlin.dom.func.ch09.ValidateOrder
import practice.kotlin.lib.curry.curry

class PlaceOrderWorkflowImpl : PlaceOrderWorkflow {
    override fun invoke(
        checkAddressExists: CheckAddressExists
    ): (CheckProductCodeExists)
    -> (GetProductPrice)
    -> (CreateOrderAcknowledgementLetter)
    -> (SendOrderAcknowledgement)
    -> PlaceOrderFun = curry {
            checkProductCodeExists: CheckProductCodeExists,
            getProductPrice: GetProductPrice,
            createOrderAcknowledgementLetter: CreateOrderAcknowledgementLetter,
            sendOrderAcknowledgement: SendOrderAcknowledgement,
        ->
        curry {
                validateOrder: ValidateOrder,
                priceOrder: PriceOrder,
                acknowledgeOrder: AcknowledgeOrder,
                createEvents: CreateEvents,
            ->
            {
                    unvalidatedOrder: UnvalidatedOrder,
                ->
                val pricedOrder =
                    unvalidatedOrder
                        .let(validateOrder(checkProductCodeExists)(checkAddressExists))
                        .let(priceOrder(getProductPrice))

                val orderAcknowledgement =
                    pricedOrder
                        .let(acknowledgeOrder(createOrderAcknowledgementLetter)(sendOrderAcknowledgement))

                val events = createEvents(pricedOrder)(orderAcknowledgement)

                events
            }
        }
    }
}
