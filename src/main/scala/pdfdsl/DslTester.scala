package pdfdsl


import java.io.FileOutputStream

object DslTester extends Application with PdfDsl {
  
  val bytes = stamp(file("target/HelloWorldRead.pdf")) {

    write text "hello world 5" at (25, 700)
    write text "hello world 6" at (26, 680) page 2
    write text "hello world 7" at (27, 660) page 1
    
    section page 1 at (100, 300) contains {
      line text "pinky jones"
      line text "suite 123"
      line text "123 main st"
      line text "des moines, ia 50023"
    }

  }

  val out = new FileOutputStream("target/HelloWorldStamper1.pdf")
  out write bytes
  out.close

}
