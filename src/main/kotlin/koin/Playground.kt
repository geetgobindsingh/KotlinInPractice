package koin


data class DATA(val amount: Double)


class UseCase(private val repo: Repository) {
    fun execute() = repo.getText()
    fun loadData() = repo.getDataFromAPI()
}

class Repository {
    private val api: MyAPI = get()

    fun getText() = "Text from repository"

    fun getDataFromAPI() = api.getData()
}

class MyAPI {
    fun getData() = DATA(20.0)
}


class ViewModel( private val useCase: UseCase) {

    fun showText() {
        println(useCase.execute())
    }

    fun showData() = println(useCase.loadData())
}


val mod1 = module {
    factory { ViewModel(get()) }
    factory { Repository() }
}

val mod2 = module {
    factory { UseCase(get()) }
    factory { MyAPI() }
}

val viewModel: ViewModel by inject()
fun main() {

    startLiteKoin {
        modules(mod1 + mod2)
    }

    val repo: Repository = get() // Forget about this. Just wanted to show off the get()

    viewModel.showText()
    viewModel.showData()

}
