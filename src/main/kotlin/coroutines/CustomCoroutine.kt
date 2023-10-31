import kotlin.coroutines.*

class CustomContinuation(private val block: () -> Unit) : Continuation<Unit> {
    private var isSuspended = false

    override val context: CoroutineContext = EmptyCoroutineContext

    override fun resumeWith(result: Result<Unit>) {
        block()
    }

    suspend fun suspendHere() {
        isSuspended = true
        return
    }

    fun resume() {
        isSuspended = false
        block()
    }

    fun isSuspended(): Boolean {
        return isSuspended
    }
}

class CustomCoroutine(private val block: suspend CustomContinuation.() -> Unit) {
    private val continuation = CustomContinuation {
        // This block will execute when the continuation is resumed
    }

    suspend fun start() {
        continuation.block()
    }
}

suspend fun customDelay(timeMillis: Long) {
    val startTime = System.currentTimeMillis()
    while (System.currentTimeMillis() - startTime < timeMillis) {
        // Simulate waiting
        continuation.suspendHere()
    }
}

lateinit var continuation: CustomContinuation

fun main() {
    val customCoroutine = CustomCoroutine {
        println("Coroutine started")
        customDelay(1000)
        println("Coroutine resumed after delay")
    }

//    continuation = customCoroutine.continuation
//    customCoroutine.start()

    println("Main thread continues")
    while (continuation.isSuspended()) {
        continuation.resume()
    }
}
