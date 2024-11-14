package specification

import calculation.Calculation
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.awt.Color
import java.io.File
import java.sql.ResultSet
import java.sql.ResultSetMetaData


interface ReportInterface {
    val implementationName: String
    val formattingFlag: Boolean
    var titleProperty:String
    var summaryProperty:String
    var formattingList : Map<String, List<String>>
    var dataTable: MutableMap<String, List<String>>


    fun generateReport(data: Map<String, List<String>>, destination: String, header: Boolean, title: String? = null, summary: String? = null)

    fun generateReport(data: Map<String, List<String>>, destination: String, header: Boolean, title: String? = null, summary: String? = null, config: String){
        var dataAfterConfig = readConfig(data,config)
        this.dataTable = (dataAfterConfig as MutableMap<String, List<String>>?)!!
        generateReportWithFormatting(dataAfterConfig!!, destination, header, this.titleProperty, this.summaryProperty,this.formattingList )
    }

    fun generateReport(jsonData: String, destination: String, header: Boolean, title: String? = null, summary: String? = null, config: String? = null){
        var preparedJsonData = prepareJsonData(jsonData)
        this.dataTable = preparedJsonData as MutableMap<String, List<String>>
        if (config!=null)
            generateReport(preparedJsonData, destination, header, title, summary, config)
        else
            generateReport(preparedJsonData, destination, header, title, summary)
        addColumn()
        preparedJsonData = this.dataTable
        generateReport(dataTable, destination, header, title, summary)
    }

    fun generateReport(data: ResultSet, destination: String, header: Boolean, title: String? = null, summary: String? = null){
        val preparedData = prepareData(data)
        generateReport(preparedData, destination, header, title, summary)
    }
    fun generateReportWithFormatting(data: Map<String, List<String>>, destination: String, header: Boolean, title: String? = null, summary: String? = null, formattingList:Map<String, List<String>>?)

    /*fun generateReport(){
        calculations(podaci)
        generateReport(preparedData, destination, header, title, summary)
    }*/

    fun prepareJsonData(jsonData: String): Map<String, List<String>> {

        val gson = Gson()
        val scheduleType = object : TypeToken<List<Map<String, Any>>>() {}.type
        val schedules: List<Map<String, Any>> = gson.fromJson(jsonData, scheduleType)
        val reportData: MutableMap<String, MutableList<String>> = mutableMapOf()

        schedules.forEach { schedule ->
            schedule.forEach { (key, value) ->
                if (!reportData.containsKey(key)) {
                    reportData[key] = mutableListOf()
                }
                reportData[key]!!.add(value.toString())
            }
        }
        return reportData
    }

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


    fun addColumn(){
        /** IZBACITI PRINT IZ BIBLIOTEKE  */
        print("Write path to your column config file")
        //val configPath = "D:\\Marko workspace\\Fakultet\\Projekti\\softverskekomponente_tim_markostojicic_vidanstojic\\softverskekomponente_prvi_projekat\\testApp\\src\\main\\resources\\config.txt"
        val configPath = "C:/Users/vidan_gofx79m/Desktop/softverske komponente/softverskekomponente_tim_markostojicic_vidanstojic/softverskekomponente_prvi_projekat/testApp/src/main/resources/column.txt"
        val lines = File(configPath).readLines()
        var columnName = "n"
        var values = listOf<String>()

        lines.forEach { line ->
            when {
                line.startsWith("ColumnName:") -> {
                    columnName = line.removePrefix("ColumnName:").trim()
                }
                line.startsWith("Values:") -> {
                    values = line.removePrefix("Values:")
                        .trim()
                        .split(",")
                        .map { it.trim().toString() }
                }
            }
        }
        this.dataTable[columnName] = values
    }


