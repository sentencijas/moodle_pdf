package by.sentencija.entity.element;

import by.sentencija.entity.PDFConvertable;
import by.sentencija.entity.question.Question;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class Quiz extends CourseElement implements PDFConvertable  {
    private final String introText;
    private final List<Question> questions;

    @Override
    public Object covertToPDF() {
        return this; //the fuck
    }
}
