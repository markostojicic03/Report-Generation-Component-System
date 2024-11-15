package text

import specification.ReportInterface
import java.io.File


class TextReport: ReportInterface {
    override val implementationName: String = "TXT"
    override val formattingFlag: Boolean = false
    override var titleProperty: String = ""
    override var summaryProperty: String = ""
    override var formattingList: Map<String, List<String>> = mutableMapOf()



    override fun generateReport(
        data: Map<String, List<String>>,
        destination: String,
        header: Boolean,
        title: String?,
        summary: String?
    ):Map<String, List<String>> {
        val columns = data.keys.toList()
        val numRows = data.values.first().size
        this.titleProperty = title!!
        this.summaryProperty = summary!!

        val columnWidths = columns.map { column ->
            val maxDataWidth = data[column]?.maxOfOrNull { it.length } ?: 0
            maxOf(column.length, maxDataWidth)
        }


        File(destination).printWriter().use { writer ->

            title?.let {
                writer.println(it)
                writer.println()
            }


            columns.forEachIndexed { index, column ->
                writer.print(column.padEnd(columnWidths[index] + 2))  // +2 for spacing
            }
            writer.println()


            columnWidths.forEach { width ->
                writer.print("-".repeat(width + 2))  // +2 for spacing
            }
            writer.println()

            for (i in 0 until numRows) {
                columns.forEachIndexed { index, column ->
                    val cell = data[column]?.get(i) ?: ""
                    writer.print(cell.padEnd(columnWidths[index] + 2))  // +2 for spacing
                }
                writer.println()
            }

            summary?.let {
                writer.println()
                writer.println(it)
            }
        }
        return data
    }

    override fun generateReportWithFormatting(
        data: Map<String, List<String>>,
        destination: String,
        header: Boolean,
        title: String?,
        summary: String?,
        formattingList: Map<String, List<String>>?
    ):Map<String, List<String>> {
        this.titleProperty = title!!
        this.summaryProperty = summary!!
        val dataReport = generateReport(data, destination, header,title,summary)
        if (!formattingFlag && !formattingList.isNullOrEmpty()) {
            throw UnsupportedOperationException("Formatting is not valid for this type of format.")
        }
        return dataReport
    }

}