package model

internal class Column(name : String, rows : MutableList<Row>) {
    internal var name: String = name
    internal val rows: MutableList<Row> = rows.toMutableList()



}