package pdfdsl

object DslTester extends Application with PdfDsl {

  stamp("filename") {
    write text "hello world 5" at (25, 35) page 5
    write text "hello world 6" at (26, 36) page 6
    write text "hello world 7" at (27, 37) page 7

    section page 5 at (100, 500) contains {
      line text "pinky jones"
      line text "123 main st"
      line text "des moines, ia 50023"
    }
  }

  stamp("filename") {
    write text "hello world 8" at (28, 38) page 8
    write text "hello world 9" at (29, 39) page 9
  }

}
