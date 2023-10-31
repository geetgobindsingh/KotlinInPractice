package coroutines.playground

import java.lang.Exception
import kotlin.concurrent.thread
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED

fun main() {
    // Compiler generates a new class that contains the suspendingLambda
    class Continuation1(val previousContinuation: Continuation<Double>) : Continuation<Unit> {
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

        // Define a suspend lambda
        val suspendingLambda: (Continuation<Any?>) -> Any? = { continuation ->
            calculateFourthRoot(16.0, continuation)
        }
    }

    // Define a callback object
    val completionCallback: Continuation<Double> = object : Continuation<Double> {
        override val context: CoroutineContext = EmptyCoroutineContext

        override fun resumeWith(result: Result<Double>) {
            println("④ ${result.getOrNull()}")
        }
    }

    // Call to createCoroutineUnintercepted returns Continuation1 instance
    val continuation = Continuation1(completionCallback)

    // Start the coroutine
    continuation.resumeWith(Result.success(Unit))
}


fun calculateFourthRoot(number: Double, continuation: Continuation<Any?>): Any? {
    class Continuation2(val previousContinuation: Continuation<Double>) : Continuation<Any?> {
        var result: Result<Any?>? = null
        var checkPoint = 0

        override val context = previousContinuation.context
        override fun resumeWith(result: Result<Any?>) {
            this.result = result
            try {
                val returnedValue = calculateFourthRoot(number, this)
                if (returnedValue != COROUTINE_SUSPENDED) {
                    previousContinuation.resumeWith(Result.success(returnedValue) as Result<Double>)
                }
            } catch (e: Exception) {
                previousContinuation.resumeWith(Result.failure(e))
            }
        }
    }

    // Wrap previous continuation with current continuation, only if not already wrapped
    val localContinuation = if (continuation is Continuation2) continuation
    else Continuation2(continuation)

    var result: Any? = null
    run label1@{
        when (localContinuation.checkPoint) {
            0 -> {
                var current = number
                println("① $current")

                localContinuation.checkPoint = 1
                result = calculateSquareRoot(
                    current,
                    localContinuation
                )
                if (result == COROUTINE_SUSPENDED) {
                    return COROUTINE_SUSPENDED
                }
            }

            1 -> {
                result = localContinuation.result?.getOrThrow()
            }

            2 -> {
                result = localContinuation.result?.getOrThrow()
                return@label1
            }
        }
        var current = result as Double
        println("② $current")

        localContinuation.checkPoint = 2
        result = calculateSquareRoot(
            current,
            localContinuation
        )
        if (result == COROUTINE_SUSPENDED) {
            return COROUTINE_SUSPENDED
        }
    }
    var current = result as Double
    println("③ $current")

    return current
}

fun calculateSquareRoot(number: Double, continuation: Continuation<Any?>): Any? {
    val block: (Continuation<Double>) -> Any? = {
        thread {
            val sqrt = kotlin.math.sqrt(number)
            it.resumeWith(Result.success(sqrt))
        }
        COROUTINE_SUSPENDED
    }
    // Call to suspendCoroutineUninterceptedOrReturn invokes the block with current continuation
    return block(continuation)
}