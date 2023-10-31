package coroutines.playground

import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED

class Play2 {
}


fun main() {
    // Compiler generates a new class that contains the suspendingLambda
    class Continuation1(val previousContinuation: Continuation<String>) : Continuation<Unit> {
        override val context = previousContinuation.context
        override fun resumeWith(result: Result<Unit>) {
            try {
                val returnedValue = suspendingLambda(previousContinuation as Continuation<Any?>)
                if (returnedValue != COROUTINE_SUSPENDED) {
                    previousContinuation.resumeWith(Result.success(returnedValue))
                }
            } catch (e: Exception) {
                previousContinuation.resumeWith(Result.failure(e))
            }
        }

        // Define a suspend lambda that returns "Hello World!"
        val suspendingLambda: (Continuation<Any?>) -> Any? = { continuation ->
            "Hello World!"
        }
    }

    // Define a callback object
    val completionCallback: Continuation<String> = object : Continuation<String> {
        override val context: CoroutineContext = EmptyCoroutineContext

        override fun resumeWith(result: Result<String>) {
            // Prints "Hello World!"
            println(result.getOrNull())
        }
    }

    // Call to createCoroutineUnintercepted returns a Continuation1 class instance
    val continuation = Continuation1(completionCallback)

    // Start the coroutine
    continuation.resumeWith(Result.success(Unit))
}