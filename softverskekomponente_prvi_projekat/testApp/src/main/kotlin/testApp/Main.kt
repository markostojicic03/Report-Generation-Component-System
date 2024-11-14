package org.example.testApp

import specification.ReportInterface
import java.io.File
import java.sql.DriverManager
import java.util.*

val exporterServices = mutableMapOf<String, ReportInterface> ()
val serviceLoader = ServiceLoader.load(ReportInterface::class.java)

fun loadData(flagForConfig : Boolean){


    var scanner = Scanner(System.`in`)
    println("1. TXT")
    println("2. Database")
    println("3. Json")
    println("--------------------------------")
    print("Choose the source of your data:")
    var response = scanner.nextInt()
    println("--------------------------------")
    when(response){
        1 ->{
            print("Write your file path:")
            var response2 = scanner.next()

        }
        2 -> {
            print("Write path to your database: ")
            var urlSc = scanner.next()
            //val url = "jdbc:mysql://localhost:3306/raspored"
            print("Write your user name: ")
            var userSc = scanner.next()
            val user = "root"
            print("Write your password: ")
            var passwordSc = readlnOrNull()
            if (passwordSc == null)
                passwordSc = ""
            val password = ""
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
            println("1. TXT")
            println("2. CSV")
            println("3. PDF")
            println("4. EXCEL")
            println("-----------------------------------")
            print("Choose your format: ")
            var response3 = scanner.nextInt()
            println("---------------------------------")
            when(response3){
                1 -> {
                    if(!flagForConfig){
                        exporterServices["TXT"]?.generateReport(data, "izlazCsvNormal.txt", true)
                    }
                    else{
                        val inputStream = object {}.javaClass.getResourceAsStream("/config.txt")
                        if (inputStream != null) {

                            //val configPath = "D:\\Marko workspace\\Fakultet\\Projekti\\softverskekomponente_tim_markostojicic_vidanstojic\\softverskekomponente_prvi_projekat\\testApp\\src\\main\\resources\\config.txt"
                            val configPath = "C:/Users/vidan_gofx79m/Desktop/softverske komponente/softverskekomponente_tim_markostojicic_vidanstojic/softverskekomponente_prvi_projekat/testApp/src/main/resources/config.txt"
                            exporterServices["TXT"]?.generateReport(data, "izlazTxtConfig.txt", true, title = "", summary = "", config = configPath)
                        } else {
                            println("Fajl nije pronađen!")
                        }
                    }
                }
                2 -> {
                    if(!flagForConfig){
                        exporterServices["CSV"]?.generateReport(data, "izlazCsvNormal.txt", true)
                    }
                    else{
                        val inputStream = object {}.javaClass.getResourceAsStream("/config.txt")
                        if (inputStream != null) {

                            val configPath = "D:\\Marko workspace\\Fakultet\\Projekti\\softverskekomponente_tim_markostojicic_vidanstojic\\softverskekomponente_prvi_projekat\\testApp\\src\\main\\resources\\config.txt"
                            //val configPath = "C:/Users/vidan_gofx79m/Desktop/softverske komponente/softverskekomponente_tim_markostojicic_vidanstojic/softverskekomponente_prvi_projekat/testApp/src/main/resources/config.txt"
                            exporterServices["CSV"]?.generateReport(data, "izlazCsvConfig.csv", true, title = "", summary = "", config = configPath)
                        } else {
                            println("Fajl nije pronađen!")
                        }
                    }
                }
                3 -> {
                    if(!flagForConfig){
                        exporterServices["PDF"]?.generateReport(data, "izlazPdfNormal.pdf", true)
                    }
                    else{
                        val inputStream = object {}.javaClass.getResourceAsStream("/config.txt")
                        if (inputStream != null) {

                            val configPath = "D:\\Marko workspace\\Fakultet\\Projekti\\softverskekomponente_tim_markostojicic_vidanstojic\\softverskekomponente_prvi_projekat\\testApp\\src\\main\\resources\\config.txt"
                            //val configPath = "C:/Users/vidan_gofx79m/Desktop/softverske komponente/softverskekomponente_tim_markostojicic_vidanstojic/softverskekomponente_prvi_projekat/testApp/src/main/resources/config.txt"
                            exporterServices["PDF"]?.generateReport(data, "izlazPdfConfig.pdf", true, title = "", summary = "", config = configPath)
                        } else {
                            println("Fajl nije pronađen!")
                        }
                    }
                }
                4 -> {
                    if(!flagForConfig){
                        exporterServices["XLS"]?.generateReport(data, "izlazExcelNormal.xls", true)
                    }
                    else{
                        val inputStream = object {}.javaClass.getResourceAsStream("/config.txt")
                        if (inputStream != null) {

                            val configPath = "D:\\Marko workspace\\Fakultet\\Projekti\\softverskekomponente_tim_markostojicic_vidanstojic\\softverskekomponente_prvi_projekat\\testApp\\src\\main\\resources\\config.txt"
                            //val configPath = "C:/Users/vidan_gofx79m/Desktop/softverske komponente/softverskekomponente_tim_markostojicic_vidanstojic/softverskekomponente_prvi_projekat/testApp/src/main/resources/config.txt"
                            exporterServices["XLS"]?.generateReport(data, "izlazExcelConfig.xlsx", true, title = "", summary = "", config = configPath)
                        } else {
                            println("Fajl nije pronađen!")
                        }
                    }
                }
            }

        }
        3 -> {
            print("Write your json path: ")
            var response2 = scanner.next()
            println("-----------------------------------")
            val file = File(response2)
//  ./testApp/src/main/resources/izvorPodataka.json
            val jsonData = file.readText()
            val data = jsonData
            println("1. TXT")
            println("2. CSV")
            println("3. PDF")
            println("4. EXCEL")
            println("-----------------------------------")
            print("Choose your format: ")
            var response3 = scanner.nextInt()
            println("---------------------------------")

            when(response3){
                1 ->{
                    println("Usao u txt export services u loadData.")
                    if(!flagForConfig){
                        exporterServices["TXT"]?.generateReport(data, "izlazCsvNormal.txt", true)
                    }
                    else{
                        val inputStream = object {}.javaClass.getResourceAsStream("/config.txt")
                        if (inputStream != null) {

                            //val configPath = "D:\\Marko workspace\\Fakultet\\Projekti\\softverskekomponente_tim_markostojicic_vidanstojic\\softverskekomponente_prvi_projekat\\testApp\\src\\main\\resources\\config.txt"
                           val configPath = "C:/Users/vidan_gofx79m/Desktop/softverske komponente/softverskekomponente_tim_markostojicic_vidanstojic/softverskekomponente_prvi_projekat/testApp/src/main/resources/config.txt"
                            exporterServices["TXT"]?.generateReport(data, "izlazTxtConfig.txt", true, title = "", summary = "", config = configPath)
                        } else {
                            println("Fajl nije pronađen!")
                        }
                    }
                }
                2 -> {
                    println("Usao u csv export services u loadData.")
                    if(!flagForConfig){
                        exporterServices["CSV"]?.generateReport(data, "izlazCsvNormal.txt", true)
                    }
                    else{
                        val inputStream = object {}.javaClass.getResourceAsStream("/config.txt")
                        if (inputStream != null) {

                            val configPath = "D:\\Marko workspace\\Fakultet\\Projekti\\softverskekomponente_tim_markostojicic_vidanstojic\\softverskekomponente_prvi_projekat\\testApp\\src\\main\\resources\\config.txt"
                            // val configPath = "C:/Users/vidan_gofx79m/Desktop/softverske komponente/softverskekomponente_tim_markostojicic_vidanstojic/softverskekomponente_prvi_projekat/testApp/src/main/resources/config.txt"
                            exporterServices["CSV"]?.generateReport(data, "izlazCsvConfig.csv", true, title = "", summary = "", config = configPath)
                        } else {
                            println("Fajl nije pronađen!")
                        }
                    }
                }
                3 ->  {
                    println("Usao u pdf export services u loadData.")
                    if(!flagForConfig){
                        exporterServices["PDF"]?.generateReport(data, "izlazPdfNormal.pdf", true)
                    }
                    else{
                        val inputStream = object {}.javaClass.getResourceAsStream("/config.txt")
                        if (inputStream != null) {

                            val configPath = "D:\\Marko workspace\\Fakultet\\Projekti\\softverskekomponente_tim_markostojicic_vidanstojic\\softverskekomponente_prvi_projekat\\testApp\\src\\main\\resources\\config.txt"
                            // val configPath = "C:/Users/vidan_gofx79m/Desktop/softverske komponente/softverskekomponente_tim_markostojicic_vidanstojic/softverskekomponente_prvi_projekat/testApp/src/main/resources/config.txt"
                            exporterServices["PDF"]?.generateReport(data, "izlazPdfConfig.pdf", true, title = "", summary = "", config = configPath)
                        } else {
                            println("Fajl nije pronađen!")
                        }
                    }
                }
                4 ->  {
                    println("Usao u excel export services u loadData.")
                    if(!flagForConfig){
                        exporterServices["XLS"]?.generateReport(data, "izlazExcelNormal.xls", true)
                    }
                    else{
                        val inputStream = object {}.javaClass.getResourceAsStream("/config.txt")
                        if (inputStream != null) {

                            val configPath = "D:\\Marko workspace\\Fakultet\\Projekti\\softverskekomponente_tim_markostojicic_vidanstojic\\softverskekomponente_prvi_projekat\\testApp\\src\\main\\resources\\config.txt"
                            // val configPath = "C:/Users/vidan_gofx79m/Desktop/softverske komponente/softverskekomponente_tim_markostojicic_vidanstojic/softverskekomponente_prvi_projekat/testApp/src/main/resources/config.txt"
                            exporterServices["XLS"]?.generateReport(data, "izlazExcelConfig.xlsx", true, title = "", summary = "", config = configPath)
                        } else {
                            println("Fajl nije pronađen!")
                        }
                    }
                }
            }
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
        println("3. None")
        println("4. Exit.")
        println("----------------------------------------------")
        print("Choose your option: ")
        var response = scanner.nextInt()
        println("----------------------------------------------")
        when (response) {
            1 -> loadData(false)
            2 -> loadData(true)
            3 -> println(3)
            4 -> break
            else -> println("Unknown command.")
        }
        break
    }

}
//  ./testApp/src/main/resources/izvorPodataka.json