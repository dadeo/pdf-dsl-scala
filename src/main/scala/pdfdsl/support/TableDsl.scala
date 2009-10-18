package pdfdsl.support


import com.lowagie.text.pdf.{PdfPCell, PdfPTable, ColumnText}
import com.lowagie.text.Phrase
import support.Locations.{BaseLocation, Location}


import support.SectionDsl

class TableDsl extends InternalDsl {
  var headers: List[TableHeaderDsl] = List()
  var row: List[TableRowDsl] = List()

  def at(coordinates: Tuple2[Location, Location]) = {
    lingo += ("AT" -> coordinates)
    this
  }

  def page(number: Int) = {
    lingo += ("PAGE" -> number)
    this
  }

  def justified(location: Location) = {
    lingo += ("JUSTIFICATION" -> location)
    this
  }

  def width(width: Int) = {
    lingo += ("WIDTH" -> width)
    this
  }

  def height(length: Int) = {
    lingo += ("HEIGHT" -> length)
    this
  }

  def contains(f: => Unit) {
    f
  }

  def <<(headers: TableHeaderDsl) {
    this.headers = this.headers ::: List(headers)
  }

  def <<(row: TableRowDsl) {
    this.row = this.row ::: List(row)
  }

  override def stampWith(dslWriter: DslWriter, defaults: Map[String, Any]): Unit = {
    val mapWrapper = new MapWrapper(defaults ++ lingo)
    val table = new PdfPTable(headers(0).values.size)
    dslWriter.column(mapWrapper) { columnText: ColumnText =>
      headers(0).values.foreach { header =>
        val cell = new PdfPCell(new Phrase(header))
        cell.setGrayFill(0.95f)
        cell.setBorderWidthTop(0f)
        cell.setBorderWidthLeft(0f)
        cell.setBorderWidthRight(0f)
        table.addCell(cell)
      }
      row(0).values.foreach { rows =>
        rows.foreach { column =>
          table.addCell(column)
        }
      }
      columnText.addElement(table)
    }
  }

  override def toString: String = "SectionDsl" + lingo.toString + "\n" + headers.mkString("\n")
}