package by.sentencija.entity.element;

import lombok.Getter;

@Getter
public class Assignment extends CourseElement{
    private final String name;
    private final String intro;

    public Assignment(Long contextId, String name, String intro) {
        super(contextId);
        this.name = name;
        this.intro = intro;
    }
}
