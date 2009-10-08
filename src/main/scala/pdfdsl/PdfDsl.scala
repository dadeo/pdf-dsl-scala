package pdfdsl

import com.lowagie.text.pdf.{PdfReader, PdfStamper, BaseFont}
import java.io.{File, ByteArrayOutputStream, FileInputStream}
import support._

trait PdfDsl {
  implicit def convert(w: Write) = {
    val internal = new WriteDsl
    internals = internals ::: List(internal)
    internal
  }

  implicit def convert(section: Section) = {
    val internal = new SectionDsl
    internals = internals ::: List(internal)
    currentSection = internal
    internal
  }

  implicit def convert(line: Line) = {
    val lineDsl = new LineDsl
    currentSection << lineDsl
    lineDsl
  }

  implicit def convert(s: String): File = new File(s)

  class Write
  val write = new Write

  class Line
  val line = new Line

  class Section
  val section = new Section

  var internals: List[InternalDsl] = List()
  var currentSection: SectionDsl = null
  val defaults = Map("FONT_SIZE" -> 18, "PAGE" -> 1)

  def stamp(contents: Array[byte])(f: => Unit) = {
    internals = List()

    f

    val stamperWrapper = new StamperWrapper(contents)
    internals.foreach {_.stampWith(stamperWrapper, defaults)}
    stamperWrapper.bytes
  }


  def file(f: File) = {
    FileUtility loadBytes f
  }

}