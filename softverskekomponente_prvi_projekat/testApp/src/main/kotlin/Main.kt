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
/**
 *
 * 1. Napisati println za korisnika.
 * 2. Dodati fajlove gde korisnik oznacava sta zeli od kalkulacija/formatiranja.
 * 3. Napraviti funkciju koja parsira taj fajl od korisnika i kupi te podatke i zatim zove funkcije koje je potrebno uraditi za kalk/formatiranje.
 * 4. Dodati u specifikaciji funkcije za kalk/formatiranje.
 * 5. Napraviti dokumentaciju u specifikaciji(implementirati java docs).
 * */
fun main() {
    val serviceLoader = ServiceLoader.load(ReportInterface::class.java)

    val exporterServices = mutableMapOf<String, ReportInterface> ()

    serviceLoader.forEach{
            service ->
        exporterServices[service.implementationName] = service
    }

    println(exporterServices.keys)

    val inputStream = object {}.javaClass.getResourceAsStream("/izvorPodataka.json")
    val reader = InputStreamReader(inputStream)
    val data = prepareData(reader)
    reader.close()
    var scanner = Scanner(System.`in`)
    while(true){
        println("----------------------------------------------")
        println("1. Load data for your report.")
        println("2. Generate your report.")
        println("3. Generate your report with title/summary.")
        println("4. Generate your report with calculations.")
        println("5. Exit.")
        println("----------------------------------------------")
        print("Choose your option: ")
        var response = scanner.nextInt()
        when (response) {
            1 -> println(1)
            2 -> println(2)
            3 -> println(3)
            4 -> println(4)
            5 -> break
            else -> println("Unknown command.")
        }
        break
    }

}