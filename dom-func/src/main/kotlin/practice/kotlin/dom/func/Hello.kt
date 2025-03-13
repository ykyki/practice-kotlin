package practice.kotlin.dom.func

internal object Hello {
    internal fun sayHello(
        name: String,
        times: Int,
        printer: (String) -> Unit
    ) {
        repeat(times) {
            printer("Hello, $name!\n")
        }
    }
}
