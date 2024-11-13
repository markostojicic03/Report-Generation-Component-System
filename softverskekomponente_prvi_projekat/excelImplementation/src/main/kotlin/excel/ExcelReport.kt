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
    override var formattingNameProperty: String? = null
    override var formattingTextProperty: String? = null
    override var dataTable: MutableMap<String, List<String>> = mutableMapOf()

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
        formattingName: String?,
        formattingText: String?
    ) {
        if (!formattingFlag) {
            throw IllegalArgumentException("Formatting is not valid for this type of format.")
        }
        val workbook: Workbook = XSSFWorkbook()
        val sheet: Sheet = workbook.createSheet("Report")

        // Definisanje osnovnog stila za formatiranje
        val customStyle = workbook.createCellStyle().apply {
            val customFont: Font = workbook.createFont()

            // Primena stilova u zavisnosti od formattingName
            when (formattingName?.lowercase()) {
                "bold" -> customFont.bold = true
                "italic" -> customFont.italic = true
                "underline" -> customFont.underline = Font.U_SINGLE
                "color_red" -> customFont.color = IndexedColors.RED.index
                "color_blue" -> customFont.color = IndexedColors.BLUE.index
                // Dodajte više boja po potrebi
            }
            this.setFont(customFont)
        }

        // Dodavanje naslova sa potencijalnim formatiranjem
        if (formattingText.equals("title", ignoreCase = true)) {
            title?.let {
                val titleRow: Row = sheet.createRow(0)
                val titleCell: Cell = titleRow.createCell(0)
                titleCell.setCellValue(it)

                // Merge title cells
                sheet.addMergedRegion(CellRangeAddress(0, 0, 0, data.size - 1))

                // Primeni customStyle za naslov
                titleCell.cellStyle = customStyle
            }
        } else {
            // Ako `title` nije označen za formatiranje, dodajemo ga kao običan tekst
            title?.let {
                val titleRow: Row = sheet.createRow(0)
                val titleCell: Cell = titleRow.createCell(0)
                titleCell.setCellValue(it)

                // Merge title cells
                sheet.addMergedRegion(CellRangeAddress(0, 0, 0, data.size - 1))

                // Kreiramo i postavljamo standardni stil za naslov
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
        }

        // Kreiranje reda zaglavlja ako je potrebno
        if (header) {
            val headerRow: Row = sheet.createRow(1)
            data.keys.forEachIndexed { index, columnName ->
                val headerCell = headerRow.createCell(index)
                headerCell.setCellValue(columnName)
                headerCell.cellStyle = if (formattingText == index.toString()) customStyle else workbook.createCellStyle() // Primeni customStyle ako je kolona označena za formatiranje
            }
        }

        // Dodavanje redova podataka
        val numRows = data.values.first().size
        for (i in 0 until numRows) {
            val dataRow: Row = sheet.createRow(if (header) i + 2 else i + 1) // Adjust for header
            data.keys.forEachIndexed { index, columnName ->
                val cellData = data[columnName]?.get(i) ?: ""
                val dataCell = dataRow.createCell(index)
                dataCell.setCellValue(cellData)
                // Primeni customStyle ako je kolona označena za formatiranje
                if (formattingText == index.toString()) {
                    dataCell.cellStyle = customStyle
                }
            }
        }

        // Dodavanje rezimea sa potencijalnim formatiranjem
        if (formattingText.equals("summary", ignoreCase = true)) {
            summary?.let {
                val summaryRow: Row = sheet.createRow(numRows + 2)
                val summaryCell: Cell = summaryRow.createCell(0)
                summaryCell.setCellValue("Summary: $it")
                summaryCell.cellStyle = customStyle // Primeni customStyle za rezime
            }
        } else {
            // Ako `summary` nije označen za formatiranje, dodajemo ga kao običan tekst
            summary?.let {
                val summaryRow: Row = sheet.createRow(numRows + 2)
                val summaryCell: Cell = summaryRow.createCell(0)
                summaryCell.setCellValue("Summary: $it")
            }
        }

        // Upisivanje u odredišni fajl
        FileOutputStream(destination).use { outputStream ->
            workbook.write(outputStream)
        }

        // Zatvaranje workbook-a
        workbook.close()
    }


}