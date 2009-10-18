package pdfdsl.support


import support.Locations.Location

class TableHeaderDsl extends InternalDsl {
  var values : List[String] = List()

  def data(values : List[String]) = {
    this.values = values
    this
  }

  def justified(location: Location) = {
    lingo += ("JUSTIFICATION" -> location)
    this
  }

  def apply() = {
//    f
    println("apply this")
  }

}