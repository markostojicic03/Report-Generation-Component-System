package excel

import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import specification.ReportInterface
import java.awt.Color
import java.io.File
import java.io.FileOutputStream

class ExcelReport: ReportInterface{
    override val implementationName: String = "XLS"
    override val formattingFlag: Boolean = true
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
        val workbook: Workbook = XSSFWorkbook()
        val sheet: Sheet = workbook.createSheet("Report")
        this.titleProperty = title!!
        this.summaryProperty = summary!!

        title?.let {
            val titleRow: Row = sheet.createRow(0)
            val titleCell: Cell = titleRow.createCell(0)
            titleCell.setCellValue(it)


            sheet.addMergedRegion(CellRangeAddress(0, 0, 0, data.size - 1))


            val titleStyle = workbook.createCellStyle().apply {
                alignment = HorizontalAlignment.CENTER
                val titleFont: Font = workbook.createFont().apply {
                    bold = true
                    fontHeightInPoints = 18
                }
                this.setFont(titleFont)
            }
            titleCell.cellStyle = titleStyle
        }

        if (header) {
            val headerRow: Row = sheet.createRow(1)
            data.keys.forEachIndexed { index, columnName ->
                headerRow.createCell(index).setCellValue(columnName)
            }
        }


        val numRows = data.values.first().size
        for (i in 0 until numRows) {
            val dataRow: Row = sheet.createRow(if (header) i + 2 else i + 1)
            data.keys.forEachIndexed { index, columnName ->
                dataRow.createCell(index).setCellValue(data[columnName]?.get(i) ?: "")
            }
        }


        summary?.let {
            val summaryRow: Row = sheet.createRow(numRows + 2)
            val summaryCell: Cell = summaryRow.createCell(0)
            summaryCell.setCellValue("Summary: $it")
        }


        FileOutputStream(destination).use { outputStream ->
            workbook.write(outputStream)
        }

        workbook.close()
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
        this.formattingList = formattingList!!
        val workbook: Workbook = XSSFWorkbook()
        val sheet: Sheet = workbook.createSheet("Report")

        val boldFont = workbook.createFont().apply { bold = true }
        val italicFont = workbook.createFont().apply { italic = true }
        val redFont = workbook.createFont().apply { color = IndexedColors.RED.index }
        val blueFont = workbook.createFont().apply { color = IndexedColors.BLUE.index }

        fun createCombinedStyle(formattingKeys: List<String>): CellStyle {
            val cellStyle = workbook.createCellStyle()
            val combinedFont = workbook.createFont()

            formattingKeys.forEach { format ->
                when (format.lowercase()) {
                    "bold" -> combinedFont.bold = true
                    "italic" -> combinedFont.italic = true
                    "underline" -> combinedFont.underline = Font.U_SINGLE
                    "color_red" -> combinedFont.color = IndexedColors.RED.index
                    "color_blue" -> combinedFont.color = IndexedColors.BLUE.index
                }
            }
            cellStyle.setFont(combinedFont)
            return cellStyle
        }


        title?.let {
            val titleRow: Row = sheet.createRow(0)
            val titleCell: Cell = titleRow.createCell(0)
            titleCell.setCellValue(it)
            sheet.addMergedRegion(CellRangeAddress(0, 0, 0, data.size - 1))


            val titleFormats = formattingList?.filterValues { "title" in it }?.keys?.toList()
            titleFormats?.let {
                titleCell.cellStyle = createCombinedStyle(titleFormats)
            }
        }

        var currentRow = if (header) 1 else 0
        if (header) {
            val headerRow: Row = sheet.createRow(currentRow++)
            data.keys.forEachIndexed { index, columnName ->
                val headerCell = headerRow.createCell(index)
                headerCell.setCellValue(columnName)

                val columnFormats = formattingList?.filterValues { index.toString() in it }?.keys?.toList()
                columnFormats?.let {
                    headerCell.cellStyle = createCombinedStyle(columnFormats)
                }
            }
        }

        val numRows = data.values.first().size
        for (i in 0 until numRows) {
            val dataRow: Row = sheet.createRow(currentRow++)
            data.keys.forEachIndexed { index, columnName ->
                val dataCell = dataRow.createCell(index)
                dataCell.setCellValue(data[columnName]?.get(i) ?: "")

                val columnFormats = formattingList?.filterValues { index.toString() in it }?.keys?.toList()
                columnFormats?.let {
                    dataCell.cellStyle = createCombinedStyle(columnFormats)
                }
            }
        }

        summary?.let {
            val summaryRow: Row = sheet.createRow(currentRow + 1)
            val summaryCell: Cell = summaryRow.createCell(0)
            summaryCell.setCellValue("Summary: $it")
            sheet.addMergedRegion(CellRangeAddress(currentRow + 1, currentRow + 1, 0, data.size - 1))

            val summaryFormats = formattingList?.filterValues { "summary" in it }?.keys?.toList()
            summaryFormats?.let {
                summaryCell.cellStyle = createCombinedStyle(summaryFormats)
            }
        }


        FileOutputStream(destination).use { outputStream ->
            workbook.write(outputStream)
        }

        workbook.close()
        return data
    }


}