@file:Suppress("unused")

package practice.kotlin.dom.func.ch07.modeling

import arrow.core.Either
import kotlinx.coroutines.Deferred
import java.time.OffsetDateTime

// ---------
// Util
// ---------

internal typealias AsyncResult<L, R> = Deferred<Either<L, R>>

internal data class Command<A>(
    val data: A,
    val timestamp: OffsetDateTime,
    val userId: String,
)

// ---------
// Input Port
// ---------

public data class UnvalidatedOrder(
    val orderId: OrderId,
    val customerInfo: UnvalidatedCustomer,
    val shippingAddress: UnvalidatedAddress,
)

public data class UnvalidatedCustomer(
    val name: String,
    val email: String,
)

public interface UnvalidatedAddress // TODO

// ---------
// Workflow
// ---------

internal typealias PlaceOrderWorkflow = (PlaceOrderCommand)
-> AsyncResult<PlaceOrderError, List<PlaceOrderEvent>>

internal typealias PlaceOrderCommand = Command<UnvalidatedOrder>

@JvmInline
internal value class OrderPlaced(
    val price: PricedOrder,
)

internal data class BillableOrderPlaced(
    val orderId: OrderId,
    val address: Address,
    val billingAmount: BillingAmount,
)

internal data class OrderAcknowledgementSent(
    val orderId: OrderId,
    val emailAddress: EmailAddress,
)

internal sealed class PlaceOrderEvent {
    internal data class OrderPlaced(val orderPlaced: OrderPlaced) : PlaceOrderEvent()
    internal data class BillableOrderPlaced(val billableOrderPlaced: BillableOrderPlaced) : PlaceOrderEvent()
    internal data class AcknowledgementSent(val orderAcknowledgementSent: OrderAcknowledgementSent) : PlaceOrderEvent()
}

internal interface PlaceOrderError

// ---------
// Lifecycle of Order
// ---------

internal sealed class Order {
    data class Unvalidated(val unvalidatedOrder: UnvalidatedOrder) : Order()
    data class Validated(val validatedOrder: ValidatedOrder) : Order()
    data class Priced(val pricedOrder: PricedOrder) : Order()
}

internal data class ValidatedOrder(
    val orderId: OrderId,
    val customerInfo: CustomerInfo,
    val shippingAddress: ValidatedAddress,
    val orderLines: List<ValidatedOrderLine>,
)

public interface OrderId
internal interface CustomerInfo
internal interface Address
internal typealias ValidatedOrderLine = Nothing // TODO

internal interface PricedOrder
internal interface BillingAmount

// ---------
// Type definitions of internal steps
// ---------

// --------- verify order

internal typealias ValidateOrder = (CheckProductCodeExists)
-> (CheckAddressExists)
-> (UnvalidatedOrder)
-> AsyncResult<List<ValidationError>, ValidatedOrder>

internal typealias CheckProductCodeExists = (ProductCode) -> AsyncResult<Unit, Boolean>

internal typealias CheckAddressExists = (UnvalidatedAddress)
-> AsyncResult<AddressValidationError, ValidatedAddress>

internal interface ValidationError

@JvmInline
internal value class ValidatedAddress(val address: UnvalidatedAddress)

internal interface ProductCode

@JvmInline
internal value class AddressValidationError(val message: String)

// --------- price order

internal typealias PriceOrder = (GetProductPrice)
-> (ValidatedOrder)
-> Either<PricingError, PricedOrder>

internal typealias GetProductPrice = (ProductCode)
-> Price

internal interface Price

@JvmInline
internal value class PricingError(val message: String)

// --------- create order acknowledgment

internal typealias AcknowledgeOrder = (CreateOrderAcknowledgmentLetter)
-> (SendOrderAcknowledgment)
-> (PricedOrder)
-> Deferred<OrderAcknowledgementSent?>

internal typealias CreateOrderAcknowledgmentLetter = (PricedOrder)
-> HtmlString

internal sealed class SendResult {
    object Success : SendResult()
    object Failure : SendResult()
}
internal typealias SendOrderAcknowledgment = (OrderAcknowledgment)
-> Deferred<SendResult>

internal data class OrderAcknowledgment(
    val emailAddress: EmailAddress,
    val letter: HtmlString,
)

@JvmInline
internal value class HtmlString(val value: String)
internal interface EmailAddress

// --------- create events

internal typealias CreateEvents = (PricedOrder)
-> List<PlaceOrderEvent>
