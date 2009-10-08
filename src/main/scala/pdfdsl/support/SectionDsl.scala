package pdfdsl.support


import support.Locations.{BaseLocation, Location}

class SectionDsl extends InternalDsl {
  var internals: List[InternalDsl] = List()

  def at(coordinates: Tuple2[Location, Location]) = {
    lingo += ("AT" -> coordinates)
    this
  }

  def page(number: Int) = {
    lingo += ("PAGE" -> number)
    this
  }

  def contains(f: => Unit) {
    f
  }

  def <<(line: LineDsl) {
    internals = internals ::: List(line)
  }

  override def stampWith(stamper: StamperWrapper, defaults: Map[String, Any]): Unit = {
    var coordinates: Map[String, Any] = Map.empty
    for (internal <- internals) {
      val newDefaults = defaults ++ lingo ++ coordinates
      internal.stampWith(stamper, newDefaults)
      val mapWrapper = new MapWrapper(newDefaults)
      val (x: Location, y: Location) = mapWrapper.at
      val adjustedY = y - new BaseLocation(mapWrapper.fontSize)
      coordinates = Map("AT" -> (x, adjustedY))
    }
  }

  override def toString: String = "SectionDsl" + lingo.toString + "\n" + internals.mkString("\n")
}
