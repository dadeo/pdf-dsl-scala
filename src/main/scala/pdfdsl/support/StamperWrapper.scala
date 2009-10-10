package pdfdsl.support


import com.lowagie.text.PageSize
import com.lowagie.text.pdf.{BaseFont, PdfContentByte, PdfReader, PdfStamper}
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

  def pageCount = reader.getNumberOfPages

  def insertPage = stamper.insertPage(reader.getNumberOfPages + 1, PageSize.LETTER)
}
