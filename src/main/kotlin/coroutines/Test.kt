package coroutines

suspend fun add(a: Int, b: Int): Int {
    var c = a
    var d = b
    var addResult = (c * a) + (d * b)
    return addResult
}

suspend fun doSomething(): String {
    add(1,2)
    print("A")
    add(3,4)
    print("B")
    add(5,6)
    print("C")
    return "doSomethingAsString"
}

suspend fun doAnything(): String? {
    doSomething()
    return null
}

fun main() {

}