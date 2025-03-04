package practice.kotlin.dom.func

object Hello {
    fun sayHello(
        name: String,
        times: Int,
        printer: (String) -> Unit
    ) {
        // repeat(times) {
        //     printer("Hello, $name!\n")
        // }
        for (i in 0 until times) {
            printer("Hello, $name!\n")
        }
    }
}
