package pdf

import com.lowagie.text.*
import com.lowagie.text.pdf.PdfPCell
import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.pdf.PdfWriter
import specification.ReportInterface
import java.awt.Color
import java.io.FileOutputStream

class PdfReport: ReportInterface {
    override val implementationName: String = "PDF"
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
    ) {
        // Create a new document
        val document = Document()

        try {
            // Initialize PdfWriter
            PdfWriter.getInstance(document, FileOutputStream(destination))

            // Open the document for writing
            document.open()

            // Add title if provided
            title?.let {
                val titleParagraph = Paragraph(it, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18f))
                titleParagraph.alignment = Element.ALIGN_CENTER
                document.add(titleParagraph)
                document.add(Chunk.NEWLINE)  // Add a new line after the title
            }

            // Create a table based on the number of columns in the data
            val columns = data.keys.toList()
            val numColumns = columns.size
            val table = PdfPTable(numColumns)

            // Add header row if necessary
            if (header) {
                columns.forEach { column ->
                    val cell = PdfPCell(Paragraph(column, FontFactory.getFont(FontFactory.HELVETICA_BOLD)))
                    cell.horizontalAlignment = Element.ALIGN_CENTER
                    table.addCell(cell)
                }
            }

            // Add data rows
            val numRows = data.values.first().size
            for (i in 0 until numRows) {
                columns.forEach { column ->
                    val cellData = data[column]?.get(i) ?: ""
                    table.addCell(cellData)
                }
            }

            // Add the table to the document
            document.add(table)

            // Add summary if provided
            summary?.let {
                document.add(Chunk.NEWLINE)
                val summaryParagraph = Paragraph("Summary: $summary", FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE))
                document.add(summaryParagraph)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            // Close the document
            document.close()
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
        val document = Document()

        try {
            PdfWriter.getInstance(document, FileOutputStream(destination))
            document.open()

            fun createCombinedFont(styles: List<String>): Font {
                val font = FontFactory.getFont(FontFactory.HELVETICA, 12f)
                styles.forEach { style ->
                    when (style.lowercase()) {
                        "bold" -> font.style = font.style or Font.BOLD
                        "italic" -> font.style = font.style or Font.ITALIC
                        "color_red" -> font.color = Color.RED
                        "color_blue" -> font.color = Color.BLUE
                    }
                }
                return font
            }

            title?.let {
                val titleFormats = formattingList?.filterValues { "title" in it }?.keys?.toList() ?: listOf()
                val titleFont = if (titleFormats.isNotEmpty()) createCombinedFont(titleFormats) else FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18f)
                val titleParagraph = Paragraph(it, titleFont).apply { alignment = Element.ALIGN_CENTER }
                document.add(titleParagraph)
                document.add(Chunk.NEWLINE)
            }


            val columns = data.keys.toList()
            val numColumns = columns.size
            val table = PdfPTable(numColumns)

            if (header) {
                columns.forEachIndexed { index, column ->
                    val headerFormats = formattingList?.filterValues { index.toString() in it }?.keys?.toList() ?: listOf()
                    val headerFont = if (headerFormats.isNotEmpty()) createCombinedFont(headerFormats) else FontFactory.getFont(FontFactory.HELVETICA_BOLD)
                    val cell = PdfPCell(Paragraph(column, headerFont)).apply {
                        horizontalAlignment = Element.ALIGN_CENTER
                        backgroundColor = Color.LIGHT_GRAY
                    }
                    table.addCell(cell)
                }
            }

            val numRows = data.values.first().size
            for (i in 0 until numRows) {
                columns.forEachIndexed { index, column ->
                    val dataFormats = formattingList?.filterValues { index.toString() in it }?.keys?.toList() ?: listOf()
                    val dataFont = if (dataFormats.isNotEmpty()) createCombinedFont(dataFormats) else FontFactory.getFont(FontFactory.HELVETICA, 12f)
                    val cell = PdfPCell(Paragraph(data[column]?.get(i) ?: "", dataFont)).apply {
                        horizontalAlignment = Element.ALIGN_CENTER
                    }
                    table.addCell(cell)
                }
            }
            document.add(table)

            summary?.let {
                document.add(Chunk.NEWLINE)
                val summaryFormats = formattingList?.filterValues { "summary" in it }?.keys?.toList() ?: listOf()
                val summaryFont = if (summaryFormats.isNotEmpty()) createCombinedFont(summaryFormats) else FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE)
                val summaryParagraph = Paragraph("Summary: $it", summaryFont)
                document.add(summaryParagraph)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            document.close()
        }
    }





}