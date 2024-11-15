package calculation

/**
 * The Calculation interface provides a blueprint for implementing methods that perform calculations
 * based on specified columns within a data set. Implementations of this interface are expected to
 * process the provided data and return a map that includes the original data along with the new calculated values.
 * */
interface Calculation {
    /**
     * This function performs a sum calculation on specified columns within the provided data.
     * The result is returned as a map containing the original data with an additional column
     * representing the calculated sum.
     *
     * @param data A map where the key is the column name and the value is a list of strings representing the data.
     * @param configColumns A list of column indices that the user has specified to be displayed in the report.
     * @param calculationColumns A list of column indices for which the sum will be calculated.
     * @return A map with an added column that contains the calculated sum for the specified columns.
     */
    fun sumCalculate(data: Map<String, List<String>>, configColumns : List<Int>, calculationColumns :List<Int>):Map<String, List<String>>
    /**
     * This function performs an average calculation on specified columns within the provided data.
     * The result is returned as a map containing the original data with an additional column
     * representing the calculated average.
     *
     * @param data A map where the key is the column name and the value is a list of strings representing the data.
     * @param configColumns A list of column indices that the user has specified to be displayed in the report.
     * @param calculationColumns A list of column indices for which the average will be calculated.
     *
     * @return A map with an added column that contains the calculated average for the specified columns.
     * */
    fun avgCalculate(data: Map<String, List<String>>, configColumns : List<Int>, calculationColumns :List<Int>):Map<String, List<String>>


    /**
     *  Counts the number of values in the specified columns based on a given condition.
     *  @param data A map where the key is the column name and the value is a list of strings representing the column data.
     *  @param configColumns A list of column indices specified by the user that they want to include in the output.
     *  @param calculationColumns A list of column indices on which the count operation will be performed.
     *  @param operand A string representing the conditional operator (e.g., "<", ">", "<=", ">=", "=").
     *  @param numberForOperation An integer representing the number used in the conditional operation.
     *  @return A map with the original data and an additional column that contains the count of values meeting the condition.
     *  *
     * */
    fun countCalculate(data: Map<String, List<String>>, configColumns : List<Int>, calculationColumns :List<Int>, operand: String?, numberForOperation: Int?): Map<String, List<String>>

    /**
     * This function performs a subtraction calculation on specified columns within the provided data.
     * The result is returned as a map containing the original data with an additional column
     * representing the calculated subtraction result.
     *
     *
     * @param data A map where the key is the column name and the value is a list of strings representing the data.
     * @param configColumns A list of column indices that the user has specified to be displayed in the report.
     * @param calculationColumns A list of column indices for which the subtraction will be calculated.
     * @throws IllegalArgumentException if more than two columns are provided for subtraction.
     * @return A map with an added column that contains the calculated subtraction result for the specified columns.
     * */
    fun subCalculate(data: Map<String, List<String>>, configColumns : List<Int>, calculationColumns :List<Int>):Map<String, List<String>>
    /**
     * This function performs a multiplication calculation on specified columns within the provided data.
     * The result is returned as a map containing the original data with an additional column
     * representing the calculated product.
     *
     * @param data A map where the key is the column name and the value is a list of strings representing the data.
     * @param configColumns A list of column indices that the user has specified to be displayed in the report.
     * @param calculationColumns A list of column indices for which the product will be calculated.
     * @return A map with an added column that contains the calculated product for the specified columns.
     *
     * */
    fun mulCalculate(data: Map<String, List<String>>, configColumns : List<Int>, calculationColumns :List<Int>):Map<String, List<String>>
    /**
     * This function performs a division calculation on specified columns within the provided data.
     * The result is returned as a map containing the original data with an additional column
     * representing the calculated division result.
     *
     * @param data A map where the key is the column name and the value is a list of strings representing the data.
     * @param configColumns A list of column indices that the user has specified to be displayed in the report.
     * @param calculationColumns A list of column indices for which the division will be calculated.
     * @throws IllegalArgumentException if more than two columns are provided for division.
     * @return A map with an added column that contains the calculated division result for the specified columns.
     * */
    fun divCalculate(data: Map<String, List<String>>, configColumns : List<Int>, calculationColumns :List<Int>):Map<String, List<String>>
}