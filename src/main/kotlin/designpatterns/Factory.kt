package designpatterns

interface Computer {
    fun getRam(): String
    fun getDiskSpace(): String
}

class Dummy : Computer {
    override fun getRam(): String {
        return "Dummy G.B."
    }

    override fun getDiskSpace(): String {
        return "Dummy G.B."
    }

}
class PC : Computer {
    override fun getRam(): String {
        return "16 G.B."
    }
    override fun getDiskSpace(): String {
        return "256 G.B."
    }
}

class Server : Computer {
    override fun getRam(): String {
        return "64 G.B."
    }

    override fun getDiskSpace(): String {
       return "10 T.B."
    }

}

class ComputerFactory {
    companion object {
        fun getComputer(type: String): Computer {
            return when(type) {
                "PC" -> PC()
                "Server" -> Server()
                else -> Dummy()
            }
        }
    }
}


fun main() {
    val pc = ComputerFactory.getComputer("PC")
    val server = ComputerFactory.getComputer("Server")
    val dummy = ComputerFactory.getComputer("")
    println(pc.getRam() + ":" + server.getRam() + ":" + dummy.getRam())
    println(pc.getDiskSpace() + ":" + server.getDiskSpace() + ":" + dummy.getDiskSpace())
}