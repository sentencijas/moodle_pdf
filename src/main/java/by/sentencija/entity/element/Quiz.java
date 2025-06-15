package by.sentencija.entity.element;

import by.sentencija.entity.question.Question;
import lombok.Getter;

import java.util.List;

@Getter
public class Quiz extends CourseElement{
    private final String introText;
    private final String name;
    private final List<Question> questions;

    public Quiz(Long contextId, String introText, String name, List<Question> questions) {
        super(contextId);
        this.introText = introText;
        this.name = name;
        this.questions = questions;
    }
}
