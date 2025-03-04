package practice.kotlin.dom.func.ch07.modeling

import arrow.core.Either
import kotlinx.coroutines.Deferred
import java.time.OffsetDateTime

// ---------
// Util
// ---------

typealias AsyncResult<L, R> = Deferred<Either<L, R>>

// ---------
// Input Data
// ---------

data class UnvalidatedOrder(
    val orderId: OrderId,
    val customerInfo: UnvalidatedCustomer,
    val shippingAddress: UnvalidatedAddress,
)

data class UnvalidatedCustomer(
    val name: String,
    val email: String,
)

interface UnvalidatedAddress // TODO

// ---------
// Input Command
// ---------

data class Command<A>(
    val data: A,
    val timestamp: OffsetDateTime,
    val userId: String,
)

typealias PlaceOrderCommand = Command<UnvalidatedOrder>

// ---------
// Public Api
// ---------

@JvmInline
value class OrderPlaced(val price: PricedOrder)

data class BillableOrderPlaced(
    val orderId: OrderId,
    val address: Address,
    val billingAmount: BillingAmount,
)

data class OrderAcknowledgementSent(
    val orderId: OrderId,
    val emailAddress: EmailAddress,
)

sealed class PlaceOrderEvent {
    data class OrderPlaced(val orderPlaced: OrderPlaced) : PlaceOrderEvent()
    data class BillableOrderPlaced(val billableOrderPlaced: BillableOrderPlaced) : PlaceOrderEvent()
    data class AcknowledgementSent(val orderAcknowledgementSent: OrderAcknowledgementSent) : PlaceOrderEvent()
}

interface PlaceOrderError

typealias PlaceOrderWorkflow = (PlaceOrderCommand)
-> AsyncResult<PlaceOrderError, List<PlaceOrderEvent>>

// ---------
// Lifecycle of Order
// ---------

sealed class Order {
    data class Unvalidated(val unvalidatedOrder: UnvalidatedOrder) : Order()
    data class Validated(val validatedOrder: ValidatedOrder) : Order()
    data class Priced(val pricedOrder: PricedOrder) : Order()
}

data class ValidatedOrder(
    val orderId: OrderId,
    val customerInfo: CustomerInfo,
    val shippingAddress: ValidatedAddress,
    val orderLines: List<ValidatedOrderLine>,
)

interface OrderId
interface CustomerInfo
interface Address
typealias ValidatedOrderLine = Nothing // TODO

interface PricedOrder
interface BillingAmount

// ---------
// Type definitions of internal steps
// ---------

// --------- verify order

typealias ValidateOrder = (CheckProductCodeExists)
-> (CheckAddressExists)
-> (UnvalidatedOrder)
-> AsyncResult<List<ValidationError>, ValidatedOrder>

typealias CheckProductCodeExists = (ProductCode) -> AsyncResult<Unit, Boolean>

typealias CheckAddressExists = (UnvalidatedAddress)
-> AsyncResult<AddressValidationError, ValidatedAddress>

interface ValidationError

@JvmInline
value class ValidatedAddress(val address: UnvalidatedAddress)

interface ProductCode

@JvmInline
value class AddressValidationError(val message: String)

// --------- price order

typealias PriceOrder = (GetProductPrice)
-> (ValidatedOrder)
-> Either<PricingError, PricedOrder>

typealias GetProductPrice = (ProductCode)
-> Price

interface Price

@JvmInline
value class PricingError(val message: String)

// --------- create order acknowledgment

typealias AcknowledgeOrder = (CreateOrderAcknowledgmentLetter)
-> (SendOrderAcknowledgment)
-> (PricedOrder)
-> Deferred<OrderAcknowledgementSent?>

typealias CreateOrderAcknowledgmentLetter = (PricedOrder)
-> HtmlString

sealed class SendResult {
    object Success : SendResult()
    object Failure : SendResult()
}
typealias SendOrderAcknowledgment = (OrderAcknowledgment)
-> Deferred<SendResult>

data class OrderAcknowledgment(
    val emailAddress: EmailAddress,
    val letter: HtmlString,
)

@JvmInline
value class HtmlString(val value: String)
interface EmailAddress

// --------- create events

typealias CreateEvents = (PricedOrder)
-> List<PlaceOrderEvent>
