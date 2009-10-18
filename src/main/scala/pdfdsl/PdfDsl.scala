package pdfdsl

import java.io.File
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

  def create() : Array[Byte] = {
    val dslWriter = new PdfCreator()
    var lastPage = 1
    sort(internals).foreach { instruction =>
      val page = new MapWrapper(defaults ++ instruction.lingo).page
      while(lastPage < page) {
        dslWriter.insertPage
        lastPage += 1
      }
      instruction.stampWith(dslWriter, defaults)
    }
    dslWriter.bytes
  }

  def stamp(contents: Array[Byte]) : Array[Byte] = {
    val dslWriter = new StamperWrapper(contents)
    internals.foreach { instruction =>
      val page = new MapWrapper(defaults ++ instruction.lingo).page
      while(dslWriter.pageCount < page) {
        dslWriter.insertPage
      }
      instruction.stampWith(dslWriter, defaults)
    }
    dslWriter.bytes
  }

  def sort(list:List[InternalDsl]) : List[InternalDsl] = {
    list.sort((first, next) => new MapWrapper(defaults ++ first.lingo).page <= new MapWrapper(defaults ++ next.lingo).page)
  }
}