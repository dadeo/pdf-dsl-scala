package pdfdsl


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

  class Write
  val write = new Write

  class Line
  val line = new Line

  class Section
  val section = new Section

  var internals: List[InternalDsl] = List()
  var currentSection: SectionDsl = null

  def stamp(filename: String)(f: => Unit) {
    internals = List()
    f
    for(internal <- internals) println(internal)
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

}