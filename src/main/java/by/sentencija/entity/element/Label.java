package by.sentencija.entity.element;

import lombok.Getter;

@Getter
public class Label extends CourseElement{
    private final String intro;

    public Label(Long contextId, String intro) {
        super(contextId);
        this.intro = intro;
    }
}
