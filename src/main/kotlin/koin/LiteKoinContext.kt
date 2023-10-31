package koin


object LiteKoinContext {
    private val liteKoin = LiteKoin()

    fun modules(modules: List<Module>) {
        liteKoin.loadModules(modules)
    }

    fun getLiteKoin() = liteKoin
}


fun startLiteKoin(block: LiteKoinContext.() -> Unit) = LiteKoinContext.apply(block)
