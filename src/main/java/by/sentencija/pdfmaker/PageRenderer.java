package by.sentencija.pdfmaker;

import by.sentencija.entity.element.Page;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;

public class PageRenderer implements PDFRenderer<Page> {
    private final static MoodlePdfGenerator generator = new MoodlePdfGenerator();
    @Override
    public void render(PdfDocument pdf, Document doc, PdfAcroForm form, Page page) {
        doc.add(new Paragraph("Страница: " + page.getTitle()).setBold());
        generator.addHtmlString(doc, pdf, page.getContent());
        doc.add(new LineSeparator(new SolidLine(1)));
    }
}
