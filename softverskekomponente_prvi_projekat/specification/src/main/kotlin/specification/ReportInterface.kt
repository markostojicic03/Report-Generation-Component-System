package specification

import java.awt.Color
import java.io.File
import java.sql.ResultSet
import java.sql.ResultSetMetaData


interface ReportInterface {
    abstract val implementationName: String
    abstract val formattingFlag: Boolean
    var titleProperty:String
    var summaryProperty:String

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
        var dataAfterConfig = readConfig(data,config)
        generateReport(dataAfterConfig!!, destination, header, titleProperty, summaryProperty )
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

    private fun readConfig(data: Map<String, List<String>>, config: String):Map<String, List<String>>?{
        val lines = File(config).readLines()

        val columns = mutableListOf<Int>()
        var calculation: String? = null
        val calculationColumns = mutableListOf<Int>()
        var customTitle: String? = null
        var customSummary: String? = null


        val columnsRegex = Regex("""Columns for export:\s*([\d,]+)""")
        val calculationRegex = Regex("""Calculations:\s*(\w+)\(([\d,]+)\)""")
        val titleRegex = Regex("""Title:\s*(.*)""")
        val summaryRegex = Regex("""Summary:\s*(.*)""")


        for (line in lines) {
            when {
                columnsRegex.matches(line) -> {
                    val match = columnsRegex.find(line)
                    match?.groups?.get(1)?.value?.split(",")?.forEach {
                        columns.add(it.trim().toInt())
                    }
                }
                calculationRegex.matches(line) -> {
                    val match = calculationRegex.find(line)
                    calculation = match?.groups?.get(1)?.value
                    match?.groups?.get(2)?.value?.split(",")?.forEach {
                        calculationColumns.add(it.trim().toInt())
                    }
                }
                titleRegex.matches(line) -> {
                    customTitle = titleRegex.find(line)?.groups?.get(1)?.value
                }
                summaryRegex.matches(line) -> {
                    customSummary = summaryRegex.find(line)?.groups?.get(1)?.value
                }
            }
        }

        // Ispis podataka za proveru
        println("Konfigurisane kolone: $columns")
        println("Kalkulacija: $calculation za kolone $calculationColumns")

        this.titleProperty = customTitle!!
        this.summaryProperty = customSummary!!

        if(calculation =="SUM"){
            val dataAfterSum = sumCalculate(data, columns, calculationColumns)

            return dataAfterSum
        }
        else if(calculation == "AVG"){
            return null
        }
        else if(calculation == "COUNT"){
            return null
        }
        else{
            return null
            // mnozenje, deljenje, oduzimanje??
        }


    }

    private fun sumCalculate(data: Map<String, List<String>>, configColumns : MutableList<Int>, sumColumns :MutableList<Int> ):Map<String, List<String>>?{

        val result = mutableMapOf<String, List<String>>()
        val filteredData = mutableMapOf<String, List<String>>()

        for(i in configColumns){
            val columnName = data.keys.elementAt(i)
            filteredData[columnName] = data[columnName] ?: emptyList()
        }
        val sumColumnValues = mutableListOf<String>()
        val numRows = data.values.firstOrNull()?.size ?: 0
        for (i in 0 until numRows) {
            var sum = 0
            for (colIndex in sumColumns) {
                val columnName = data.keys.elementAt(colIndex)
                val value = data[columnName]?.get(i)?.toIntOrNull() ?: 0
                sum += value
            }
            sumColumnValues.add(sum.toString())
        }

        result["sumColumn"] = sumColumnValues

        filteredData.forEach { (key, value) ->
            result[key] = value
        }

        return result

    }


}