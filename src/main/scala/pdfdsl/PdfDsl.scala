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
            val attributes = defaults ++ section.lingo ++ coordinates ++ sectionInternal.lingo
            stamperWrapper.stamp(attributes)
            val fontSize = attributes("FONT_SIZE") match { case size : Int => size }
            attributes("AT") match { case (x:Number, y:Number) =>
              coordinates = Map("AT"->(x, y.floatValue - fontSize.floatValue))
            }
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

  class StamperWrapper(bytesIn: Array[Byte]) {
    private val reader = new PdfReader(bytesIn)
    private val out = new ByteArrayOutputStream
    private val stamper = new PdfStamper(reader, out);

    def stamp(attributes: Map[String, Any]) {
      val bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
      val page = attributes("PAGE") match { case page: Int => page }
      val fontSize = attributes("FONT_SIZE") match { case size: Int => size }
      val over = stamper.getOverContent(page)
      over.beginText();
      over.setFontAndSize(bf, fontSize.floatValue)
      attributes("AT") match {
        case (x: Number, y: Number) => over.setTextMatrix(x.floatValue, y.floatValue)
      }
      over.showText(attributes("TEXT").toString)
      over.endText();
    }

    def bytes: Array[byte] = {
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