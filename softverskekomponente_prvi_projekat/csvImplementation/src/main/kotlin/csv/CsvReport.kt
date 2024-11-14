package csv

import specification.ReportInterface
import java.io.File

class CsvReport: ReportInterface {
    override val implementationName: String = "CSV"
    override val formattingFlag: Boolean = false
    override var titleProperty: String = ""
    override var summaryProperty: String = ""
    override var formattingList: Map<String, List<String>> = mutableMapOf()
    override var dataTable: MutableMap<String, List<String>> = mutableMapOf()

    override fun generateReport(
        data: Map<String, List<String>>,
        destination: String,
        header: Boolean,
        title: String?,
        summary: String?
    ) {
        val columns = data.keys.toList()
        val numRows = data.values.first().size


        File(destination).printWriter().use { writer ->
            if(header)
                writer.println(columns.joinToString(","))
            for (i in 0 until numRows) {
                val row = columns.map { column -> data[column]?.get(i) ?: "" }
                writer.println(row.joinToString(","))
            }
        }
    }

    override fun generateReportWithFormatting(
        data: Map<String, List<String>>,
        destination: String,
        header: Boolean,
        title: String?,
        summary: String?,
        formattingList: Map<String, List<String>>?
    ) {
        generateReport(data, destination, header,title,summary)
        if (!formattingFlag) {
            throw UnsupportedOperationException("Formatting is not valid for this type of format.")
        }


    }


}