package pdfdsl.support


import com.lowagie.text.pdf.{PdfContentByte, PdfWriter, PdfReader, PdfStamper}
import com.lowagie.text.{Rectangle, Document}
import java.io.ByteArrayOutputStream

class PdfCreator extends DslWriter {
  private val out = new ByteArrayOutputStream
  private val document = new Document();
  private val writer = PdfWriter.getInstance(document, out);

  document.open();

  protected def getDirectContent(page: Int) = writer.getDirectContent
  protected def getPageSize(page: Int) = document.getPageSize

  def bytes = {
    document.close
    out.toByteArray
  }

  def insertPage {
    writer.setPageEmpty(false)
    document.newPage
  }
}