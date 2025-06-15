package by.sentencija.entity.element;

import lombok.Getter;

@Getter
public class Page extends CourseElement{
    private final String title;
    private final String intro;
    private final String content;

    public Page(Long contextId, String title, String intro, String content) {
        super(contextId);
        this.title = title;
        this.intro = intro;
        this.content = content;
    }
}

