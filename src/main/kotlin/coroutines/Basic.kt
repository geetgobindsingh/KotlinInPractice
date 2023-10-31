package coroutines

import kotlinx.coroutines.*
import kotlin.coroutines.EmptyCoroutineContext

class Basic {
    suspend fun doStuff() {
        val scope = CoroutineScope(
            context = (EmptyCoroutineContext
                    + Dispatchers.Unconfined
                    + SupervisorJob()
                    + CoroutineName("parent")
                    + CoroutineExceptionHandler { coroutineContext, throwable ->
                        println("CoroutineExceptionHandler got $coroutineContext $throwable")  })
        )
        /*
        The coroutine is not started immediately.
         It will only start when it's explicitly invoked by a suspension point
         or when you call the start function on the returned Job.
         This is useful when you want to delay the actual execution of the coroutine until it's needed.
         */
        scope.launch(context = Job(), start = CoroutineStart.LAZY) {
            println("Lazy done")
            throw Exception("Self harm")
        }.start() // this specially needs start call method to start
        /*
        This is the default behavior. The coroutine is started immediately, and it competes with other coroutines for execution.
         */
        scope.launch(context = Job(), start = CoroutineStart.DEFAULT) {
            println("Default done")
        }
        /*
        The coroutine is started immediately, just like DEFAULT.
        However, if the parent coroutine (the coroutine that launched this one)
         is canceled before the child coroutine starts executing,
         the child coroutine is automatically canceled as well.
          This ensures that the child coroutine doesn't perform unnecessary work.
         */
        scope.launch(context = Job(), start = CoroutineStart.ATOMIC) {
            println("Atomic done")
        }
        /*
        The coroutine is started immediately and will execute on the current thread
        until the first suspension point. After the first suspension point,
        the coroutine may be dispatched to a different thread based on its dispatcher.
        This option can be useful to avoid unnecessary context switching for very short-lived coroutines.
         */
        scope.launch(context = Job(), start = CoroutineStart.UNDISPATCHED) {
            println("UnDispatched done")
        }
    }
}

fun main() = runBlocking {
    Basic().doStuff()
}