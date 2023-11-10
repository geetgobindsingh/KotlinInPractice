package designpatterns

interface Database {
    fun getPdfReport(): String
    fun getHTMLReport(): String
}

class MySqlDb : Database {
    override fun getPdfReport(): String {
        return "MySqlDb PDF report"
    }

    override fun getHTMLReport(): String {
        return "MySqlDb HTML report"
    }
}

class OracleDb : Database {
    override fun getPdfReport(): String {
        return "OracleDB PDF report"
    }

    override fun getHTMLReport(): String {
        return "OracleDB HTML report"
    }
}

sealed class ReportType {
    object HTML : ReportType()
    object PDF : ReportType()
}

class FacadeHelper {
    companion object {
        fun getReport(databaseName: String, reportType: ReportType): String {
            return when (databaseName) {
                "Mysql" -> {
                    val db = MySqlDb()
                    return when (reportType) {
                        is ReportType.PDF -> db.getPdfReport()
                        is ReportType.HTML -> db.getHTMLReport()
                        else -> ""
                    }
                }

                "Oracle" -> {
                    val db = OracleDb()
                    return when (reportType) {
                        is ReportType.PDF -> db.getPdfReport()
                        is ReportType.HTML -> db.getHTMLReport()
                        else -> ""
                    }
                }

                else -> ""
            }
        }
    }
}

fun main() {
    println(FacadeHelper.getReport("Mysql", ReportType.PDF))
    println(FacadeHelper.getReport("Mysql", ReportType.HTML))
    println(FacadeHelper.getReport("Oracle", ReportType.PDF))
    println(FacadeHelper.getReport("Oracle", ReportType.PDF))
}