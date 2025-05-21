package by.sentencija.entity;

import by.sentencija.entity.element.CourseElement;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Section implements PDFConvertable {
    private String name;
    private List<? extends CourseElement> elements;

    @Override
    public Object covertToPDF() { //TODO
        return this;
    }
}
