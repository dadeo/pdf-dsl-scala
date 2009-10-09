package pdfdsl


import com.lowagie.text.pdf.PdfWriter
import com.lowagie.text.{Section, Document, Paragraph, Chapter}
import java.io.FileOutputStream

object TestPdfFactory {
  def createPdf(filename: String) {
    var document = new Document();
    PdfWriter.getInstance(document, new FileOutputStream(filename));
    document.open();
    def hello = new Paragraph("(English:) hello, " +
            "(Esperanto:) he, alo, saluton, (Latin:) heu, ave, " +
            "(French:) all\u00f4, (Italian:) ciao, (German:) hallo, he, heda, holla, " +
            "(Portuguese:) al\u00f4, ol\u00e1, hei, psiu, bom d\u00eda, (Dutch:) hallo, dag, " +
            "(Spanish:) ola, eh, (Catalan:) au, bah, eh, ep, " +
            "(Swedish:) hej, hejsan(Danish:) hallo, dav, davs, goddag, hej, " +
            "(Norwegian:) hei; morn, (Papiamento:) halo; hallo; k\u00ed tal, " +
            "(Faeroese:) hall\u00f3, hoyr, (Turkish:) alo, merhaba, (Albanian:) tungjatjeta");
    def universe = new Chapter("To the Universe:", 1);
    var section: Section = null
    section = universe.addSection("to the World:");
    section.add(hello);
    section = universe.addSection("to the Sun:");
    section.add(hello);
    section = universe.addSection("to the Moon:");
    section.add(hello);
    section = universe.addSection("to the Stars:");
    section.add(hello);
    document.add(universe);
    var people = new Chapter("To the People:", 2);
    section = people.addSection("to mothers and fathers:");
    section.add(hello);
    section = people.addSection("to brothers and sisters:");
    section.add(hello);
    section = people.addSection("to wives and husbands:");
    section.add(hello);
    section = people.addSection("to sons and daughters:");
    section.add(hello);
    section = people.addSection("to complete strangers:");
    section.add(hello);
    document.add(people);
    var animals = new Chapter("To the Animals:", 3);
    section = animals.addSection("to cats and dogs:");
    section.add(hello);
    section = animals.addSection("to birds and bees:");
    section.add(hello);
    section = animals.addSection("to farm animals and wild animals:");
    section.add(hello);
    section = animals.addSection("to bugs and beatles:");
    section.add(hello);
    section = animals.addSection("to fish and shellfish:");
    section.add(hello);
    document.add(animals);
    document.close();
  }

}