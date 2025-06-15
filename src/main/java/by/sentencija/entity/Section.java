package by.sentencija.entity;

import by.sentencija.entity.element.CourseElement;
import lombok.Getter;

import java.util.List;

public record Section(String name, List<? extends CourseElement> elements) {
    public Section(String name, List<? extends CourseElement> elements) {
        this.name = "$@NULL@$".equals(name) ? "Секция без названия" : name;
        this.elements = elements;
    }
}
