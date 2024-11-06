package spec

import model.Column
import model.Row
import java.awt.Color
import java.io.File
import java.sql.ResultSet
import java.sql.ResultSetMetaData


interface ReportInterface {
    abstract val implementationName: String
    abstract val formattingFlag: Boolean
    abstract val reportHeader: String
    abstract val reportSummary: String

    /**
     * kalkulacije(sum, average, count, mnozenje, deljenje, oduzimanje) - proveriti na koji nacin se rade kalkulacije
     * */

    fun generateReport(data: Map<String, List<Row>>, fileDestination: String)

    fun generateReport(data: ResultSet, fileDestination: String){
        var prepareData = prepareData(data)
        generateReport(prepareData, fileDestination)
    }


    fun generateReport(dataJson: String){
        var prepareData = prepareData(dataJson)
        generateReport(prepareData, dataJson)
    }


    fun prepareData(resultSet: ResultSet): Map<String, List<Row>> {
        val reportData = mutableMapOf<String, MutableList<Row>>()

        val metaData: ResultSetMetaData = resultSet.metaData
        val columnCount = metaData.columnCount

        for (i in 1..columnCount) {
            val columnName = metaData.getColumnName(i)
            var column = Column(columnName, mutableListOf())

            reportData[column.name] = column.rows
        }

        while (resultSet.next()) {
            for (i in 1..columnCount) {
                val columnName = metaData.getColumnName(i)
                var row = Row(resultSet.getString(i))
                reportData[columnName]!!.add(row)
            }
        }

        return reportData
    }


    fun prepareData(dataJson : String): Map<String, List<Row>>{
        val reportData = mutableMapOf<String, MutableList<Row>>()

        /**
         * Dodati logiku za pretvare json fajla u mapu, odnosno prebacivanje informacija izjava u mapu.
         * */


        return reportData
    }

    /**
     * Dodati funkciju prepereData za listu u listi.
     * Da bi ta funkcija radila, potrebna ti je i metoda generateReport koja prima kao argument listu u listi.
     * */

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



}