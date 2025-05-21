package by.sentencija.pdfmaker;

import by.sentencija.entity.MoodleCourse;
import by.sentencija.entity.Section;
import by.sentencija.entity.element.CourseElement;
import by.sentencija.entity.element.Quiz;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.*;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.Paragraph;

import java.io.IOException;

public class MoodlePdfGenerator {
    public static int counter = 1;

    public void generatePdfFromCourse(MoodleCourse courseData, String fontPath, String outputPath) throws IOException {
        // Создание PDF-документа
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(outputPath));
        Document document = new Document(pdfDoc);

        // становка шрифта
        PdfFont font = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H);
        document.setFont(font);

        //форма для добавления интерактивных полей
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        document.add(new Paragraph("Курс: " + courseData.course().fullName()).setBold());

        var quizCounter = 1;
        // проходим по всему
        for (Section section : courseData.sections()) {
            document.add(new Paragraph("Раздел: " + section.getName()).setBold());
            for (CourseElement element : section.getElements()) {
                if (element instanceof Quiz quiz) {
                    document.add(new Paragraph("Тест: " + quiz.getName()));
                    HtmlConverter.convertToElements(quiz.getIntroText()).forEach(iElement -> document.add((IBlockElement) iElement));
                    QuizAdder.addQuestionForm(pdfDoc, document, form, quiz.getQuestions(), "q_" + quizCounter++);
                }
            }
        }

        document.close();
        System.out.println("PDF создан: " + outputPath);
    }

    // для вопроса с текстовым ответом
    private float addTextField(PdfDocument pdf, Document doc, PdfAcroForm form,
                               String question, String fieldName, float yPos, String hint) {
        doc.add(new Paragraph(question));
        doc.add(new Paragraph("(Подсказка: " + hint + ")").setItalic());

        PdfTextFormField textField = PdfTextFormField.createText(
                pdf, new Rectangle(50, yPos - 30, 300, 20), fieldName, "");
        form.addField(textField);
        return yPos - 60;
    }
}
