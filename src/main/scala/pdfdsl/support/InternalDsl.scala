package pdfdsl.support


trait InternalDsl {
  var lingo: Map[String, Any] = Map.empty

  def page = lingo("PAGE") match { case page: Int => page }

  def stampWith(dslWriter: DslWriter, values: Map[String, Any]): Unit = dslWriter.stamp(values ++ lingo)
}

 