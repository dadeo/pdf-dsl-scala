package pdfdsl


import support.FileUtility._

object DslTester extends Application {
  TestPdfFactory.createPdf("target/HelloWorldRead.pdf")

  object updatePdf extends PdfDsl {
    table at (100, top - 200) page 1 width 500 height 600 contains {
      headers justified center data List("hello\nworld", "column 0", "column 1", "column 2", "column 3")

      rows data List(
        List("c1", "c2", "c3", "c4", "c5"),
        List("c1", "c2", "c3", "c4", "c5"),
        List("c1", "c2", "c3", "c4", "c5")
        )
    }

    write text "centered-middle" justified center at (center, middle) page 5
    write text "hello world 1" at (25, 700)
    write text "hello world 6" at (26, 680) page 2
    write text "hello world 7" at (27, 660) page 1
    write text "bottom-right" justified right at (right, bottom) page 1
    write text "bottom-left" justified left at (left, bottom) page 1
    write text "top-right" justified right at (right, top - fontSize) page 1
    write text "top-left" at (left, top - fontSize) page 1
    write text "top-center" at ((right - left) / 2, top - fontSize) page 1
    write text "almost-top-center" at (center, top - fontSize - fontSize) page 1
    write text "top-center-justified" justified center at (center, top - fontSize * 3) page 1
    write text "top-right-justified" justified right at (center, top - fontSize * 4) page 1
    write text "centered-middle" justified center at (center, middle) page 1

    section page 1 at (left + 50, 400) contains {
      line text "pinky jones"
      line text "suite abc"
      line text "123 main st"
      line text "des moines, ia 50023"
    }

    section page 1 at (center, 300) justified center contains {
      line text "pinky jones"
      line text "suite abc"
      line text "123 main st"
      line text "des moines, ia 50023"
    }

    section page 1 at (right - 50, 200) justified right contains {
      line text "pinky jones"
      line text "suite abc"
      line text "123 main st"
      line text "des moines, ia 50023"
    }
  }

  write("target/HelloWorldCreate.pdf")(updatePdf.create)
  write("target/HelloWorldUpdate.pdf")(updatePdf.stamp(loadBytes("target/HelloWorldRead.pdf")))
}
