package pdfdsl


import java.io.FileOutputStream

object DslTester extends Application with PdfDsl {
  TestPdfFactory.createPdf("target/HelloWorldRead.pdf")

  val bytes = stamp(file("target/HelloWorldRead.pdf")) {

    write text "hello world 5" at (25, 700)
    write text "hello world 6" at (26, 680) page 2
    write text "hello world 7" at (27, 660) page 1
    write text "bottom-right" at (right - 125, bottom) page 1
    write text "top-right" at (right - 110, top - fontSize) page 1
    write text "top-left" at (left, top - fontSize) page 1
    write text "top-center" at ((right - left) / 2, top - fontSize) page 1
    write text "almost-top-center" at (center, top - fontSize - fontSize) page 1

    section page 1 at (100, 300) contains {
      line text "pinky jones"
      line text "suite abc"
      line text "123 main st"
      line text "des moines, ia 50023"
    }

  }

  val out = new FileOutputStream("target/HelloWorldStamper1.pdf")
  out write bytes
  out.close

}
