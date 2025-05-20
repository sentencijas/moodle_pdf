package by.sentencija.pdfmaker;

import by.sentencija.entity.MoodleCourse;
import by.sentencija.entity.Section;
import by.sentencija.entity.element.CourseElement;
import by.sentencija.entity.element.Quiz;
import by.sentencija.entity.question.MultipleChoiceQuestion;
import by.sentencija.entity.question.Question;
import by.sentencija.entity.question.TextQuestion;
import by.sentencija.entity.question.TrueFalseQuestion;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.*;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import lombok.val;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class MoodlePdfGenerator {
    //мапа правильных ответов
    private final Map<String, String> correctAnswers = new LinkedHashMap<>();

    public void generatePdfFromCourse(MoodleCourse courseData, String fontPath, String outputPath) throws IOException {
        // Создание PDF-документа
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(outputPath));
        Document document = new Document(pdfDoc);

        // становка шрифта
        PdfFont font = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H);
        document.setFont(font);

        //форма для добавления интерактивных полей
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        // Начальная вертикальная позиция для размещения штук
        float yPosition = 800;
        int questionCounter = 1;

        // проходим по всему
        for (Section section : courseData.sections()) {
            for (CourseElement element : section.elements) {
                if (element instanceof Quiz quiz) {
                    document.add(new Paragraph("Раздел: Викторина").setBold());
                    for (Question q : quiz.getQuestions()) {
                        String fieldName = "q" + questionCounter;
                        if (q instanceof MultipleChoiceQuestion mcq) {
                            yPosition = addRadioQuestion(pdfDoc, document, form,
                                    questionCounter + ". " + mcq.getText(),
                                    mcq.getAnswersValues().keySet().toArray(new String[0]),
                                    fieldName, yPosition);

                            // Сохраняем правильный ответ
                            String correctOption = mcq.getAnswersValues().entrySet().stream()
                                    .filter(e -> e.getValue() > 0)
                                    .map(Map.Entry::getKey)
                                    .findFirst()
                                    .orElse(null);
                            correctAnswers.put(fieldName, correctOption);

                        } else if (q instanceof TextQuestion tq) {
                            yPosition = addTextField(pdfDoc, document, form,
                                    questionCounter + ". " + tq.getText(),
                                    fieldName, yPosition, tq.getCorrectAnswer());
                            correctAnswers.put(fieldName, tq.getCorrectAnswer());

                        } else if (q instanceof TrueFalseQuestion tfq) {
                            yPosition = addYesNoQuestion(pdfDoc, document, form,
                                    questionCounter + ". " + tfq.getText(),
                                    fieldName, yPosition);
                            correctAnswers.put(fieldName, tfq.getCorrectAnswer() ? "Yes" : "No");
                        }
                        questionCounter++;
                    }
                }
            }
        }

        // Кнопка "Проверить тест"
        addCheckButton(pdfDoc, form, yPosition - 50, questionCounter - 1);

        document.close();
        System.out.println("PDF создан: " + outputPath);
    }

    // штука для добавления вопроса с выбором одного ответа
    private float addRadioQuestion(PdfDocument pdf, Document doc, PdfAcroForm form,
                                   String question, String[] options,
                                   String fieldName, float yPos) {
        doc.add(new Paragraph(question));
        PdfButtonFormField radioGroup = PdfFormField.createRadioGroup(pdf, fieldName, "");

        for (int i = 0; i < options.length; i++) {
            Rectangle rect = new Rectangle(50, yPos - (i + 1) * 25, 20, 20);
            PdfFormField radioButton = PdfFormField.createRadioButton(pdf, rect, radioGroup, options[i]);
            doc.add(new Paragraph(options[i]).setFixedPosition(80, yPos - (i + 1) * 25, 200));
        }

        form.addField(radioGroup);
        return yPos - (options.length + 1) * 30;
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

    // для вопросов ДаНет
    private float addYesNoQuestion(PdfDocument pdf, Document doc, PdfAcroForm form,
                                   String question, String fieldName, float yPos) {
        doc.add(new Paragraph(question));

        PdfButtonFormField radioGroup = PdfFormField.createRadioGroup(pdf, fieldName, "");

        Rectangle yesRect = new Rectangle(50, yPos - 25, 20, 20);
        Rectangle noRect = new Rectangle(150, yPos - 25, 20, 20);

        PdfFormField yesButton = PdfFormField.createRadioButton(pdf, yesRect, radioGroup, "Yes");
        PdfFormField noButton = PdfFormField.createRadioButton(pdf, noRect, radioGroup, "No");

        doc.add(new Paragraph("Да").setFixedPosition(80, yPos - 25, 50));
        doc.add(new Paragraph("Нет").setFixedPosition(180, yPos - 25, 50));

        form.addField(radioGroup);
        return yPos - 60;
    }

    //добавление кнопки проверки теста
    private void addCheckButton(PdfDocument pdf, PdfAcroForm form, float yPos, int questionCount) {
        PdfButtonFormField checkButton = PdfFormField.createPushButton(
                pdf, new Rectangle(50, yPos, 200, 25), "checkButton", "Проверить тест");
        checkButton.setAction(PdfAction.createJavaScript(generateCheckScript(questionCount)));
        form.addField(checkButton);
    }

    // JavaScript для проверки ответов
    private String generateCheckScript(int questionCount) {
        StringBuilder js = new StringBuilder();
        js.append("var score = 0; var results = '';\n");

        for (int i = 1; i <= questionCount; i++) {
            String fieldName = "q" + i;
            String correct = correctAnswers.get(fieldName);
            js.append("var q = this.getField('" + fieldName + "');\n");
            js.append("if (q && q.value == '" + correct + "') {\n");
            js.append("  score += 100 / " + questionCount + ";\n");
            js.append("  results += 'Вопрос " + i + ": correct\\n';\n");
            js.append("} else {\n");
            js.append("  results += 'Question " + i + ": error\\n';\n");
            js.append("}\n");
        }

        js.append("app.alert(results + '\\nTotal: ' + score.toFixed(2) + '%', 3);");
        return js.toString();
    }
}
