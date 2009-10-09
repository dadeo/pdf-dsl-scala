package pdfdsl.support

import com.lowagie.text.Rectangle
import org.scalatest.junit.JUnit3Suite
import junit.framework.Assert._
import support.Locations.BaseLocation

class LocationsTest extends JUnit3Suite with PdfDsl {
  var rect : Rectangle = null
  var mapWrapper : MapWrapper = null

  override def setUp() {
    rect = new Rectangle(75, 50, 400, 700)
    mapWrapper = new MapWrapper(Map(
      "AT"->(new BaseLocation(5), new BaseLocation(10)),
      "PAGE"->2,
      "FONT_SIZE"->18,
      "JUSTIFICATION"->Locations.center))
  }

  def testRectangleDimensions() {
    assertEquals(700.0f, rect.getTop)
    assertEquals(50.0f, rect.getBottom)
    assertEquals(75.0f, rect.getLeft)
    assertEquals(400.0f, rect.getRight)
  }

  def test_BaseLocation_Int() {
    assertEquals(42f, 42.value(rect, mapWrapper))
  }

  def test_BaseLocation_Float() {
    assertEquals(42f, 42f.value(rect, mapWrapper))
  }

  def test_top() {
    assertEquals(700f, top.value(rect, mapWrapper))
  }

  def test_bottom() {
    assertEquals(50f, bottom.value(rect, mapWrapper))
  }

  def test_left() {
    assertEquals(75f, left.value(rect, mapWrapper))
  }

  def test_right() {
    assertEquals(400f, right.value(rect, mapWrapper))
  }

  def test_top_minus_base() {
    def x = top - new BaseLocation(42f)
    assertEquals(658f, x.value(rect, mapWrapper))
  }

  def test_bottom_plus_base() {
    def x = bottom + 42
    assertEquals(92f, x.value(rect, mapWrapper))
  }

  def test_int_before_location() {
    def x = 42 + bottom
    assertEquals(92f, x.value(rect, mapWrapper))
  }

  def test_top_minus_fontSize() {
    def x = top - fontSize
    assertEquals(682f, x.value(rect, mapWrapper))
  }

  def test_multiple_values() {
    assertEquals(782f, (top - fontSize + 200 - 100).value(rect, mapWrapper))
    assertEquals(618f, (top - 200 + fontSize + 100).value(rect, mapWrapper))
    assertEquals(382f, (top - (200 + fontSize + 100)).value(rect, mapWrapper))
  }
}