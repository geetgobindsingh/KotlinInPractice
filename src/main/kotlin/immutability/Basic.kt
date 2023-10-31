package immutability

data class Basic(val a: Int)

fun main() {
    var basic = Basic(a = 1)
    println(basic)
    basic = basic.copy(a = 2)
    println(basic)
}