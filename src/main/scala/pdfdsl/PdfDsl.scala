package pdfdsl


trait PdfDsl {
  implicit def convert(w: Write) = {
    val internal = new InternalDsl
    internals = internals ::: List(internal)
    internal
  }

  class Write
  val write = new Write

  var internals: List[InternalDsl] = List()

  def stamp(filename:String)(f: => Unit) {
    internals = List()
    f
    println(internals)
  }

  class InternalDsl {
    var lingo: Map[String, Any] = Map.empty

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

    override def toString: String = lingo.toString
  }

}