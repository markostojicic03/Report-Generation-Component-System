package specification

import java.awt.Color
import java.io.File
import java.sql.ResultSet
import java.sql.ResultSetMetaData


interface ReportInterface {
    abstract val implementationName: String
    abstract val formattingFlag: Boolean

    /**
        IZMENJENA VERZIJA SPECIFIKACIJE TREBA DA SADRZI:
     * funkcija za kalkulacije, treba ti dodatan argument u novoj funkciji za generate report, u trenutnku kada se pozove taj generatereport sa dodatnim argumentom(npr. argument ce se zvati config),
         * onda se taj argument salje funkciji koja radi kalkulacije, npr. ukoliko je dodatan argument SUM, onda ce funkcija za kalkulacije da sabere kolone i da pozove ponovo generatereport koji ce da izbaci izlaz za statemente nakon kalkulacija
         * ovaj deo je objasnjen kod Jefimije na vezbama 4, oko 44/45. minuta
     * potrebna ti je i funkcija za razlicite vrste formatiranja(bold, italic, color itd.), formatiranje je moguce samo za header i summary
     * sve se radi preko mape, tako da ukoliko ti korisnik posalje resultset ti ces samo preko funkcije prepare statement da to pretvoris u mapu
         * na isti nacin bi to radio i ukoliko ti posalje drugaciji izvor podataka(kao npr. json)
     * BITNI DELOVI SA CASA KOD JEFIMIJE(Vezbe 23.10.2024.):
         * na pocetku casa do 33. minuta objasnjava kako se povezuje projekat(ovaj deo je bitan jer ce to pitati na odbrani)
         * objasnjavanje specifikacije - od 33. minuta
         * objasnjavanje CSV implementacije - od 45. minuta
         * objasnjavanje TXT implementacije - od 48. minuta
         * objasnjavanje PDF implementacije - od 51. minuta
         * objasnjavanje Excel implementacije - od 54. minuta
         * objasnjavanje testne aplikacije - od 55. minuta
     */

    fun generateReport(data: Map<String, List<String>>, destination: String, header: Boolean, title: String? = null, summary: String? = null)
    fun generateReport(data: Map<String, List<String>>, destination: String, header: Boolean, title: String? = null, summary: String? = null, config: String){
        var calculatedData = calculateData(data,config)
        //generateReport(calculatedData, destination, header, title, summary )
    }

    fun generateReport(data: ResultSet, destination: String, header: Boolean, title: String? = null, summary: String? = null){
        val preparedData = prepareData(data)
        generateReport(preparedData, destination, header, title, summary)
    }

    /*fun generateReport(){
        calculations(podaci)
        generateReport(preparedData, destination, header, title, summary)
    }*/

    private fun prepareData(resultSet: ResultSet): Map<String, List<String>> {
        val reportData = mutableMapOf<String, MutableList<String>>()

        val metaData: ResultSetMetaData = resultSet.metaData
        val columnCount = metaData.columnCount

        for (i in 1..columnCount) {
            val columnName = metaData.getColumnName(i)
            reportData[columnName] = mutableListOf()
        }

        while (resultSet.next()) {
            for (i in 1..columnCount) {
                val columnName = metaData.getColumnName(i)
                reportData[columnName]!!.add(resultSet.getString(i))
            }
        }

        return reportData
    }

    fun boldFormattingMethod(textForBold : String, density : Int){
        if(!this.formattingFlag){
            println("This format is not valid for formatting.")
            return
        }

    }

    fun italicFormattingMethod(textForItalic : String, italicCurv: Int){
        if(!this.formattingFlag){
            println("This format is not valid for formatting.")
            return
        }

    }

    fun underlineFormattingMethod(textForUnderline : String){
        if(!this.formattingFlag){
            println("This format is not valid for formatting.")
            return
        }

    }

    fun colorFormattingMethod(textForUnderColor : String, color : Color){
        if(!this.formattingFlag){
            println("This format is not valid for formatting.")
            return
        }

    }
    fun calculateData(columns: Map<String, List<String>>, config: String){
        var columns = ""
        var calculate = ""
        var title = ""

        File(config).forEachLine { line ->
            val parts = line.split(":")
            if (parts.size == 2) {
                val key = parts[0].trim()
                val value = parts[1].trim()
                when (key) {
                    "columns" -> columns = value
                    "calculate" -> calculate = value
                    "title" -> title = value
                }
            }
        }

        println("Columns: $columns")
        println("Calculate: $calculate")
        println("Title: $title")

        //return reportData
    }


}