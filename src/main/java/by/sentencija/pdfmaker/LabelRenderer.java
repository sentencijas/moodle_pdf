package by.sentencija.pdfmaker;

import by.sentencija.entity.element.Label;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;

public class LabelRenderer implements PDFRenderer<Label>{
    private final static MoodlePdfGenerator generator = new MoodlePdfGenerator();
    @Override
    public void render(PdfDocument pdf, Document doc, PdfAcroForm form, Label element) {
        generator.addHtmlString(doc, pdf, element.getIntro());
    }
}
