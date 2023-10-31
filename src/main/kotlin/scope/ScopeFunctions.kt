package scope

data class ClassA(val intA: Int)

fun main(args: Array<String>) {
    val list = listOf<ClassA>(ClassA(1), ClassA(2), ClassA(3), ClassA(4))
    // let scope
    // object reference : it
    // receiver: lambda result
    // access property: with it
    // here return is int value that is list.size
    val letResult = list.let {
        return@let it.size
    }
    println("OG list: $list")
    println("letResult: $letResult")

    val letNUllResult = null.let {
        return@let 0
    }
    println("letNUllResult: $letNUllResult")

    // run scope
    // object reference : this
    // receiver: lambda result
    // access property: without this
    // here return is int value that is list.size
    val runOnContextObjectResult = list.run {
        return@run size
    }

    println("OG list: $list")
    println("runOnContextObjectResult: $runOnContextObjectResult")

    // run scope Without ContextObject
    val runWithoutContextObject = run {
        return@run 1
    }

    println("runWithoutContextObject: $runWithoutContextObject")

    val runNUllResult = null.run {
        return@run 0
    }
    println("runNUllResult: $runNUllResult")

    val inta: Int? = null
    val intb: Int? = inta

    val resultOfLetAndRunOnNULL = intb?.let {
        return@let 2
    } ?: run {
        // here run behave as runWithoutContextObject
        return@run 1
    }
    println("resultOfLetAndRunOnNULL: $resultOfLetAndRunOnNULL")


    // with scope
    // object reference : this
    // receiver: lambda result
    // access property: without this
    // here return is int value that is list.size
    val withResult = with(list) {
        return@with size
    }

    println("#####")
    data class Person(var age: Int?, var name: String?)

    println(Person(null, null).run {
        this.age = 3
    })

    println(with(Person(null, null)) {
        this.age = 3
    })
    println("#####")


    println("OG list: $list")
    //println("OG list: ${list.toRefString()}")
    println("withResult: $withResult")

    val withNUllResult = with(null) {
        return@with 0
    }
    println("withNUllResult: $withNUllResult")


    // apply scope
    // object reference : this
    // access property: without this
    // receiver: same context object on which it is applied
    val applyResult = list.apply {
        size
    }
    println("OG list: ${list.toRefString()}")
    println("applyResult: ${applyResult.toRefString()}")

    val applyNUllResult = null.apply {

    }
    println("applyNUllResult: $applyNUllResult")


    // also scope
    // object reference : it
    // access property: with it
    // receiver: same context object on which it is applied
    val alsoResult = list.also {
    }
    println("OG list: ${list.toRefString()}")
    println("alsoResult: ${alsoResult.toRefString()}")

    val alsoNUllResult = null.also {

    }
    println("alsoNUllResult: $alsoNUllResult")

}

fun Any.toRefString(): String {
    return this.javaClass.name + "@" +
            Integer.toHexString(System.identityHashCode(this))
}