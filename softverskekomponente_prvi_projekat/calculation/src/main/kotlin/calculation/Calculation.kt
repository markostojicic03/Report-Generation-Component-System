package calculation

interface Calculation {
    fun sumCalculate(data: Map<String, List<String>>, configColumns : List<Int>, calculationColumns :List<Int>):Map<String, List<String>>

    fun avgCalculate(data: Map<String, List<String>>, configColumns : List<Int>, calculationColumns :List<Int>):Map<String, List<String>>

    fun countCalculate(data: Map<String, List<String>>, configColumns : List<Int>, calculationColumns :List<Int>, operand: String?, numberForOperation: Int?): Map<String, List<String>>

    fun subCalculate(data: Map<String, List<String>>, configColumns : List<Int>, calculationColumns :List<Int>):Map<String, List<String>>

    fun mulCalculate(data: Map<String, List<String>>, configColumns : List<Int>, calculationColumns :List<Int>):Map<String, List<String>>

    fun divCalculate(data: Map<String, List<String>>, configColumns : List<Int>, calculationColumns :List<Int>):Map<String, List<String>>
}