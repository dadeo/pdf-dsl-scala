package pdfdsl.support


import com.lowagie.text.pdf.{ColumnText, PdfContentByte}
import com.lowagie.text.{Element, Phrase, PageSize, Rectangle}

trait DslWriter {
  def stamp(map: Map[String, Any]): Unit = stamp(new MapWrapper(map))

  def stamp(values: MapWrapper) {
    val over = getDirectContent(values.page)
    over.beginText();
    over.setFontAndSize(values.baseFont, values.fontSize)
    val (x, y) = values.at
    val pageSize: Rectangle = getPageSize(values.page)
    val adjustedX = x.value(pageSize, values) - values.justificationOffset
    val adjustedY = y.value(pageSize, values)
    over.setTextMatrix(adjustedX, adjustedY)
    over.showText(values.text)
    over.endText();
  }

  def column(values: MapWrapper)(f: (ColumnText) => Unit) {
    val columnText: ColumnText = new ColumnText(getDirectContent(values.page))

    val (x, y) = values.at
    val pageSize: Rectangle = getPageSize(values.page)
    val adjustedX = x.value(pageSize, values) - values.justificationOffset
    val adjustedY = y.value(pageSize, values)
    columnText.setSimpleColumn(adjustedX, adjustedY - values.height, adjustedX + values.width, adjustedY)

    f(columnText)

    columnText.go()
  }

  def bytes: Array[Byte]

  protected def getDirectContent(page: Int): PdfContentByte

  protected def getPageSize(page: Int): Rectangle
}