package coroutines

import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class SingleAsyncTask {

    suspend fun doAsyncOperation(): String = suspendCancellableCoroutine { continuation ->
        val callback = object : Callback {
            override fun onSuccess(result: String) {
                continuation.resume(result)
            }

            override fun onFailure(error: Exception) {
                continuation.resumeWithException(error)
            }
        }

        initiateAsyncOperation(callback)

        // This block is invoked when the coroutine is cancelled
        continuation.invokeOnCancellation {
            // Clean up or cancel the underlying async operation if needed
            cancelAsyncOperation()
        }
    }

    interface Callback {
        fun onSuccess(result: String)
        fun onFailure(error: Exception)
    }

    // Simulating an async operation that uses callbacks
    fun initiateAsyncOperation(callback: Callback) {
        CoroutineScope(Dispatchers.Default).launch {
            delay(1000) // Simulating some async work
            // For the sake of the example, let's assume the operation is successful
            callback.onSuccess("Async operation result")
        }
    }

    fun cancelAsyncOperation() {
        // Clean up or cancel the ongoing async operation here
    }
}


fun main() = runBlocking(SupervisorJob() + CoroutineExceptionHandler { _, t -> print("$t") }) {
    try {
        val result = SingleAsyncTask().doAsyncOperation()
        println("Result: $result")
        val result2 = SingleAsyncTask().doAsyncOperation()
        println("Result: $result2")
    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}

