package org.example.testApp

import specification.ReportInterface
import java.io.File
import java.sql.DriverManager
import java.util.*

val exporterServices = mutableMapOf<String, ReportInterface> ()
val serviceLoader = ServiceLoader.load(ReportInterface::class.java)
val scanner = Scanner(System.`in`)


fun askForFormat(flagForConfig: Boolean, dataDataBase: Map<String, List<String>> = mutableMapOf(), dataJson:String = "", flagForJson :Boolean){

    println("1. TXT")
    println("2. CSV")
    println("3. PDF")
    println("4. EXCEL")
    println("-----------------------------------")
    print("Choose your format: ")
    var formatResponse = scanner.nextInt()
    println("---------------------------------")
    when(formatResponse){
        1 -> {
            if(!flagForConfig) {
                if (flagForJson) {
                    var dataResult = exporterServices["TXT"]?.generateReport(dataJson, "izlazTxtNormal.txt", true)
                    print("Do you want to add a new column(1 - yes, 0 -no): ")
                    var newColumnResponse = scanner.nextInt()
                    println("---------------------------------")
                    if (newColumnResponse == 1) {
                        if (dataResult != null) {
                            exporterServices["TXT"]?.addColumn(
                                dataResult,
                                "izlazAddColumn.txt",
                                true,
                                pathtoConfigColumn = "D:\\Marko workspace\\Fakultet\\Projekti\\softverskekomponente_tim_markostojicic_vidanstojic\\softverskekomponente_prvi_projekat\\testApp\\src\\main\\resources\\column.txt"
                            )
                        }
                    }
                }
                else {
                    var dataResult = exporterServices["TXT"]?.generateReport(dataDataBase, "izlazTxtNormal.txt", true)
                    print("Do you want to add a new column(1 - yes, 0 -no): ")
                    var newColumnResponse = scanner.nextInt()
                    println("---------------------------------")
                    if (newColumnResponse == 1) {
                        if (dataResult != null) {
                            exporterServices["TXT"]?.addColumn(
                                dataResult,
                                "izlazAddColumn.txt",
                                true,
                                pathtoConfigColumn = "D:\\Marko workspace\\Fakultet\\Projekti\\softverskekomponente_tim_markostojicic_vidanstojic\\softverskekomponente_prvi_projekat\\testApp\\src\\main\\resources\\column.txt"
                            )
                        }
                    }
                }
            }
            else{
                val inputStream = object {}.javaClass.getResourceAsStream("/config.txt")
                if (inputStream != null) {

                    val configPath = "D:\\Marko workspace\\Fakultet\\Projekti\\softverskekomponente_tim_markostojicic_vidanstojic\\softverskekomponente_prvi_projekat\\testApp\\src\\main\\resources\\config.txt"
                    //val configPath = "C:/Users/vidan_gofx79m/Desktop/softverske komponente/softverskekomponente_tim_markostojicic_vidanstojic/softverskekomponente_prvi_projekat/testApp/src/main/resources/config.txt"

                    if(flagForJson) exporterServices["TXT"]?.generateReport(dataJson, "izlazTxtConfig.txt", true, title = "", summary = "", config = configPath)
                    else exporterServices["TXT"]?.generateReport(dataDataBase, "izlazTxtConfig.txt", true, title = "", summary = "", config = configPath)
                } else {
                    println("Fajl nije pronađen!")
                }
            }
        }
        2 -> {
            if(!flagForConfig){
                if(flagForJson){
                    var dataResult = exporterServices["CSV"]?.generateReport(dataJson, "izlazCsvNormal.csv", true)
                    print("Do you want to add a new column(1 - yes, 0 -no): ")
                    var newColumnResponse = scanner.nextInt()
                    println("---------------------------------")
                    if (newColumnResponse == 1) {
                        if (dataResult != null) {
                            exporterServices["CSV"]?.addColumn(
                                dataResult,
                                "izlazAddColumn.csv",
                                true,
                                pathtoConfigColumn = "D:\\Marko workspace\\Fakultet\\Projekti\\softverskekomponente_tim_markostojicic_vidanstojic\\softverskekomponente_prvi_projekat\\testApp\\src\\main\\resources\\column.txt"
                            )
                        }
                    }
                }
                else{
                    var dataResult = exporterServices["CSV"]?.generateReport(dataDataBase, "izlazCsvNormal.csv", true)
                    print("Do you want to add a new column(1 - yes, 0 -no): ")
                    var newColumnResponse = scanner.nextInt()
                    println("---------------------------------")
                    if (newColumnResponse == 1) {
                        if (dataResult != null) {
                            exporterServices["CSV"]?.addColumn(
                                dataResult,
                                "izlazAddColumn.csv",
                                true,
                                pathtoConfigColumn = "D:\\Marko workspace\\Fakultet\\Projekti\\softverskekomponente_tim_markostojicic_vidanstojic\\softverskekomponente_prvi_projekat\\testApp\\src\\main\\resources\\column.txt"
                            )
                        }
                    }
                }
            }
            else{
                val inputStream = object {}.javaClass.getResourceAsStream("/config.txt")
                if (inputStream != null) {

                    val configPath = "D:\\Marko workspace\\Fakultet\\Projekti\\softverskekomponente_tim_markostojicic_vidanstojic\\softverskekomponente_prvi_projekat\\testApp\\src\\main\\resources\\config.txt"
                    //val configPath = "C:/Users/vidan_gofx79m/Desktop/softverske komponente/softverskekomponente_tim_markostojicic_vidanstojic/softverskekomponente_prvi_projekat/testApp/src/main/resources/config.txt"
                    if(flagForJson) exporterServices["CSV"]?.generateReport(dataJson, "izlazCsvConfig.csv", true, title = "", summary = "", config = configPath)
                    else exporterServices["CSV"]?.generateReport(dataDataBase, "izlazCsvConfig.csv", true, title = "", summary = "", config = configPath)
                } else {
                    println("Fajl nije pronađen!")
                }
            }
        }
        3 -> {
            if(!flagForConfig){
                if(flagForJson){
                   var dataResult =  exporterServices["PDF"]?.generateReport(dataJson, "izlazPdfNormal.pdf", true)
                    print("Do you want to add a new column(1 - yes, 0 -no): ")
                    var newColumnResponse = scanner.nextInt()
                    println("---------------------------------")
                    if (newColumnResponse == 1) {
                        if (dataResult != null) {
                            exporterServices["PDF"]?.addColumn(
                                dataResult,
                                "izlazAddColumn.pdf",
                                true,
                                pathtoConfigColumn = "D:\\Marko workspace\\Fakultet\\Projekti\\softverskekomponente_tim_markostojicic_vidanstojic\\softverskekomponente_prvi_projekat\\testApp\\src\\main\\resources\\column.txt"
                            )
                        }
                    }
                }
                else{
                    var dataResult = exporterServices["PDF"]?.generateReport(dataDataBase, "izlazPdfNormal.pdf", true)
                    print("Do you want to add a new column(1 - yes, 0 -no): ")
                    var newColumnResponse = scanner.nextInt()
                    println("---------------------------------")
                    if (newColumnResponse == 1) {
                        if (dataResult != null) {
                            exporterServices["PDF"]?.addColumn(
                                dataResult,
                                "izlazAddColumn.pdf",
                                true,
                                pathtoConfigColumn = "D:\\Marko workspace\\Fakultet\\Projekti\\softverskekomponente_tim_markostojicic_vidanstojic\\softverskekomponente_prvi_projekat\\testApp\\src\\main\\resources\\column.txt"
                            )
                        }
                    }
                }
            }
            else{
                val inputStream = object {}.javaClass.getResourceAsStream("/config.txt")
                if (inputStream != null) {

                    val configPath = "D:\\Marko workspace\\Fakultet\\Projekti\\softverskekomponente_tim_markostojicic_vidanstojic\\softverskekomponente_prvi_projekat\\testApp\\src\\main\\resources\\config.txt"
                    //val configPath = "C:/Users/vidan_gofx79m/Desktop/softverske komponente/softverskekomponente_tim_markostojicic_vidanstojic/softverskekomponente_prvi_projekat/testApp/src/main/resources/config.txt"
                    if(flagForJson) exporterServices["PDF"]?.generateReport(dataJson, "izlazPdfConfig.pdf", true, title = "", summary = "", config = configPath)
                    else exporterServices["PDF"]?.generateReport(dataDataBase, "izlazPdfConfig.pdf", true, title = "", summary = "", config = configPath)
                } else {
                    println("Fajl nije pronađen!")
                }
            }
        }
        4 -> {
            if(!flagForConfig){
                if(flagForJson){
                   var dataResult = exporterServices["XLS"]?.generateReport(dataJson, "izlazExcelNormal.xls", true)
                    print("Do you want to add a new column(1 - yes, 0 -no): ")
                    var newColumnResponse = scanner.nextInt()
                    println("---------------------------------")
                    if (newColumnResponse == 1) {
                        if (dataResult != null) {
                            exporterServices["XLS"]?.addColumn(
                                dataResult,
                                "izlazAddColumn.xls",
                                true,
                                pathtoConfigColumn = "D:\\Marko workspace\\Fakultet\\Projekti\\softverskekomponente_tim_markostojicic_vidanstojic\\softverskekomponente_prvi_projekat\\testApp\\src\\main\\resources\\column.txt"
                            )
                        }
                    }
                }
                else{
                    var dataResult = exporterServices["XLS"]?.generateReport(dataDataBase, "izlazExcelNormal.xls", true)
                    print("Do you want to add a new column(1 - yes, 0 -no): ")
                    var newColumnResponse = scanner.nextInt()
                    println("---------------------------------")
                    if (newColumnResponse == 1) {
                        if (dataResult != null) {
                            exporterServices["XLS"]?.addColumn(
                                dataResult,
                                "izlazAddColumn.xls",
                                true,
                                pathtoConfigColumn = "D:\\Marko workspace\\Fakultet\\Projekti\\softverskekomponente_tim_markostojicic_vidanstojic\\softverskekomponente_prvi_projekat\\testApp\\src\\main\\resources\\column.txt"
                            )
                        }
                    }
                }
            }
            else{
                val inputStream = object {}.javaClass.getResourceAsStream("/config.txt")
                if (inputStream != null) {

                    val configPath = "D:\\Marko workspace\\Fakultet\\Projekti\\softverskekomponente_tim_markostojicic_vidanstojic\\softverskekomponente_prvi_projekat\\testApp\\src\\main\\resources\\config.txt"
                    //val configPath = "C:/Users/vidan_gofx79m/Desktop/softverske komponente/softverskekomponente_tim_markostojicic_vidanstojic/softverskekomponente_prvi_projekat/testApp/src/main/resources/config.txt"
                    if(flagForJson) exporterServices["XLS"]?.generateReport(dataJson, "izlazExcelConfig.xlsx", true, title = "", summary = "", config = configPath)
                    else exporterServices["XLS"]?.generateReport(dataDataBase, "izlazExcelConfig.xlsx", true, title = "", summary = "", config = configPath)
                } else {
                    println("Fajl nije pronađen!")
                }
            }
        }
    }

}









