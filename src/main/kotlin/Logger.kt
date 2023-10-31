object Logger {
    fun d(message: String) {
        println(message)
    }

    fun d(tag: String, message: String) {
        println("$tag : $message")
    }

    fun e(message: String) {
        println("Error: $message")
    }

    fun e(tag: String, message: String) {
        println("Error: $tag : $message")
    }
}