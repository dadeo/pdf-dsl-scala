package pdfdsl


import com.lowagie.text.pdf.{PdfReader, PdfStamper, BaseFont}
import java.io.{File, ByteArrayOutputStream, FileInputStream}

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
    val internal = new LineDsl
    currentSection.internals = currentSection.internals ::: List(internal)
    internal
  }

  implicit def convert(s: String): File = new File(s)

  private implicit def convert(map:Map[String, Any]) = new MapWrapper(map)

  class Write
  val write = new Write

  class Line
  val line = new Line

  class Section
  val section = new Section

  var internals: List[InternalDsl] = List()
  var currentSection: SectionDsl = null
  val defaults = Map("FONT_SIZE"->18, "PAGE"->1)

  def stamp(contents: Array[byte])(f: => Unit) = {
    internals = List()
    f
    val stamperWrapper = new StamperWrapper(contents)
    for (internal <- internals) internal match {
      case _: WriteDsl =>
        stamperWrapper.stamp(defaults++internal.lingo)
      case section: SectionDsl =>
        var coordinates : Map[String, Any] = Map.empty
        for (sectionInternal <- section.internals) sectionInternal match {
          case _: LineDsl =>
            val mapWrapper = new MapWrapper(defaults ++ section.lingo ++ coordinates ++ sectionInternal.lingo)
            stamperWrapper.stamp(mapWrapper)
            val(x, y) = mapWrapper.at
            coordinates = Map("AT"->(x, y.floatValue - mapWrapper.fontSize))
        }
    }
    stamperWrapper.bytes
  }

  trait InternalDsl {
    var lingo: Map[String, Any] = Map.empty
  }

  class WriteDsl extends InternalDsl {
    def text(value: String) = {
      lingo += ("TEXT" -> value)
      this
    }

    def at(coordinates: Tuple2[Number, Number]) = {
      lingo += ("AT" -> coordinates)
      this
    }

    def page(number: Int) = {
      lingo += ("PAGE" -> number)
      this
    }

    override def toString: String = "WriteDsl" + lingo.toString
  }

  class LineDsl extends InternalDsl {
    def text(value: String) = {
      lingo += ("TEXT" -> value)
      this
    }

    override def toString: String = "LineDsl" + lingo.toString
  }

  class SectionDsl extends InternalDsl {
    var internals: List[InternalDsl] = List()

    def at(coordinates: Tuple2[Number, Number]) = {
      lingo += ("AT" -> coordinates)
      this
    }

    def page(number: Int) = {
      lingo += ("PAGE" -> number)
      this
    }

    def contains(f: => Unit) {
      f
      currentSection = null
    }

    override def toString: String = "SectionDsl" + lingo.toString + "\n" + internals.mkString("\n")
  }

  class MapWrapper(mapIn:Map[String, Any]) {
    val text = mapIn("TEXT").toString
    val at = mapIn("AT") match { case (x: Number, y: Number) => (x.floatValue, y.floatValue) }
    val fontSize = mapIn("FONT_SIZE") match { case size: Int => size.floatValue }
    val page = mapIn("PAGE") match { case page: Int => page }
    val baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
  }

  class StamperWrapper(bytesIn: Array[Byte]) {
    private val reader = new PdfReader(bytesIn)
    private val out = new ByteArrayOutputStream
    private val stamper = new PdfStamper(reader, out);

    def stamp(values: MapWrapper) {
      val over = stamper.getOverContent(values.page)
      over.beginText();
      over.setFontAndSize(values.baseFont, values.fontSize)
      val(x, y) = values.at
      over.setTextMatrix(x, y)
      over.showText(values.text)
      over.endText();
    }

    def bytes = {
      stamper.close
      out.toByteArray
    }
  }

  def file(f: File) = {
    val bos = new ByteArrayOutputStream
    val ba = new Array[Byte](2048)
    val is = new FileInputStream(f)
    def read {
      is.read(ba) match {
        case n if n < 0 =>
        case 0 => read
        case n => bos.write(ba, 0, n); read
      }
    }
    read
    bos.toByteArray
  }

}