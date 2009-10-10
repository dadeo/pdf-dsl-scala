package pdfdsl.support

import com.lowagie.text.Rectangle
import org.scalatest.junit.JUnit3Suite
import junit.framework.Assert._
import support.Locations.BaseLocation

class LocationsTest extends JUnit3Suite with PdfDsl {
  val FONT_SIZE = 18f
  val TOP = 700f
  val RIGHT = 400f
  val BOTTOM = 50f
  val LEFT = 75f

  var rect : Rectangle = null
  var mapWrapper : MapWrapper = null

  override def setUp() {
    rect = new Rectangle(LEFT, BOTTOM, RIGHT, TOP)
    mapWrapper = new MapWrapper(Map(
      "AT"->(new BaseLocation(5), new BaseLocation(10)),
      "PAGE"->2,
      "FONT_SIZE"->FONT_SIZE,
      "JUSTIFICATION"->Locations.center))
  }

  def testRectangleDimensions() {
    assertEquals(TOP, rect.getTop)
    assertEquals(BOTTOM, rect.getBottom)
    assertEquals(LEFT, rect.getLeft)
    assertEquals(RIGHT, rect.getRight)
  }

  def test_BaseLocation_Int() {
    assertEquals(42f, 42.value(rect, mapWrapper))
  }

  def test_BaseLocation_Float() {
    assertEquals(42f, 42f.value(rect, mapWrapper))
  }

  def test_top() {
    assertEquals(TOP, top.value(rect, mapWrapper))
  }

  def test_bottom() {
    assertEquals(BOTTOM, bottom.value(rect, mapWrapper))
  }

  def test_left() {
    assertEquals(LEFT, left.value(rect, mapWrapper))
  }

  def test_right() {
    assertEquals(RIGHT, right.value(rect, mapWrapper))
  }

  def test_center() {
    assertEquals((RIGHT - LEFT) / 2, center.value(rect, mapWrapper))
  }

  def test_middle() {
    assertEquals((TOP - BOTTOM) / 2, middle.value(rect, mapWrapper))
  }

  def test_top_minus_base() {
    def x = top - new BaseLocation(42f)
    assertEquals(TOP - 42, x.value(rect, mapWrapper))
  }

  def test_bottom_plus_base() {
    def x = bottom + 42
    assertEquals(BOTTOM + 42, x.value(rect, mapWrapper))
  }

  def test_int_before_location() {
    def x = 42 + bottom
    assertEquals(BOTTOM + 42, x.value(rect, mapWrapper))
  }

  def test_top_minus_fontSize() {
    def x = top - fontSize
    assertEquals(TOP - FONT_SIZE, x.value(rect, mapWrapper))
  }

  def test_multiple_values() {
    assertEquals(TOP - FONT_SIZE + 200 - 100, (top - fontSize + 200 - 100).value(rect, mapWrapper))
    assertEquals(TOP - 200 + FONT_SIZE + 100, (top - 200 + fontSize + 100).value(rect, mapWrapper))
    assertEquals(TOP - (200 + FONT_SIZE + 100), (top - (200 + fontSize + 100)).value(rect, mapWrapper))
  }
}