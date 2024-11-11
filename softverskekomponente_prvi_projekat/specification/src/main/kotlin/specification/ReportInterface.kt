package specification

import java.awt.Color
import java.io.File
import java.sql.ResultSet
import java.sql.ResultSetMetaData


interface ReportInterface {
    val implementationName: String
    val formattingFlag: Boolean
    var titleProperty:String
    var summaryProperty:String
    var formattingNameProperty: String?
    var formattingTextProperty :String?


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
      //  generateReport(dataAfterConfig!!, destination, header, this.titleProperty, this.summaryProperty)
        generateReportWithFormatting(dataAfterConfig!!, destination, header, this.titleProperty, this.summaryProperty, this.formattingNameProperty, this.formattingTextProperty)
    }

    fun generateReport(data: ResultSet, destination: String, header: Boolean, title: String? = null, summary: String? = null){
        val preparedData = prepareData(data)
        generateReport(preparedData, destination, header, title, summary)
    }
    fun generateReportWithFormatting(data: Map<String, List<String>>, destination: String, header: Boolean, title: String? = null, summary: String? = null, formattingName: String? = null, formattingText: String? = null)

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
        var formattingType: String? = null
        var textForFormatting: String? = null
        var sign_Operator: String? = null
        var numberForOperation: Int? = null

        val columnsRegex = Regex("""Columns for export:\s*([\d,]+)""")
        val calculationRegex = Regex("""Calculations:\s*(\w+)\((\d+),\s*([<>=]+)?\s*(\d+)?\)""")
        val titleRegex = Regex("""Title:\s*(.*)""")
        val summaryRegex = Regex("""Summary:\s*(.*)""")
        val formattingRegex = Regex("""Formatting:\s*(\w+)\((\w+)\)""")

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
                    if (calculation == "COUNT") {
                        match?.groups?.get(2)?.value?.toInt()?.let { calculationColumns.add(it) }
                        sign_Operator = match?.groups?.get(3)?.value
                        numberForOperation = match?.groups?.get(4)?.value?.toInt()
                    } else {
                        match?.groups?.get(2)?.value?.split(",")?.forEach {
                            calculationColumns.add(it.trim().toInt())
                        }
                    }
                }
                titleRegex.matches(line) -> {
                    customTitle = titleRegex.find(line)?.groups?.get(1)?.value
                }
                summaryRegex.matches(line) -> {
                    customSummary = summaryRegex.find(line)?.groups?.get(1)?.value
                }
                formattingRegex.matches(line) -> {
                    val match = formattingRegex.find(line)
                    formattingType = match?.groups?.get(1)?.value ?: ""
                    textForFormatting = match?.groups?.get(2)?.value ?: ""
                }
            }
        }

        // Ispis podataka za proveru
       // println("FORMATIRANJE: $formattingType za text(kolonu) $textForFormatting")

        this.titleProperty = customTitle!!
        this.summaryProperty = customSummary!!
