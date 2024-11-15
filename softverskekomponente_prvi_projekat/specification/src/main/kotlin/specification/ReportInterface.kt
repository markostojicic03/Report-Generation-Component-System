package specification

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import model.CalculationModel
import model.MyException
import java.io.File
import java.sql.ResultSet
import java.sql.ResultSetMetaData

/**
 * An interface for generating formatted or non-formatted reports from various sources, such as a map of column data, a database, or a JSON file, to different formats.
 *
 * Implementations of this interface should define how the report is formatted and saved, specifying how the functions for generating reports will work for that format, or they may choose to use the default implementation of the function.
 */
interface ReportInterface {
    /**
     * Each implementation that uses this interface must specify the name of the implementation as a string.
     */
    val implementationName: String
    /**
     * Each implementation must indicate in the booleanFlag whether the format supports valid formatting (true) or not (false).
     */
    val formattingFlag: Boolean
    /**
     * Each implementation must specify the title as a string. If the report does not have a title, an empty string should be used.
     */
    var titleProperty:String
    /**
     * Each implementation must specify the summary as a string. If the report does not have a summary, an empty string should be used.
     */
    var summaryProperty:String
    /**
     * Each implementation must include a map that specifies the types of formatting to be applied to columns, the title, and the summary.
     * Implementations that support formatting must define this field, while implementations that do not support formatting should set this field to null.
     *
     */
    var formattingList : Map<String, List<String>>

    /**
     * Generates a report based on the provided data and writes it to the specified destination.
     *
     * @param data A map where the key is the column name and the value is a list of strings representing the column data.
     *             All lists in the map should have the same size to ensure proper row alignment.
     * @param destination The file path where the report will be saved.
     * @param header Indicates if header is provided in data
     * @param title An optional title for the report, used only in the formatted reports.
     * @param summary An optional summary for the report, used only in the formatted reports.
     * @return Map<String, List<String>>; A map where the key is the column name and the value is a list of strings containing the column values.
     */
    fun generateReport(data: Map<String, List<String>>, destination: String, header: Boolean, title: String? = null, summary: String? = null) :Map<String, List<String>>
    /**
     *  * Generates a report based on the provided data and writes it to the specified destination.
     *   This method uses a configuration file to determine which columns to include, the calculations to perform,
     *   and the desired formatting for the report.
     * @param data A map where the key is the column name and the value is a list of strings representing the column data.
     *              All lists in the map should have the same size to ensure proper row alignment.
     * @param destination The file path where the report will be saved.
     * @param header Indicates if header is provided in data.
     * @param title An optional title for the report, used only in the formatted reports.
     * @param summary An optional summary for the report, used only in the formatted reports.
     * @param config The path to the configuration file that specifies which columns to export,
     *  *               which calculations to perform, and the formatting options for the report.
     *  *               Example content of the config file:
     *  *               Columns for export: 0,1,2,3,4,5,6,7
     *  *               Calculations: SUM(2,3,4)
     *  *               Title: NaslovNovi
     *  *               Summary: ZakljucakNovi
     *  *               Formatting: Italic(1) Bold(5) Italic(summary) Bold(2) color_red(2)
     *  @throws UnsupportedOperationException if the user specifies formatting in config file for format that does not support formatting.
     *  @return Map<String, List<String>>; A map where the key is the column name and the value is a list of strings containing the column values.
     * */
    fun generateReport(data: Map<String, List<String>>, destination: String, header: Boolean, title: String? = null, summary: String? = null, config: String):Map<String, List<String>>{
        val dataAfterConfig = readConfig(data,config)
        val dataAfterFormatting = generateReportWithFormatting(dataAfterConfig!!, destination, header, this.titleProperty, this.summaryProperty,this.formattingList )
        return dataAfterFormatting
    }
    /**
     * Generates a report based on the provided JSON data and writes it to the specified destination.
     * If a configuration file is provided, the report will be generated with additional options such as
     * selected columns, calculations, title, summary, and formatting. If no configuration file is provided,
     * the report is generated with the default settings.
     *
     * @param jsonData A JSON string representing the data for the report. It should contain structured data
     *                 that can be parsed into a map of columns and their respective values.
     * @param destination The file path where the report will be saved.
     * @param header Indicates if header is provided in the data.
     * @param title An optional title for the report, used only in the formatted reports.
     * @param summary An optional summary for the report, used only in the formatted reports.
     * @param config An optional path to a configuration file that specifies which columns to export,
     *               which calculations to perform, and the formatting options for the report. If null,
     *               the report will be generated without any additional configuration.
     * @throws UnsupportedOperationException if the user specifies formatting for columns or elements
     *      *         that do not support it.
     * @return A map where the key is the column name and the value is a list of strings containing the column values.
     *         The format of the returned map will depend on the provided configuration.
     */
    fun generateReport(jsonData: String, destination: String, header: Boolean, title: String? = null, summary: String? = null, config: String? = null):Map<String, List<String>>{
        val preparedJsonData = prepareJsonData(jsonData)
        if (config!=null) {
            val dataReport = generateReport(preparedJsonData, destination, header, title, summary, config)
            return dataReport
        }
        else{
            val dataReport = generateReport(preparedJsonData, destination, header, title, summary)
            return dataReport
        }

    }
    /**
     * Generates a report based on the provided database query result (ResultSet) and writes it to the specified destination.
     * If a configuration file is provided, the report will be generated with additional options such as
     * selected columns, calculations, title, summary, and formatting. If no configuration file is provided,
     * the report will be generated with the default settings.
     *
     * @param data A `ResultSet` object representing the result of a database query. It contains rows of data
     *             that will be processed to generate the report.
     * @param destination The file path where the report will be saved.
     * @param header Indicates if header is provided in the data.
     * @param title An optional title for the report, used only in the formatted reports.
     * @param summary An optional summary for the report, used only in the formatted reports.
     * @param config An optional path to a configuration file that specifies which columns to export,
     *               which calculations to perform, and the formatting options for the report. If null,
     *               the report will be generated without any additional configuration.
     * @throws UnsupportedOperationException if the user specifies formatting for columns or elements
     *               that do not support it.
     * @return A map where the key is the column name and the value is a list of strings containing the column values.
     *         The format of the returned map will depend on the provided configuration.
     */
    fun generateReport(data: ResultSet, destination: String, header: Boolean, title: String? = null, summary: String? = null, config: String? = null):Map<String, List<String>>{
        val preparedData = prepareData(data)
        if(config!=null) {
            val dataReport = generateReport(preparedData, destination, header, title, summary, config)
            return dataReport
        }
        else{
            val dataReport = generateReport(preparedData, destination, header, title, summary)
            return dataReport
        }
    }

