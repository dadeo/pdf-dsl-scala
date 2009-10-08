package pdfdsl.support


import com.lowagie.text.Rectangle

object Locations {
  trait Location {
    def value: float = 0

    protected var operations: List[Tuple2[String, Location]] = Nil

    def value(rect: Rectangle, mapWrapper: MapWrapper): float = {
      operations.foldLeft(value) {
        (v: float, tuple: Tuple2[String, Location]) =>
                tuple._1 match {
                  case "-" => v - tuple._2.value(rect, mapWrapper)
                  case "+" => v + tuple._2.value(rect, mapWrapper)
                }
      }
    }

    def -(other: Location): Location = {
      val result = new BaseLocation(0f)
      result.operations += ("+", this)
      result.operations += ("-", other)
      result
    }

    def +(other: Location): Location = {
      val result = new BaseLocation(0f)
      result.operations += ("+", this)
      result.operations += ("+", other)
      result
    }
  }

  class BaseLocation(override val value: float) extends Location

  object top extends Location {
    override def value(rect: Rectangle, mapWrapper: MapWrapper): float = {
      super.value(rect, mapWrapper) + rect.getTop
    }
  }

  object bottom extends Location {
    override def value(rect: Rectangle, mapWrapper: MapWrapper): float = {
      super.value(rect, mapWrapper) + rect.getBottom
    }
  }

  object left extends Location {
    override def value(rect: Rectangle, mapWrapper: MapWrapper): float = {
      super.value(rect, mapWrapper) + rect.getLeft
    }
  }

  object right extends Location {
    override def value(rect: Rectangle, mapWrapper: MapWrapper): float = {
      super.value(rect, mapWrapper) + rect.getRight
    }
  }

  object fontSize extends Location {
    override def value(rect: Rectangle, mapWrapper: MapWrapper): float = {
      super.value(rect, mapWrapper) + mapWrapper.fontSize
    }
  }
}