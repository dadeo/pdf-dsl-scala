package pdfdsl.support

import com.lowagie.text.pdf.BaseFont
import support.Locations.Location

class MapWrapper(val mapIn: Map[String, Any]) {
  val text = if (mapIn.contains("TEXT")) mapIn("TEXT").toString else ""
  val at: Tuple2[Location, Location] = mapIn("AT") match {case (x: Location, y: Location) => (x, y)}
  val fontSize = mapIn("FONT_SIZE") match {case size: Int => size.floatValue}
  val page = mapIn("PAGE") match {case page: Int => page}
  val baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
}

