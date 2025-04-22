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

interface PlaceOrderWorkflow : PlaceOrderWorkflowFun

// --------- validate ---------

typealias ValidateOrderFun = (CheckProductCodeExists)
-> (CheckAddressExists)
-> (UnvalidatedOrder)
-> ValidatedOrder

interface ValidateOrder : ValidateOrderFun

typealias CheckAddressExistsFun = (UnvalidatedAddress) -> CheckedAddress

interface CheckAddressExists : CheckAddressExistsFun

typealias CheckProductCodeExistsFun = (ProductCode) -> Boolean

interface CheckProductCodeExists : CheckProductCodeExistsFun

// --------- price order ---------

typealias PriceOrderFun = (GetProductPrice) -> (ValidatedOrder) -> PricedOrder

interface PriceOrder : PriceOrderFun

typealias GetProductPriceFun = (ProductCode) -> Price

interface GetProductPrice : GetProductPriceFun

// --------- acknowledge order ---------

typealias AcknowledgeOrderFun = (CreateOrderAcknowledgementLetter)
-> (SendOrderAcknowledgement)
-> (PricedOrder)
-> OrderAcknowledgementSent?

interface AcknowledgeOrder : AcknowledgeOrderFun

typealias CreateOrderAcknowledgementLetterFun = (PricedOrder) -> HtmlString

interface CreateOrderAcknowledgementLetter : CreateOrderAcknowledgementLetterFun

typealias SendOrderAcknowledgementFun = (OrderAcknowledgement) -> SendResult

interface SendOrderAcknowledgement : SendOrderAcknowledgementFun

// -------- create events ---------

typealias CreateEventsFun = (PricedOrder) // 入力
-> (OrderAcknowledgementSent?) // 前ステップのイベント
-> List<PlaceOrderEvent>

interface CreateEvents : CreateEventsFun

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
