package pdfdsl.support


import com.lowagie.text.pdf.BaseFont

class MapWrapper(val mapIn:Map[String, Any]) {
  val text = if(mapIn.contains("TEXT")) mapIn("TEXT").toString else ""
  val at = { val (x : Number, y : Number) = mapIn("AT"); (x.floatValue, y.floatValue) }
  val fontSize = mapIn("FONT_SIZE") match { case size: Int => size.floatValue }
  val page = mapIn("PAGE") match { case page: Int => page }
  val baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
}

