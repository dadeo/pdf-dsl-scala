package pdfdsl.support


class LineDsl extends InternalDsl {
  def text(value: String) = {
    lingo += ("TEXT" -> value)
    this
  }

  override def toString: String = "LineDsl" + lingo.toString
}

