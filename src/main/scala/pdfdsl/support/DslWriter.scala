package pdfdsl.support


import com.lowagie.text.pdf.PdfContentByte
import com.lowagie.text.Rectangle

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

  def bytes : Array[Byte]
  
  protected def getDirectContent(page: Int) : PdfContentByte
  protected def getPageSize(page: Int) : Rectangle
}