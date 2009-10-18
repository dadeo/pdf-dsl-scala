package pdfdsl.support


import support.TableHeaderDsl

class TableRowDsl extends InternalDsl {
  var values : List[List[String]] = List()

  def data(values : List[List[String]]) {
    this.values = values
  }
}