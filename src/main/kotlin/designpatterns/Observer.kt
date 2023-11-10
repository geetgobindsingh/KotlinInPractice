package designpatterns

import java.lang.ref.WeakReference

interface Observer {
    fun update(value: String)
}


interface Observable {

    fun sendUpdates(value: String)
    fun subscribe(observer: Observer)
    fun unsubscribe(observer: Observer)
}

class NewsAgency : Observable {

    private var news: String = ""
    private val observers = HashSet<WeakReference<Observer>>()
    override fun sendUpdates(value: String) {
        news = value
        notifyObservers()
    }

    private fun notifyObservers() {
        val removeList = ArrayList<WeakReference<Observer>>()
        for (subscriber in observers) {
            if (subscriber.get() != null) {
                subscriber.get()!!.update(news)
            } else {
                removeList.add(subscriber)
            }
        }
        for (subscriber in removeList) {
            observers.remove(subscriber)
        }
    }

    override fun subscribe(observer: Observer) {
        var alreadyExist: Boolean = false
        for (subscriber in observers) {
            if (subscriber.get() != null && subscriber.get() == observer) {
                alreadyExist = true
                break
            }
        }
        if (!alreadyExist) {
            observers.add(WeakReference(observer))
        }
    }

    override fun unsubscribe(observer: Observer) {
        var removeObserver: WeakReference<Observer>? = null
        for (subscriber in observers) {
            if (subscriber.get() != null && subscriber.get() == observer) {
                removeObserver = subscriber
                break
            }
        }
        observers.remove(removeObserver)
    }

}

class NewsChannel(val name: String) : Observer {
    override fun update(value: String) {
        println("${name}'s new : $value")
    }

}

fun main() {
    val newsAgency = NewsAgency()
    val newsChannelA = NewsChannel("A")
    val newsChannelB = NewsChannel("B")
    newsAgency.subscribe(newsChannelA)
    newsAgency.subscribe(newsChannelB)
    newsAgency.sendUpdates("0")
    newsAgency.sendUpdates("1")
    newsAgency.sendUpdates("2")
    newsAgency.unsubscribe(newsChannelB)
    newsAgency.sendUpdates("3")
    newsAgency.sendUpdates("4")
    newsAgency.unsubscribe(newsChannelA)
    newsAgency.sendUpdates("5")
}