/** Dodati proveru toga da li je korisnik uopste napisao u configuration fajl neku kalkulaciju/title/summary/formatiranje ili nije.Ukoliko nije napisao onda mu poslati print neki da je pogresio ili slicno.*/




        if(calculation =="SUM"){
            val dataAfterSum = sumCalculate(data, columns, calculationColumns)
            if((!formattingType.isNullOrEmpty()) || (!textForFormatting.isNullOrEmpty() )){
                println("Poslat je zeljeni nacin formatiranja.")
                this.formattingNameProperty = formattingType
                this.formattingTextProperty = textForFormatting

            }
            else println("Nema formatiranja u config fajlu.")
            return dataAfterSum
        }
        else if(calculation == "AVG"){
            val dataAfterAvg = avgCalculate(data, columns, calculationColumns)
            return dataAfterAvg
        }
        else if(calculation == "COUNT"){
            val dataAfterCount = countCalculate(data, columns, calculationColumns, sign_Operator, numberForOperation)
            return dataAfterCount
        }
        else if (calculation == "SUB"){
            val dataAfterSub = subCalculate(data, columns, calculationColumns)
            return dataAfterSub
        }
        else if (calculation == "MUL"){
            val dataAfterMul = mulCalculate(data, columns, calculationColumns)
            return dataAfterMul
        }
        else if (calculation == "DIV"){
            val dataAfterDiv = divCalculate(data, columns, calculationColumns)
            return dataAfterDiv
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

    private fun avgCalculate(data: Map<String, List<String>>, configColumns : MutableList<Int>, avgColumns :MutableList<Int>):Map<String, List<String>>?{

        val result = mutableMapOf<String, List<String>>()
        val filteredData = mutableMapOf<String, List<String>>()

        for(i in configColumns){
            val columnName = data.keys.elementAt(i)
            filteredData[columnName] = data[columnName] ?: emptyList()
        }
        val avgColumnValues = mutableListOf<String>()

        val numRows = data.values.firstOrNull()?.size ?: 0
        for (i in 0 until numRows) {
            var numer = 0;
            var sum = 0
            for (colIndex in avgColumns) {
                val columnName = data.keys.elementAt(colIndex)
                val value = data[columnName]?.get(i)?.toIntOrNull() ?: 0
                sum += value
                numer++
            }
            val avg = (sum * 1.0) / (numer * 1.0)
            avgColumnValues.add(avg.toString())
        }

        result["avgColumn"] = avgColumnValues

        filteredData.forEach { (key, value) ->
            result[key] = value
        }

        return result
    }

    private fun countCalculate(data: Map<String, List<String>>, configColumns: MutableList<Int>, countColumns: MutableList<Int>, operand: String?, numberForOperation: Int?): Map<String, List<String>>? {

        val result = mutableMapOf<String, MutableList<String>>()
        result["countColumn"] = mutableListOf()

        val columnName = data.keys.elementAt(countColumns.get(0))
        val columnData = data[columnName] ?: emptyList()

        columnData.forEach { value ->
            val intValue = value.toIntOrNull()
            if (intValue != null && operand != null && numberForOperation != null) {
                val conditionMet = when (operand) {
                    "<" -> intValue < numberForOperation
                    ">" -> intValue > numberForOperation
                    "<=" -> intValue <= numberForOperation
                    ">=" -> intValue >= numberForOperation
                    "=" -> intValue == numberForOperation
                    else -> false
                }

                if (conditionMet) {
                    result["countColumn"]?.add(value)
                }
            }
        }

        val numRows = result["countColumn"]?.size ?: 0
        result["countColumn"]?.add("Count $numRows")
        println(numRows)
        return result
    }


    private fun subCalculate(data: Map<String, List<String>>, configColumns : MutableList<Int>, subColumns :MutableList<Int> ):Map<String, List<String>>?{

        val result = mutableMapOf<String, List<String>>()
        val filteredData = mutableMapOf<String, List<String>>()

        for(i in configColumns){
            val columnName = data.keys.elementAt(i)
            filteredData[columnName] = data[columnName] ?: emptyList()
        }
        val subColumnValues = mutableListOf<String>()
        val numRows = data.values.firstOrNull()?.size ?: 0
        for (i in 0 until numRows) {
            var colIndex = 0
            var sub = 0
            while (colIndex < subColumns.size - 1) {
                var columnName = data.keys.elementAt(subColumns.get(colIndex))
                var value = data[columnName]?.get(i)?.toIntOrNull() ?: 0
                sub = value
                columnName = data.keys.elementAt(subColumns.get(++colIndex))
                value = data[columnName]?.get(i)?.toIntOrNull() ?: 0
                sub -= value
            }
            subColumnValues.add(sub.toString())
        }

        result["subColumn"] = subColumnValues

        filteredData.forEach { (key, value) ->
            result[key] = value
        }

        return result

    }

    private fun mulCalculate(data: Map<String, List<String>>, configColumns : MutableList<Int>, muulColumns :MutableList<Int> ):Map<String, List<String>>?{

        val result = mutableMapOf<String, List<String>>()
        val filteredData = mutableMapOf<String, List<String>>()

        for(i in configColumns){
            val columnName = data.keys.elementAt(i)
            filteredData[columnName] = data[columnName] ?: emptyList()
        }
        val mulColumnValues = mutableListOf<String>()
        val numRows = data.values.firstOrNull()?.size ?: 0
        for (i in 0 until numRows) {
            var mul = 1.0
            for (colIndex in muulColumns) {
                val columnName = data.keys.elementAt(colIndex)
                val value = data[columnName]?.get(i)?.toIntOrNull() ?: 0
                mul *= value
            }
            mulColumnValues.add(mul.toString())
        }

        result["mulColumn"] = mulColumnValues

        filteredData.forEach { (key, value) ->
            result[key] = value
        }

        return result

    }

    private fun divCalculate(data: Map<String, List<String>>, configColumns : MutableList<Int>, divColumns :MutableList<Int> ):Map<String, List<String>>?{

        val result = mutableMapOf<String, List<String>>()
        val filteredData = mutableMapOf<String, List<String>>()

        for(i in configColumns){
            val columnName = data.keys.elementAt(i)
            filteredData[columnName] = data[columnName] ?: emptyList()
        }
        val divColumnValues = mutableListOf<String>()
        val numRows = data.values.firstOrNull()?.size ?: 0
        for (i in 0 until numRows) {
            var colIndex = 0
            var div = 0.0
            while (colIndex < divColumns.size - 1) {
                var columnName = data.keys.elementAt(divColumns.get(colIndex))
                var value = data[columnName]?.get(i)?.toIntOrNull() ?: 0
                div = value.toDouble()
                columnName = data.keys.elementAt(divColumns.get(++colIndex))
                value = data[columnName]?.get(i)?.toIntOrNull() ?: 0
                div /= value
            }
            divColumnValues.add(div.toString())
        }

        result["divColumn"] = divColumnValues

        filteredData.forEach { (key, value) ->
            result[key] = value
        }

        return result

    }

}
/*
val result = mutableMapOf<String, MutableList<String>>()
        result["countColumn"] = mutableListOf()

        //for (i in countColumns) {
            val columnName = data.keys.elementAt(countColumns.get(0))
            //result["countColumn"]?.addAll(data[columnName] ?: emptyList())
            val columnData = data[columnName] ?: emptyList()

            columnData.forEach { value ->
                val intValue = value.toIntOrNull() // Konvertujemo vrednost u Int ako je moguće
                if (intValue != null && operand != null && numberForOperation != null) {
                    val conditionMet = when (operand) {
                        "<" -> intValue < numberForOperation
                        ">" -> intValue > numberForOperation
                        "<=" -> intValue <= numberForOperation
                        ">=" -> intValue >= numberForOperation
                        "=" -> intValue == numberForOperation
                        else -> false
                    }

                    // Ako uslov zadovoljava, dodajemo vrednost u rezultat
                    if (conditionMet) {
                        result[columnName]?.add(value)
                    }
                }
            }
        //}

        val numRows = result["countColumn"]?.size ?: 0
        result["countColumn"]?.add("Count $numRows")
        println(numRows)
        return result
    }
 */
//    /izvorPodataka.json