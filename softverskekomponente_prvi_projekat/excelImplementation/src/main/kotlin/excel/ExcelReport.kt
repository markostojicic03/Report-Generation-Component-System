package excel

import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import specification.ReportInterface
import java.awt.Color
import java.io.FileOutputStream

class ExcelReport: ReportInterface{
    override val implementationName: String = "XLS"
    override val formattingFlag: Boolean = true
    override var titleProperty: String = ""
    override var summaryProperty: String = ""
    override var formattingNameProperty: String? = null
    override var formattingTextProperty: String? = null
    override var formattingList: Map<String, List<String>> = mutableMapOf()

    override fun generateReport(
        data: Map<String, List<String>>,
        destination: String,
        header: Boolean,
        title: String?,
        summary: String?
    ) {
        val workbook: Workbook = XSSFWorkbook()
        val sheet: Sheet = workbook.createSheet("Report")

        // Add title if provided
        title?.let {
            val titleRow: Row = sheet.createRow(0)
            val titleCell: Cell = titleRow.createCell(0)
            titleCell.setCellValue(it)

            // Merge title cells
            sheet.addMergedRegion(CellRangeAddress(0, 0, 0, data.size - 1))

            // Create and set title style
            val titleStyle = workbook.createCellStyle().apply {
                alignment = HorizontalAlignment.CENTER
                // Import Font class
                val titleFont: Font = workbook.createFont().apply {
                    bold = true
                    fontHeightInPoints = 18
                }
                this.setFont(titleFont)
            }
            titleCell.cellStyle = titleStyle
        }
        // Create header row if necessary
        if (header) {
            val headerRow: Row = sheet.createRow(1)
            data.keys.forEachIndexed { index, columnName ->
                headerRow.createCell(index).setCellValue(columnName)
            }
        }

        // Add data rows
        val numRows = data.values.first().size
        for (i in 0 until numRows) {
            val dataRow: Row = sheet.createRow(if (header) i + 2 else i + 1) // Adjust for header
            data.keys.forEachIndexed { index, columnName ->
                dataRow.createCell(index).setCellValue(data[columnName]?.get(i) ?: "")
            }
        }

        // Add summary if provided
        summary?.let {
            val summaryRow: Row = sheet.createRow(numRows + 2) // Place summary after data
            val summaryCell: Cell = summaryRow.createCell(0)
            summaryCell.setCellValue("Summary: $it")
        }

        // Write to the destination file
        FileOutputStream(destination).use { outputStream ->
            workbook.write(outputStream)
        }

        // Closing the workbook
        workbook.close()
    }

    override fun generateReportWithFormatting(
        data: Map<String, List<String>>,
        destination: String,
        header: Boolean,
        title: String?,
        summary: String?,
        formattingList: Map<String, List<String>>?
    ) {
        val workbook: Workbook = XSSFWorkbook()
        val sheet: Sheet = workbook.createSheet("Report")

        // Definisanje stilova za formatiranje
        val boldFont = workbook.createFont().apply { bold = true }
        val italicFont = workbook.createFont().apply { italic = true }
        val redFont = workbook.createFont().apply { color = IndexedColors.RED.index }

        val boldStyle = workbook.createCellStyle().apply { setFont(boldFont) }
        val italicStyle = workbook.createCellStyle().apply { setFont(italicFont) }
        val redStyle = workbook.createCellStyle().apply { setFont(redFont) }

        // Dodaj naslov (title) ako postoji i primeni formatiranje
        title?.let {
            val titleRow: Row = sheet.createRow(0)
            val titleCell: Cell = titleRow.createCell(0)
            titleCell.setCellValue(it)

            // Primeni stilove za title na osnovu `formattingList`
            val titleStyle = workbook.createCellStyle()
            formattingList?.get("title")?.forEach { format ->
                when (format) {
                    "Bold" -> titleStyle.setFont(boldFont)
                    "Italic" -> titleStyle.setFont(italicFont)
                    "color_red" -> titleStyle.setFont(redFont)
                }
            }
            titleCell.cellStyle = titleStyle

            // Spoji ćelije za title
            sheet.addMergedRegion(CellRangeAddress(0, 0, 0, data.size - 1))
        }

        // Kreiraj zaglavlje ako je potrebno
        var currentRow = if (header) 1 else 0
        if (header) {
            val headerRow: Row = sheet.createRow(currentRow++)
            data.keys.forEachIndexed { index, columnName ->
                val headerCell = headerRow.createCell(index)
                headerCell.setCellValue(columnName)

                // Primeni formatiranje iz `formattingList` za kolone
                formattingList?.get(columnName)?.forEach { format ->
                    when (format) {
                        "Bold" -> headerCell.cellStyle = boldStyle
                        "Italic" -> headerCell.cellStyle = italicStyle
                        "color_red" -> headerCell.cellStyle = redStyle
                    }
                }
            }
        }

        // Dodavanje redova sa podacima
        val numRows = data.values.first().size
        for (i in 0 until numRows) {
            val dataRow: Row = sheet.createRow(currentRow++)
            data.keys.forEachIndexed { index, columnName ->
                val cell = dataRow.createCell(index)
                cell.setCellValue(data[columnName]?.get(i) ?: "")

                // Primeni formatiranje za podatke kolona
                formattingList?.get(columnName)?.forEach { format ->
                    when (format) {
                        "Bold" -> cell.cellStyle = boldStyle
                        "Italic" -> cell.cellStyle = italicStyle
                        "color_red" -> cell.cellStyle = redStyle
                    }
                }
            }
        }

        // Dodaj summary (zaključak) ako postoji i primeni formatiranje
        summary?.let {
            val summaryRow: Row = sheet.createRow(currentRow + 2)
            val summaryCell: Cell = summaryRow.createCell(0)
            summaryCell.setCellValue("Summary: $it")

            // Stilizuj summary na osnovu `formattingList`
            val summaryStyle = workbook.createCellStyle()
            formattingList?.get("summary")?.forEach { format ->
                when (format) {
                    "Bold" -> summaryStyle.setFont(boldFont)
                    "Italic" -> summaryStyle.setFont(italicFont)
                    "color_red" -> summaryStyle.setFont(redFont)
                }
            }
            summaryCell.cellStyle = summaryStyle

            // Spoji ćelije za summary
            sheet.addMergedRegion(CellRangeAddress(currentRow + 2, currentRow + 2, 0, data.size - 1))
        }

        // Upisivanje u fajl
        FileOutputStream(destination).use { outputStream ->
            workbook.write(outputStream)
        }

        // Zatvori workbook
        workbook.close()
    }


}