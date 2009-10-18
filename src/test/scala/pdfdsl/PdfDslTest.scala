package pdfdsl


import org.scalatest.junit.JUnit3Suite
import support.Locations.BaseLocation
import support.{WriteDsl, InternalDsl}
import junit.framework.Assert._

class PdfDslTest extends JUnit3Suite {
  var unsortedList: List[InternalDsl] = Nil

  override def setUp {
    unsortedList = Nil
  }

  def testSort() {
    unsortedList += createInternalDslForPage(3)
    unsortedList += createInternalDslForPage(2)
    unsortedList += createInternalDslForPage(4)
    unsortedList += createInternalDslForPage(1)
    unsortedList += createInternalDslForPage(3)
    unsortedList += createInternalDslForPage(1)
    assertEquals(List(1, 1, 2, 3, 3, 4), extractPages(new PdfDsl {}.sort(unsortedList)))
  }

  private def extractPages(instructions: List[InternalDsl]) = {
    instructions.map {_.page}
  }

  private def createInternalDslForPage(page: Int) = {
    val dsl = new WriteDsl
    dsl.at(Tuple2(new BaseLocation(3), new BaseLocation(5)))
    dsl.page(page)
    dsl
  }
}