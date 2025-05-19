package by.sentencija.entity;

import by.sentencija.entity.element.CourseElement;

import java.util.List;

public class Section implements PDFConvertable {
    List<CourseElement> elements;

    public Section(List<CourseElement> elements) {
        this.elements = elements;
    }

    @Override
    public Object covertToPDF() { //TODO
        return this;
    }
}
