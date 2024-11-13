package calculation

class Calculation(data: Map<String, List<String>>, configColumns : MutableList<Int>, calculationColumns :MutableList<Int>) {
    private var data = data
    private var configColumns = configColumns
    private var calculationColumns = calculationColumns

    fun sumCalculate():Map<String, List<String>>{

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
            for (colIndex in calculationColumns) {
                val columnName = data.keys.elementAt(colIndex)
                val value = data[columnName]?.get(i)?.toString()?.toDoubleOrNull()?.toInt() ?: 0
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

    fun avgCalculate():Map<String, List<String>>{

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
            for (colIndex in calculationColumns) {
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

    fun countCalculate(operand: String?, numberForOperation: Int?): Map<String, List<String>> {

        val result = mutableMapOf<String, MutableList<String>>()
        result["countColumn"] = mutableListOf()

        val columnName = data.keys.elementAt(calculationColumns.get(0))
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


    fun subCalculate():Map<String, List<String>>{

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
            while (colIndex < calculationColumns.size - 1) {
                var columnName = data.keys.elementAt(calculationColumns.get(colIndex))
                var value = data[columnName]?.get(i)?.toIntOrNull() ?: 0
                sub = value
                columnName = data.keys.elementAt(calculationColumns.get(++colIndex))
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

    fun mulCalculate():Map<String, List<String>>{

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
            for (colIndex in calculationColumns) {
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

    fun divCalculate():Map<String, List<String>>{

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
            while (colIndex < calculationColumns.size - 1) {
                var columnName = data.keys.elementAt(calculationColumns.get(colIndex))
                var value = data[columnName]?.get(i)?.toIntOrNull() ?: 0
                div = value.toDouble()
                columnName = data.keys.elementAt(calculationColumns.get(++colIndex))
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