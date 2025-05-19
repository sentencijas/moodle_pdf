package by.sentencija.entity.element;

import by.sentencija.entity.PDFConvertable;

public class Quiz extends CourseElement implements PDFConvertable  { //the fuck

    @Override
    public Object covertToPDF() {
        return this;
    }
}
