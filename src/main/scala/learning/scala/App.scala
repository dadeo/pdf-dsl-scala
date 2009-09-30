package learning.scala

import _root_.scala.io.Source
import _root_.scala.collection.mutable.Map
import _root_.scala.xml.XML

object App extends Application {
  var clients: List[Map[String, String]] = Nil
  Source.fromFile("src/main/resources/accounts.txt").getLines.zipWithIndex.foreach {
    _ match {
      case (line, i) =>
        val trimmed = line.trim
        (i % 6) match {
          case 0 => clients = Map("name" -> trimmed) :: clients
          case 1 => clients.head += ("id" -> trimmed)
          case 2 => clients.head += ("address" -> trimmed)
          case 3 => clients.head += ("csz" -> trimmed)
          case 4 => clients.head += ("phone" -> trimmed)  
          case 5 =>
        }
    }
  }

  var xml =
    <accounts>
      {
        clients.reverse.map { client =>
          val name = client("name").split(' ')

          <client id={client("id")}>
            <name>
              <first_name>{name(0)}</first_name>
              <last_name>{name(1)}</last_name>
            </name>
            <address>
              <line1>{client("address")}</line1>
              {
                val csz = """^(.+),\s*?(\w\w)\s*?(\d{5})$""".r
                val csz(city, state, zip) = client("csz")
                <city>{city}</city>
                <state>{state}</state>
                <zip_code>{zip}</zip_code>
              }
            </address>
            <phone>{client("phone")}</phone>
          </client>
        }
      }
    </accounts>

  println(xml)

  XML save ("src/main/resources/accounts.xml", xml)
}
