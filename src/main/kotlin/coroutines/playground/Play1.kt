package coroutines.playground

import kotlin.coroutines.*

class Play1 {
}

fun main() {
    // Define a suspend lambda that returns "Hello World!"
    val suspendingLambda: suspend () -> String = suspend {
        "Hello World!"
    }

    // Define a callback object
    val completionCallback = object : Continuation<String> {
        override val context: CoroutineContext = EmptyCoroutineContext
        override fun resumeWith(result: Result<String>) {
            // Prints "Hello World!"
            println(result.getOrNull())
        }
    }

    // Create the coroutine
    val continuation = suspendingLambda.createCoroutine(completionCallback)

    // Start the coroutine
    continuation.resumeWith(Result.success(Unit))
}
