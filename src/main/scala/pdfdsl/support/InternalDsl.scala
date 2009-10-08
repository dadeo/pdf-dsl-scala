package pdfdsl.support



trait InternalDsl {
  var lingo: Map[String, Any] = Map.empty
  def stampWith(stamper:StamperWrapper, values:Map[String, Any]) : Unit = stamper.stamp(values++lingo)
}

 