implicit def elvisOperator[T](alt: =>T) = new {
  def ?:[A >: T](pred: A) = if (pred == null) alt else pred
}

val result:String = null ?: "hello" ?: "there"

println(result)

