package pdfdsl.support



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

