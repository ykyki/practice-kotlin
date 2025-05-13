@file:Suppress("MagicNumber")

package practice.kotlin.dom.func.ch09

/**
 * 設計方針
 * placeOrder(unvalidatedOrder) =
 *     unvalidatedOrder
 *     |> validate
 *     |> priceOrder
 *     |> acknowledgeOrder
 *     |> createEvents
 *
 * 注意: この章では Result や Async を一旦無視する
 * エラーハンドリングも一切なし
 */

// --------- place order ---------
typealias PlaceOrderFun = (ValidateOrder)
-> (PriceOrder)
-> (AcknowledgeOrder)
-> (CreateEvents)
-> (UnvalidatedOrder)
-> List<PlaceOrderEvent>

typealias PlaceOrderWorkflowFun = (CheckAddressExists) // ambient dependencies
-> (CheckProductCodeExists)
-> (GetProductPrice)
-> (CreateOrderAcknowledgementLetter)
-> (SendOrderAcknowledgement)
-> PlaceOrderFun

fun interface PlaceOrderWorkflow : PlaceOrderWorkflowFun

// --------- validate ---------

typealias ValidateOrderFun = (CheckProductCodeExists)
-> (CheckAddressExists)
-> (UnvalidatedOrder)
-> ValidatedOrder

fun interface ValidateOrder : ValidateOrderFun

typealias CheckAddressExistsFun = (UnvalidatedAddress) -> CheckedAddress

fun interface CheckAddressExists : CheckAddressExistsFun

typealias CheckProductCodeExistsFun = (ProductCode) -> Boolean

fun interface CheckProductCodeExists : CheckProductCodeExistsFun

// --------- price order ---------

typealias PriceOrderFun = (GetProductPrice) -> (ValidatedOrder) -> PricedOrder

fun interface PriceOrder : PriceOrderFun

typealias GetProductPriceFun = (ProductCode) -> Price

fun interface GetProductPrice : GetProductPriceFun

// --------- acknowledge order ---------

typealias AcknowledgeOrderFun = (CreateOrderAcknowledgementLetter)
-> (SendOrderAcknowledgement)
-> (PricedOrder)
-> OrderAcknowledgementSent?

fun interface AcknowledgeOrder : AcknowledgeOrderFun

typealias CreateOrderAcknowledgementLetterFun = (PricedOrder) -> HtmlString

fun interface CreateOrderAcknowledgementLetter : CreateOrderAcknowledgementLetterFun

typealias SendOrderAcknowledgementFun = (OrderAcknowledgement) -> SendResult

fun interface SendOrderAcknowledgement : SendOrderAcknowledgementFun

// -------- create events ---------

typealias CreateEventsFun = (PricedOrder) // 入力
-> (OrderAcknowledgementSent?) // 前ステップのイベント
-> List<PlaceOrderEvent>

fun interface CreateEvents : CreateEventsFun

sealed class PlaceOrderEvent {
    // 配送コンテキストに送信するイベント
    data class OrderPlaced(val pricedOrder: PricedOrder) : PlaceOrderEvent()

    // 請求コンテキストに送信するイベント
    // 請求総額が0でない場合にのみ送信する
    data class BillableOrderPlaced(
        val orderId: OrderId,
        val billingAddress: Address,
        val amountToBill: BillingAmount,
    ) : PlaceOrderEvent()

    // 注文確認を送ったイベント
    data class AcknowledgementSent(
        val orderAcknowledgementSent: OrderAcknowledgementSent,
    ) : PlaceOrderEvent()
}
