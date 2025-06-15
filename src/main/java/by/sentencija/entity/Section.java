package by.sentencija.entity;

import by.sentencija.entity.element.CourseElement;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
public class Section{
    private final String name;
    private final List<? extends CourseElement> elements;

    public Section(String name, List<? extends CourseElement> elements){
        this.name = "$@NULL@$".equals(name) ? "Секция без названия" : name;
        this.elements = elements;
    }
}
