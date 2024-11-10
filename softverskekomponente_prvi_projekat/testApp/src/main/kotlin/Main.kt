package org.example

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import specification.ReportInterface
import java.io.InputStreamReader
import java.util.*

data class Schedule(
    val subject: String,
    val classroom: String,
    val year: Int,
    val group: String,
    val day: String,
    val time_from: String,
    val time_to: String
)

val exporterServices = mutableMapOf<String, ReportInterface> ()
val serviceLoader = ServiceLoader.load(ReportInterface::class.java)


fun prepareData(jsonData: InputStreamReader): Map<String, List<String>> {
    val gson = Gson()
    val scheduleType = object : TypeToken<List<Schedule>>() {}.type
    val schedules: List<Schedule> = gson.fromJson(jsonData, scheduleType)

    // Convert the list into a Map<String, List<String>> where key is column name and value is a list of corresponding column data
    val reportData: Map<String, List<String>> = mapOf(
        "subject" to schedules.map { it.subject },
        "classroom" to schedules.map { it.classroom },
        "year" to schedules.map { it.year.toString() },
        "group" to schedules.map { it.group },
        "day" to schedules.map { it.day },
        "time_from" to schedules.map { it.time_from },
        "time_to" to schedules.map { it.time_to }
    )

    return reportData
}
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
        2 -> print("")
        3 -> {
            print("Write your json path: ")
            var response2 = scanner.next()
            println("-----------------------------------")
            val inputStream = object {}.javaClass.getResourceAsStream(response2)
            val reader = InputStreamReader(inputStream)
            val data = prepareData(reader)
            reader.close()

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

                            val configPath = "D:\\Marko workspace\\Fakultet\\Projekti\\softverskekomponente_tim_markostojicic_vidanstojic\\softverskekomponente_prvi_projekat\\testApp\\src\\main\\resources\\config.txt"
                           // val configPath = "C:/Users/vidan_gofx79m/Desktop/softverske komponente/softverskekomponente_tim_markostojicic_vidanstojic/softverskekomponente_prvi_projekat/testApp/src/main/resources/config.txt"
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
                            exporterServices["XLS"]?.generateReport(data, "izlazExcelConfig.xls", true, title = "", summary = "", config = configPath)
                        } else {
                            println("Fajl nije pronađen!")
                        }
                    }
                }
            }
        }
    }
}

/**
 *
 * 1. Napisati println za korisnika.
 * 2. Dodati fajlove gde korisnik oznacava sta zeli od kalkulacija/formatiranja.
 * 3. Napraviti funkciju koja parsira taj fajl od korisnika i kupi te podatke i zatim zove funkcije koje je potrebno uraditi za kalk/formatiranje.
 * 4. Dodati u specifikaciji funkcije za kalk/formatiranje.
 * 5. Napraviti dokumentaciju u specifikaciji(implementirati java docs).
 * */
fun main() {
   // val serviceLoader = ServiceLoader.load(ReportInterface::class.java)

   // val exporterServices = mutableMapOf<String, ReportInterface> ()

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