    /**
     * Generates a report based on the provided data and applies the specified formatting.
     * The formatting is provided in the form of a map, where the key is the type of formatting (e.g., "Italic", "Bold"),
     * and the value is a list of elements (columns or summary) that the formatting should be applied to.
     *
     * @param data A map where the key is the column name and the value is a list of strings representing the column data.
     *             All lists in the map should have the same size to ensure proper row alignment.
     * @param destination The file path where the report will be saved.
     * @param header Indicates if header is provided in the data.
     * @param title An optional title for the report, used only in the formatted reports.
     * @param summary An optional summary for the report, used only in the formatted reports.
     * @param formattingList A map where the key is the formatting type (e.g., "Italic", "Bold", "color_red"),
     *                       and the value is a list of elements (columns or summary) to which the formatting will be applied.
     *                       For example, the value could include specific columns or the summary text to apply formatting.
     * @throws UnsupportedOperationException if the user specifies formatting for columns or elements
     *      *         that do not support it.
     * @return A map where the key is the column name and the value is a list of strings containing the formatted column values.
     */
    fun generateReportWithFormatting(data: Map<String, List<String>>, destination: String, header: Boolean, title: String? = null, summary: String? = null, formattingList:Map<String, List<String>>?):Map<String, List<String>>

    /**
     * Adds a new column to the provided data map based on the configuration file specified by the path.
     * The column name and its corresponding values are read from the configuration file and then added
     * to the existing data map. After the column is added, the report is generated using the updated data map.
     *
     * @param data A map where the key is the column name and the value is a list of strings representing the column data.
     *             All lists in the map should have the same size to ensure proper row alignment.
     * @param destination The file path where the report will be saved.
     * @param header Indicates if header is provided in the data.
     * @param title An optional title for the report, used only in the formatted reports.
     * @param summary An optional summary for the report, used only in the formatted reports.
     * @param pathtoConfigColumn The file path to the configuration file that specifies the new column's name and values.
     *                           The file must contain lines formatted as follows:
     *                           - "ColumnName: <column name>"
     *                           - "Values: <comma-separated list of values>"
     *
     * @return A map where the key is the column name and the value is a list of strings containing the updated column values.
     *         The new column will be added to the map.
     */
    fun addColumn(data :Map<String, List<String>>, destination: String, header: Boolean, title: String? = null, summary: String? = null, pathtoConfigColumn: String): Map<String, List<String>>{
        val dataForAdd = data as MutableMap<String, List<String>>
        val configPath = pathtoConfigColumn
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
                        .map { it.trim()}
                }
            }
        }
        dataForAdd[columnName] = values
        val result = generateReport(dataForAdd, destination, header, title, summary)
        return result
    }

    private fun prepareJsonData(jsonData: String): Map<String, List<String>> {

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
                val calculationInstance = CalculationModel()
                val dataAfterSum = calculationInstance.sumCalculate(data, columns, calculationColumns)
                try {
                    if (formattingList.isNotEmpty())
                        this.formattingList = formattingList
                    else
                        throw MyException("emptyArgument")
                } catch (e: MyException) {
                    e.logError()
                }
                return dataAfterSum
            } else if (calculation == "AVG") {
                val calculationInstance = CalculationModel()
                val dataAfterAvg = calculationInstance.avgCalculate(data, columns, calculationColumns)
                try {
                    if (formattingList.isNotEmpty())
                        this.formattingList = formattingList
                    else
                        throw MyException("emptyArgument")
                } catch (e: MyException) {
                    e.logError()
                }
                return dataAfterAvg
            } else if (calculation == "COUNT") {
                val calculationInstance = CalculationModel()
                val dataAfterCount = calculationInstance.countCalculate(data, columns, calculationColumns,sign_Operator, numberForOperation)
                try {
                    if (formattingList.isNotEmpty())
                        this.formattingList = formattingList
                    else
                        throw MyException("emptyArgument")
                } catch (e: MyException) {
                    e.logError()
                }
                return dataAfterCount
            } else if (calculation == "SUB") {
                val calculationInstance = CalculationModel()
                val dataAfterSub = calculationInstance.subCalculate(data, columns, calculationColumns)
                try {
                    if (formattingList.isNotEmpty())
                        this.formattingList = formattingList
                    else
                        throw MyException("emptyArgument")
                } catch (e: MyException) {
                    e.logError()
                }
                return dataAfterSub
            } else if (calculation == "MUL") {
                val calculationInstance = CalculationModel()
                val dataAfterMul = calculationInstance.mulCalculate(data, columns, calculationColumns)
                try {
                    if (formattingList.isNotEmpty())
                        this.formattingList = formattingList
                    else
                        throw MyException("emptyArgument")
                } catch (e: MyException) {
                    e.logError()
                }
                return dataAfterMul
            } else if (calculation == "DIV") {
                val calculationInstance = CalculationModel()
                val dataAfterDiv = calculationInstance.divCalculate(data, columns, calculationColumns)
                try {
                    if (formattingList.isNotEmpty())
                        this.formattingList = formattingList
                    else
                        throw MyException("emptyArgument")
                } catch (e: MyException) {
                    e.logError()
                }
                return dataAfterDiv
            } else {
                try {
                    throw MyException("emptyArgument")
                } catch (e: MyException) {
                    e.logError()
                }
            }

            return null
        }


    }
//     ./testApp/src/main/resources/izvorPodataka.json
/**
 *         val configPath = "D:\\Marko workspace\\Fakultet\\Projekti\\softverskekomponente_tim_markostojicic_vidanstojic\\softverskekomponente_prvi_projekat\\testApp\\src\\main\\resources\\config.txt"
 *         val configPath = "C:/Users/vidan_gofx79m/Desktop/softverske komponente/softverskekomponente_tim_markostojicic_vidanstojic/softverskekomponente_prvi_projekat/testApp/src/main/resources/column.txt"
 * */