package designpatterns

public class Singleton private constructor() {

    companion object {
        private var sInstance: Singleton? = null
        fun getInstance(): Singleton {
            if (sInstance == null) {
                synchronized(Singleton::class) {
                    if (sInstance == null) {
                        sInstance = Singleton()
                    }
                }
            }
            return sInstance!!
        }
    }

    fun add(a: Int, b: Int) : Int = a + b

}


fun main() {
    val singleton = Singleton.getInstance()
    print(singleton.add(1,2))
}