fun loadData(flagForConfig : Boolean){


    println("1. Database")
    println("2. Json")
    println("--------------------------------")
    print("Choose the source of your data: ")
    var response = scanner.nextInt()
    println("--------------------------------")
    when(response){
        1 -> {   /// DEO ZA BAZU PODATAKA
            print("Write path to your database: ")
            var urlSc = scanner.next()
            print("Write your user name: ")
            var userSc = scanner.next()
            print("Write your password: ")
            var passwordSc = readlnOrNull()
            if (passwordSc == null)
                passwordSc = ""
            val data = mutableMapOf<String, MutableList<String>>()
            DriverManager.getConnection(urlSc, userSc, passwordSc.toString()).use { connection ->
                val statement = connection.createStatement()
                val resultSet = statement.executeQuery("SELECT * FROM raspored")

                val columnCount = resultSet.metaData.columnCount
                while (resultSet.next()) {
                    for (i in 1..columnCount) {
                        val columnName = resultSet.metaData.getColumnName(i)
                        if (columnName !in data) {
                            data[columnName] = mutableListOf()
                        }
                        data[columnName]?.add(resultSet.getString(i))
                    }
                }
            }
            askForFormat(flagForConfig = flagForConfig, dataDataBase = data,flagForJson = false)

        }
        2 -> { /// DEO ZA JSON
            print("Write your json path: ")
            var response2 = scanner.next()
            println("-----------------------------------")
            val file = File(response2)
            val jsonData = file.readText()
            val data = jsonData
            askForFormat(flagForConfig = flagForConfig, dataJson = data,flagForJson = true)
        }
    }
}


