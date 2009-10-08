package pdfdsl.support


import com.lowagie.text.pdf.{PdfReader, PdfStamper}
import java.io.ByteArrayOutputStream

class StamperWrapper(bytesIn: Array[Byte]) {
  private val reader = new PdfReader(bytesIn)
  private val out = new ByteArrayOutputStream
  private val stamper = new PdfStamper(reader, out);

  def stamp(map: Map[String, Any]): Unit = stamp(new MapWrapper(map))

  def stamp(values: MapWrapper) {
    val over = stamper.getOverContent(values.page)
    over.beginText();
    over.setFontAndSize(values.baseFont, values.fontSize)
    val (x, y) = values.at
    val adjustedX = x.value(reader.getPageSizeWithRotation(values.page), values)
    val adjustedY = y.value(reader.getPageSizeWithRotation(values.page), values)
    over.setTextMatrix(adjustedX, adjustedY)
    over.showText(values.text)
    over.endText();
  }

  def bytes = {
    stamper.close
    out.toByteArray
  }
}
