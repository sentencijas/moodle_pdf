package by.sentencija.pdfmaker;

import by.sentencija.entity.MoodleCourse;
import by.sentencija.entity.Section;
import by.sentencija.entity.element.Assignment;
import by.sentencija.entity.element.CourseElement;
import by.sentencija.entity.element.Label;
import by.sentencija.entity.element.Page;
import by.sentencija.entity.element.Quiz;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.font.FontProvider;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class MoodlePdfGenerator {
    private final ConverterProperties props = new ConverterProperties();
    private final FontProvider fontProvider = new DefaultFontProvider(false, false, false);
    private final static java.util.List<String> fonts = java.util.List.of("NotoSans-Regular", "times");
    public MoodlePdfGenerator(){
        for(val fontName : fonts){
            loadFont(fontProvider, fontName);
        }
        props.setFontProvider(fontProvider);
        props.setCharset("utf-8");

    }
    private final static Logger logger = LoggerFactory.getLogger(MoodlePdfGenerator.class);

    public void generatePdfFromCourse(MoodleCourse courseData, String fontPath, String outputPath) throws IOException {
        Files.createDirectories(Path.of(outputPath));
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(outputPath));
        Document document = new Document(pdfDoc);
        document.setFont(PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H));
        document.setFontProvider(fontProvider);

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        document.add(new Paragraph("Курс: " + courseData.course().fullName()).setBold());

        for (Section section : courseData.sections()) {
            document.add(new Paragraph("Раздел: " + section.name()).setBold());
            val renderers = createRenderers();
            for (CourseElement element : section.elements()) {
                if(element == null) continue;
                val thisClass = element.getClass();
                if(!renderers.containsKey(thisClass)){
                    logger.warn("Элемент типа {} не имеет обработчик", thisClass);
                    continue;
                }
                renderers.get(thisClass).renderElement(pdfDoc, document, form, element);
            }
        }

        document.close();
        logger.info("PDF создан: {}", outputPath);
    }
    public void addHtmlString(Document doc, PdfDocument pdf, String string){

        float maxWidth = pdf.getDefaultPageSize().getWidth() - doc.getLeftMargin() - doc.getRightMargin();
        val elements = HtmlConverter.convertToElements(string, props);
        for (IElement element : elements) {
            scaleImages(element, maxWidth);

            if (element instanceof IBlockElement) {
                doc.add((IBlockElement) element);
            } else if (element instanceof Image) {
                doc.add((Image) element);
            } else {
                // Wrap unknowns in paragraph
                doc.add((IBlockElement) element);
            }
        }
    }


    private static void scaleImages(Object element, float maxWidth) {
        if (element instanceof Image) {
            Image img = (Image) element;
            if (img.getImageScaledWidth() > maxWidth) {
                img.scaleToFit(maxWidth, Float.MAX_VALUE);
            }
        } else if (element instanceof Paragraph) {
            for (IElement child : ((Paragraph) element).getChildren()) {
                scaleImages(child, maxWidth);
            }
        } else if (element instanceof Div) {
            for (IElement child : ((Div) element).getChildren()) {
                scaleImages(child, maxWidth);
            }
        } else if (element instanceof List) {
            for (val item : ((List) element).getChildren()) {
                scaleImages(item, maxWidth);
            }
        }
        // Add more types if needed (Table, Cell, etc.)
    }

    private void loadFont(FontProvider fontProvider, String name){
        try(InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/" + name + ".ttf")) {
            if (fontStream == null) {
                throw new RuntimeException("Шрифт не найден");
            }
            try {
                fontProvider.addFont(fontStream.readAllBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
        }
    }

    private static Map<Class<?>, PDFRenderer<?>> createRenderers(){
        return Map.of(
                Quiz.class, new QuizRenderer(),
                Page.class, new PageRenderer(),
                Label.class, new LabelRenderer(),
                Assignment.class, new AssignmentRenderer()
        );
    }

}