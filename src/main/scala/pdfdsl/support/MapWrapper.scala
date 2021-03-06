package pdfdsl.support

import com.lowagie.text.pdf.BaseFont
import support.Locations.Location

class MapWrapper(val mapIn: Map[String, Any]) {
  val text = if (mapIn.contains("TEXT")) mapIn("TEXT").toString else ""
  val at: Tuple2[Location, Location] = mapIn("AT") match {case (x: Location, y: Location) => (x, y)}
  val height : Int = mapIn("HEIGHT") match {case value : Int => value}
  val width : Int = mapIn("WIDTH") match {case value : Int => value}
  val fontSize = mapIn("FONT_SIZE") match {case size: Float => size; case size: Int => size.floatValue}
  val page = mapIn("PAGE") match {case page: Int => page}
  val baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED)
  val justificationOffset : float = {
    val justification = mapIn("JUSTIFICATION") match { case location: Location => location }
    def center = baseFont.getWidthPoint(text, fontSize) / 2
    def right = baseFont.getWidthPoint(text, fontSize)
    justification match {
      case Locations.center => center
      case Locations.right => right
      case _ => 0
    }
    //if (justification == Locations.center) center else if (justification == Locations.right) right else 0
  }
}