    private fun readConfig(data: Map<String, List<String>>, config: String):Map<String, List<String>>? {
        val lines = File(config).readLines()

        val columns = mutableListOf<Int>()
        var calculation: String? = null
        val calculationColumns = mutableListOf<Int>()
        var customTitle: String? = null
        var customSummary: String? = null
        val formattingList = mutableMapOf<String, MutableList<String>>()
        var sign_Operator: String? = null
        var numberForOperation: Int? = null

        val columnsRegex = Regex("""Columns for export:\s*([\d,]+)""")
        val calculationRegex = Regex("""Calculations:\s*(\w+)\((\d+(?:,(?:\d+|[<>=]\d+))*)\s*(?:([<>=]+)\s*(\d+))?\)""")
        val titleRegex = Regex("""Title:\s*(.*)""")
        val summaryRegex = Regex("""Summary:\s*(.*)""")
        val formattingRegex = Regex("""([a-zA-Z_]+)\((\w+)\)""")
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
                        match?.groups?.get(2)?.value?.split(",")?.forEach { part ->
                            when {
                                part.matches(Regex("""\d+""")) -> calculationColumns.add(part.toInt())
                                part.matches(Regex("""[<>=]\d+""")) -> {
                                    sign_Operator = part.first().toString()
                                    numberForOperation = part.drop(1).toInt()
                                }
                            }
                        }
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

                formattingRegex.containsMatchIn(line) -> {

                    val matches = formattingRegex.findAll(line)
                    matches.forEach { match ->
                        val formatType = match.groups[1]?.value ?: ""
                        val targetText = match.groups[2]?.value ?: ""
                        if (formatType.isNotEmpty() && targetText.isNotEmpty()) {
                            formattingList.computeIfAbsent(formatType) { mutableListOf() }.add(targetText)
                        }
                    }
                }
            }
        }

            this.titleProperty = customTitle!!
            this.summaryProperty = customSummary!!
            /** Dodati proveru toga da li je korisnik uopste napisao u configuration fajl neku kalkulaciju/title/summary/formatiranje ili nije.Ukoliko nije napisao onda baciti exception ili slicno.*/


            if (calculation == "SUM") {
                var calculationObject: Calculation = Calculation(data, columns, calculationColumns)
                val dataAfterSum = calculationObject.sumCalculate()
                if (formattingList.isNotEmpty()) {
                    this.formattingList = formattingList
                }
                else println("Nema formatiranja u config fajlu.")    /**IZBACITI PRINT*/


                return dataAfterSum
            } else if (calculation == "AVG") {
                var calculationObject: Calculation = Calculation(data, columns, calculationColumns)
                val dataAfterAvg = calculationObject.avgCalculate()
                if (formattingList.isNotEmpty()) {
                    this.formattingList = formattingList
                }
                else println("Nema formatiranja u config fajlu.")    /**IZBACITI PRINT*/
                return dataAfterAvg
            } else if (calculation == "COUNT") {
                var calculationObject: Calculation = Calculation(data, columns, calculationColumns)
                val dataAfterCount = calculationObject.countCalculate(sign_Operator, numberForOperation)
                if (formattingList.isNotEmpty()) {
                    this.formattingList = formattingList
                }
                else println("Nema formatiranja u config fajlu.")        /**IZBACITI PRINT*/
                return dataAfterCount
            } else if (calculation == "SUB") {
                var calculationObject: Calculation = Calculation(data, columns, calculationColumns)
                val dataAfterSub = calculationObject.subCalculate()
                if (formattingList.isNotEmpty()) {
                    this.formattingList = formattingList
                }
                else println("Nema formatiranja u config fajlu.")        /**IZBACITI PRINT*/
                return dataAfterSub
            } else if (calculation == "MUL") {
                var calculationObject: Calculation = Calculation(data, columns, calculationColumns)
                val dataAfterMul = calculationObject.mulCalculate()
                if (formattingList.isNotEmpty()) {
                    this.formattingList = formattingList
                }
                else println("Nema formatiranja u config fajlu.")            /**IZBACITI PRINT*/
                return dataAfterMul
            } else if (calculation == "DIV") {
                var calculationObject: Calculation = Calculation(data, columns, calculationColumns)
                val dataAfterDiv = calculationObject.divCalculate()
                if (formattingList.isNotEmpty()) {
                    this.formattingList = formattingList
                }
                else println("Nema formatiranja u config fajlu.")         /**IZBACITI PRINT*/
                return dataAfterDiv
            } else {
                return null
                // mnozenje, deljenje, oduzimanje??
            }


        }


    }
//     ./testApp/src/main/resources/izvorPodataka.json