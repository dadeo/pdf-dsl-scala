package pdfdsl

import com.lowagie.text.pdf.{PdfReader, PdfStamper, BaseFont}
import java.io.{File, ByteArrayOutputStream, FileInputStream}
import support._
import support.Locations.{BaseLocation, Location}

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

  implicit def convert(number: Int): Location = {
    new BaseLocation(number.floatValue)
  }

  implicit def convert(number: Float): Location = {
    new BaseLocation(number)
  }

  implicit def convert(s: String): File = new File(s)

  class Write
  val write = new Write

  class Line
  val line = new Line

  class Section
  val section = new Section

  val top = Locations.top
  val bottom = Locations.bottom
  val left = Locations.left
  val right = Locations.right
  val center = Locations.center
  val middle = Locations.middle
  val fontSize = Locations.fontSize

  var internals: List[InternalDsl] = List()
  var currentSection: SectionDsl = null
  val defaults = Map("FONT_SIZE" -> 18, "PAGE" -> 1, "JUSTIFICATION" -> Locations.left)

  def stamp(contents: Array[Byte]) : Array[Byte] = {
    val stamperWrapper = new StamperWrapper(contents)
    internals.foreach {_.stampWith(stamperWrapper, defaults)}
    stamperWrapper.bytes
  }

}