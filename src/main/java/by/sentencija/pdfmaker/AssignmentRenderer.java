package by.sentencija.pdfmaker;

import by.sentencija.entity.element.Assignment;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.LineSeparator;

public class AssignmentRenderer implements PDFRenderer<Assignment> {
    private final static MoodlePdfGenerator generator = new MoodlePdfGenerator();
    @Override
    public void render(PdfDocument pdf, Document doc, PdfAcroForm form, Assignment element) {
        generator.addHtmlString(doc, pdf, element.getName());
        generator.addHtmlString(doc, pdf, element.getIntro());
        doc.add(new LineSeparator(new SolidLine(1)));
    }
}
