package pdfdsl.support


import com.lowagie.text.Rectangle

object Locations {
  trait Location {
    def value(rect: Rectangle, mapWrapper: MapWrapper): Float

    def -(other: Location): Location = new ResultLocation(List(("+", this), ("-", other)))

    def +(other: Location): Location = new ResultLocation(List(("+", this), ("+", other)))

    def /(other: Location): Location = new ResultLocation(List(("+", this), ("/", other)))

    def *(other: Location): Location = new ResultLocation(List(("+", this), ("*", other)))
  }

  class ResultLocation(operations: List[Tuple2[String, Location]]) extends Location {
    def value(rect: Rectangle, mapWrapper: MapWrapper) = {
      operations.foldLeft(0f) {
        (v, tuple) =>
                tuple._1 match {
                  case "-" => v - tuple._2.value(rect, mapWrapper)
                  case "+" => v + tuple._2.value(rect, mapWrapper)
                  case "/" => v / tuple._2.value(rect, mapWrapper)
                  case "*" => v * tuple._2.value(rect, mapWrapper)
                }
      }
    }
  }

  class BaseLocation(val value: Float) extends Location {
    def value(rect: Rectangle, mapWrapper: MapWrapper) = value
  }

  object top extends Location {
    def value(rect: Rectangle, mapWrapper: MapWrapper) = rect.getTop
  }

  object bottom extends Location {
    def value(rect: Rectangle, mapWrapper: MapWrapper) = rect.getBottom
  }

  object left extends Location {
    def value(rect: Rectangle, mapWrapper: MapWrapper) = rect.getLeft
  }

  object right extends Location {
    def value(rect: Rectangle, mapWrapper: MapWrapper) = rect.getRight
  }

  object center extends Location {
    def value(rect: Rectangle, mapWrapper: MapWrapper) = (rect.getRight - rect.getLeft) / 2
  }

  object middle extends Location {
    def value(rect: Rectangle, mapWrapper: MapWrapper) = (rect.getTop - rect.getBottom) / 2
  }

  object fontSize extends Location {
    def value(rect: Rectangle, mapWrapper: MapWrapper) = mapWrapper.fontSize
  }
}