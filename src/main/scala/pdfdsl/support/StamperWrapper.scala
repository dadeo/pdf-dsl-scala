package pdfdsl.support


import com.lowagie.text.pdf.{PdfReader, PdfStamper}
import java.io.ByteArrayOutputStream

class StamperWrapper(bytesIn: Array[Byte]) extends DslWriter {
  private val reader = new PdfReader(bytesIn)
  private val out = new ByteArrayOutputStream
  private val stamper = new PdfStamper(reader, out);

  protected def getDirectContent(page: Int) = stamper.getOverContent(page)
  protected def getPageSize(page: Int) = reader.getPageSizeWithRotation(page)

  def bytes = {
    stamper.close
    out.toByteArray
  }

}
