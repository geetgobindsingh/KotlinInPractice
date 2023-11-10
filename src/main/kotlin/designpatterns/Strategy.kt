package designpatterns

interface PaymentStrategy {
    fun pay(amount: Int)
}

class CreditCard(val cardNumber: String) : PaymentStrategy {
    override fun pay(amount: Int) {
        println("$amount Pay via CredCard")
    }

}

class DebitCard(val cardNumber: String) : PaymentStrategy {
    override fun pay(amount: Int) {
        println("$amount Pay via DebitCard")
    }

}

class PaypalOnline(val username: String, val password: String) : PaymentStrategy {
    override fun pay(amount: Int) {
        println("$amount Pay via PaypalOnline")
    }

}

data class Item(val skuId: String, val price: Int)

class ShoppingCart(private val items: ArrayList<Item> = ArrayList<Item>()) {
    fun addItem(item: Item) {
        items.add(item)
    }

    private fun getTotalBill(): Int {
        return items.map { it.price }.reduce { acc, number ->
            acc + number
        }
    }

    fun pay(paymentStrategy: PaymentStrategy) {
        paymentStrategy.pay(getTotalBill())
        items.clear()
    }
}

fun main() {
    val shoppingCart = ShoppingCart()
    shoppingCart.addItem(Item("1", 10))
    shoppingCart.addItem(Item("2", 10))
    shoppingCart.addItem(Item("3", 10))
    shoppingCart.pay(CreditCard("1234567890"))
    shoppingCart.addItem(Item("3", 10))
    shoppingCart.pay(DebitCard("1234567890"))
    shoppingCart.addItem(Item("4", 20))
    shoppingCart.pay(PaypalOnline("geetgobindsingh", "********"))
}