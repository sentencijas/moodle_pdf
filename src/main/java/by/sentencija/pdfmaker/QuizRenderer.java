package by.sentencija.pdfmaker;
import by.sentencija.entity.element.Quiz;
import by.sentencija.entity.question.MultipleChoiceQuestion;
import by.sentencija.entity.question.Question;
import by.sentencija.entity.question.TextQuestion;
import by.sentencija.entity.question.TrueFalseQuestion;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.ParagraphRenderer;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class QuizRenderer implements PDFRenderer<Quiz> {
    private final static MoodlePdfGenerator generator = new MoodlePdfGenerator();
    private final static Logger logger = LoggerFactory.getLogger(QuizRenderer.class);

    @Override
    public void render(PdfDocument pdf, Document doc, PdfAcroForm form, Quiz courseElement) {
        doc.add(new Paragraph("Тест: " + courseElement.getName()));
        generator.addHtmlString(doc, courseElement.getIntroText());
        addQuestionForm(pdf, doc, form, courseElement.getQuestions(), "quiz_" + courseElement.hashCode());
        doc.add(new LineSeparator(new SolidLine(1)));
    }

    public static void addQuestionForm(PdfDocument pdf, Document doc, PdfAcroForm form, List<Question> questions, String quizPrefix){
        var counter = 1;
        for (Question q : questions) {
            doc.add(new Paragraph("Вопрос " + counter));
            if(q == null){
                continue;
            }
            // Render question HTML
            val clazz = q.getClass();
            val questionPrefix = quizPrefix + "_q_" + counter;
            if(clazz == MultipleChoiceQuestion.class)
                addMultipleChoiceQuestion(pdf, doc, form, (MultipleChoiceQuestion) q, questionPrefix);
            else if(clazz == TrueFalseQuestion.class)
                addTrueFalseQuestion(pdf, doc, form, (TrueFalseQuestion) q, questionPrefix);
            else if(clazz == TextQuestion.class)
                addTextQuestion(pdf, doc, (TextQuestion) q, questionPrefix);
            counter++;
        }
    }

    public static void addMultipleChoiceQuestion(PdfDocument pdf, Document doc, PdfAcroForm form,
                                                 MultipleChoiceQuestion question, String groupName){
        HtmlConverter.convertToElements(question.getText()).forEach(iElement -> doc.add((IBlockElement) iElement));

        // Table for answers: 2 columns (radio + html)
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 9}));
        table.setWidth(UnitValue.createPercentValue(100));

        // Create radio group
        logger.info("Creating radio group with name {}", groupName);
        PdfButtonFormField radioGroup = PdfFormField.createRadioGroup(pdf, groupName, "");

        int answerIndex = 1;

        for (String answerHtml : question.getAnswersValues()) {
            // Cell 1: placeholder to get position
            Cell buttonCell = new Cell().setHeight(20);
            table.addCell(buttonCell);

            // Cell 2: rendered HTML content
            Div htmlDiv = new Div();
            HtmlConverter.convertToElements(answerHtml).forEach(iElement -> htmlDiv.add((IBlockElement) iElement));
            Cell htmlCell = new Cell().add(htmlDiv);
            table.addCell(htmlCell);

            // After layout, we’ll back-patch radio buttons at placeholder positions
            // Keep a list of placeholders
            val name = groupName + "_a_" + answerIndex;
            buttonCell.setNextRenderer(
                    question.isSingleAnswer() ?
                    new RadioCellRenderer(buttonCell, radioGroup, name) :
                            new CheckboxCellRenderer(buttonCell, name, pdf)
            );
            answerIndex++;
        }

        doc.add(table);
        form.addField(radioGroup);
    }

    public static void addTrueFalseQuestion(PdfDocument pdf, Document doc, PdfAcroForm form, TrueFalseQuestion question,
                                            String groupName){
        HtmlConverter.convertToElements(question.getText()).forEach(iElement -> doc.add((IBlockElement) iElement));

        // Table for answers: 2 columns (radio + html)
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 9}));
        table.setWidth(UnitValue.createPercentValue(100));

        // Create radio group
        PdfButtonFormField radioGroup = PdfFormField.createRadioGroup(pdf, groupName, "");

        int answerIndex = 1;

        val answers = List.of("<p>Да</p>", "<p>Нет</p>");
        for (String answerHtml : answers) {
            // Cell 1: placeholder to get position
            Cell buttonCell = new Cell().setHeight(20);
            table.addCell(buttonCell);

            // Cell 2: rendered HTML content
            Div htmlDiv = new Div();
            HtmlConverter.convertToElements(answerHtml).forEach(iElement -> htmlDiv.add((IBlockElement) iElement));
            Cell htmlCell = new Cell().add(htmlDiv);
            table.addCell(htmlCell);

            // After layout, we’ll back-patch radio buttons at placeholder positions
            // Keep a list of placeholders
            buttonCell.setNextRenderer(new RadioCellRenderer(buttonCell, radioGroup, groupName + "_a_"+ answerIndex ));
            answerIndex++;
        }

        doc.add(table);
        form.addField(radioGroup);
    }

    public static void addTextQuestion(
            PdfDocument pdf, Document doc,
            TextQuestion question, String groupName) {

        // Render the HTML question text
        HtmlConverter.convertToElements(question.getText())
                .forEach(iElement -> doc.add((IBlockElement) iElement));

        // Create a placeholder block where we’ll position the text field
        Paragraph spacer = new Paragraph(" "); // dummy space
        spacer.setHeight(25); // height for the text field
        doc.add(spacer);

        // Unique field name for this question
        String fieldName = groupName + "_text";

        // Add custom renderer to place the text field
        spacer.setNextRenderer(new TextFieldRenderer(spacer, fieldName, pdf));
    }



    static class RadioCellRenderer extends CellRenderer {
        PdfButtonFormField radioGroup;
        String exportValue;

        public RadioCellRenderer(Cell modelElement, PdfButtonFormField radioGroup, String exportValue) {
            super(modelElement);
            this.radioGroup = radioGroup;
            this.exportValue = exportValue;
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);
            Rectangle rect = getOccupiedAreaBBox();
            PdfFormField radio = PdfFormField.createRadioButton(drawContext.getDocument(), rect, radioGroup, exportValue);
            radioGroup.addKid(radio);
        }

        @Override
        public IRenderer getNextRenderer() {
            return new RadioCellRenderer((Cell) getModelElement(), radioGroup, exportValue);
        }
    }

    static class CheckboxCellRenderer extends CellRenderer {
        private final String fieldName;
        private final PdfDocument pdf;

        public CheckboxCellRenderer(Cell modelElement, String fieldName, PdfDocument pdf) {
            super(modelElement);
            this.fieldName = fieldName;
            this.pdf = pdf;
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);
            Rectangle rect = getOccupiedAreaBBox();
            PdfFormField checkbox = PdfFormField.createCheckBox(pdf, rect, fieldName, "Yes");
            PdfAcroForm.getAcroForm(pdf, true).addField(checkbox);
        }

        @Override
        public IRenderer getNextRenderer() {
            return new CheckboxCellRenderer((Cell) getModelElement(), fieldName, pdf);
        }
    }


    static class TextFieldRenderer extends ParagraphRenderer {
        private final String fieldName;
        private final PdfDocument pdf;

        public TextFieldRenderer(Paragraph modelElement, String fieldName, PdfDocument pdf) {
            super(modelElement);
            this.fieldName = fieldName;
            this.pdf = pdf;
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);
            Rectangle rect = getOccupiedAreaBBox();

            PdfTextFormField textField = PdfFormField.createText(
                    pdf, rect, fieldName, ""
            );
            PdfAcroForm.getAcroForm(pdf, true).addField(textField);
        }

        @Override
        public IRenderer getNextRenderer() {
            return new TextFieldRenderer((Paragraph) getModelElement(), fieldName, pdf);
        }
    }
}