fun main() {
   serviceLoader.forEach{
            service ->
        exporterServices[service.implementationName] = service
    }
    println(exporterServices.keys)

    var scanner = Scanner(System.`in`)
    while(true){
        println("----------------------------------------------")
        println("1. Generate your report.")
        println("2. Generate your report with additional configuration.")
        println("3. Exit.")
        println("----------------------------------------------")
        print("Choose your option: ")
        var response = scanner.nextInt()
        println("----------------------------------------------")
        when (response) {
            1 -> loadData(false)
            2 -> loadData(true)
            3 -> break
            else -> println("Unknown command.")
        }
    }

}
/**
 *          BITNE PUTANJE:
 *  1) Za ucitavanje baze podataka:
 *    jdbc:mysql://localhost:3306/raspored
 *    jdbc:mysql://localhost:3306/podaciZaIzvestaj
 *  2) Putanje za json
 *    ./testApp/src/main/resources/izvorPodataka.json ----> ovo je putanja za json koja se pise kada pokrecemo program u intellij-u
 *   ../../src/main/resources/izvorPodataka.json      ----> ovo je putanja za json koja se pise kada pokrecemo program preko jar-a u terminalu
 *  3) Putanje za config fajl:
 *     val configPath = "D:\\Marko workspace\\Fakultet\\Projekti\\softverskekomponente_tim_markostojicic_vidanstojic\\softverskekomponente_prvi_projekat\\testApp\\src\\main\\resources\\config.txt"
 *     val configPath = "C:/Users/vidan_gofx79m/Desktop/softverske komponente/softverskekomponente_tim_markostojicic_vidanstojic/softverskekomponente_prvi_projekat/testApp/src/main/resources/column.txt"
 * */

