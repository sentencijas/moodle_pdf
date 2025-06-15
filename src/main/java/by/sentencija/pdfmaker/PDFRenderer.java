package by.sentencija.pdfmaker;

import by.sentencija.entity.element.CourseElement;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;

public interface PDFRenderer<T extends CourseElement> {
    default void renderElement(PdfDocument pdf, Document doc, PdfAcroForm form, CourseElement courseElement){
        render(pdf, doc, form, (T) courseElement);
    }
    void render(PdfDocument pdf, Document doc, PdfAcroForm form, T element);
}
