package specification

import calculation.Calculation
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
    var formattingList : Map<String, List<String>>

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
        generateReportWithFormatting(dataAfterConfig!!, destination, header, this.titleProperty, this.summaryProperty, this.formattingList)
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

    private fun readConfig(data: Map<String, List<String>>, config: String):Map<String, List<String>>? {
        val lines = File(config).readLines()

        val columns = mutableListOf<Int>()
        var calculation: String? = null
        val calculationColumns = mutableListOf<Int>()
        var customTitle: String? = null
        var customSummary: String? = null
        var formattingType: String? = null
        val formattingList = mutableMapOf<String, MutableList<String>>()
        var textForFormatting: String? = null
        var sign_Operator: String? = null
        var numberForOperation: Int? = null

        val columnsRegex = Regex("""Columns for export:\s*([\d,]+)""")
        val calculationRegex = Regex("""Calculations:\s*(\w+)\((\d+(?:,(?:\d+|[<>=]\d+))*)\s*(?:([<>=]+)\s*(\d+))?\)""")
        val titleRegex = Regex("""Title:\s*(.*)""")
        val summaryRegex = Regex("""Summary:\s*(.*)""")
        //   val formattingRegex = Regex("""Formatting:\s*(\w+)\((\w+)\)""")
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
                /*formattingRegex.matches(line) -> {
                    val match = formattingRegex.find(line)
                    formattingType = match?.groups?.get(1)?.value ?: ""
                    textForFormatting = match?.groups?.get(2)?.value ?: ""
                }*/
                formattingRegex.containsMatchIn(line) -> {
                    // `findAll` uzima sva podudaranja za regex u liniji
                    val matches = formattingRegex.findAll(line)
                    matches.forEach { match ->
                        // Proveravamo da li su grupe validne i dodeljujemo vrednosti
                        val formatType = match.groups[1]?.value ?: ""
                        val targetText = match.groups[2]?.value ?: ""
                        if (formatType.isNotEmpty() && targetText.isNotEmpty()) {
                            formattingList.computeIfAbsent(formatType) { mutableListOf() }.add(targetText)
                        }
                    }
                }
            }
        }
            // Ispis podataka za proveru
            // println("FORMATIRANJE: $formattingType za text(kolonu) $textForFormatting")

            this.titleProperty = customTitle!!
            this.summaryProperty = customSummary!!
            /** Dodati proveru toga da li je korisnik uopste napisao u configuration fajl neku kalkulaciju/title/summary/formatiranje ili nije.Ukoliko nije napisao onda mu poslati print neki da je pogresio ili slicno.*/


            if (calculation == "SUM") {
                println("Usao u sum")
                var calculationObject: Calculation = Calculation(data, columns, calculationColumns)
                val dataAfterSum = calculationObject.sumCalculate()
                if (formattingList.isNotEmpty()) {
                    println("Poslat je zeljeni nacin formatiranja.")
                    this.formattingList = formattingList

                    println("Mapa formattingList: "+ this.formattingList.toString())
                } else println("Nema formatiranja u config fajlu.")


                return dataAfterSum
            } else if (calculation == "AVG") {
                var calculationObject: Calculation = Calculation(data, columns, calculationColumns)
                val dataAfterAvg = calculationObject.avgCalculate()
                if ((!formattingType.isNullOrEmpty()) || (!textForFormatting.isNullOrEmpty())) {
                    println("Poslat je zeljeni nacin formatiranja.")
                    //   this.formattingNameProperty = formattingType
                    // this.formattingTextProperty = textForFormatting
                    this.formattingList = formattingList
                }
                return dataAfterAvg
            } else if (calculation == "COUNT") {
                var calculationObject: Calculation = Calculation(data, columns, calculationColumns)
                val dataAfterCount = calculationObject.countCalculate(sign_Operator, numberForOperation)
                if ((!formattingType.isNullOrEmpty()) || (!textForFormatting.isNullOrEmpty())) {
                    println("Poslat je zeljeni nacin formatiranja.")
                    //   this.formattingNameProperty = formattingType
                    // this.formattingTextProperty = textForFormatting
                    this.formattingList = formattingList
                }
                return dataAfterCount
            } else if (calculation == "SUB") {
                var calculationObject: Calculation = Calculation(data, columns, calculationColumns)
                val dataAfterSub = calculationObject.subCalculate()
                if ((!formattingType.isNullOrEmpty()) || (!textForFormatting.isNullOrEmpty())) {
                    println("Poslat je zeljeni nacin formatiranja.")
                    //   this.formattingNameProperty = formattingType
                    // this.formattingTextProperty = textForFormatting
                    this.formattingList = formattingList
                }
                return dataAfterSub
            } else if (calculation == "MUL") {
                var calculationObject: Calculation = Calculation(data, columns, calculationColumns)
                val dataAfterMul = calculationObject.mulCalculate()
                if ((!formattingType.isNullOrEmpty()) || (!textForFormatting.isNullOrEmpty())) {
                    println("Poslat je zeljeni nacin formatiranja.")
                    //   this.formattingNameProperty = formattingType
                    // this.formattingTextProperty = textForFormatting
                    this.formattingList = formattingList
                }
                return dataAfterMul
            } else if (calculation == "DIV") {
                var calculationObject: Calculation = Calculation(data, columns, calculationColumns)
                val dataAfterDiv = calculationObject.divCalculate()
                if ((!formattingType.isNullOrEmpty()) || (!textForFormatting.isNullOrEmpty())) {
                    println("Poslat je zeljeni nacin formatiranja.")
                    //   this.formattingNameProperty = formattingType
                    // this.formattingTextProperty = textForFormatting
                    this.formattingList = formattingList
                }
                return dataAfterDiv
            } else {
                return null
                // mnozenje, deljenje, oduzimanje??
            }


        }


    }
//    /izvorPodataka.json