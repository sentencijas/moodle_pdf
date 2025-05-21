package by.sentencija.entity.question;

import lombok.Getter;

import java.util.Set;

@Getter
public class MultipleChoiceQuestion extends Question {
    private final boolean singleAnswer;
    private final Set<String> answersValues;

    public MultipleChoiceQuestion(String text, Set<String> answersValues, boolean singleAnswer) {
        super(text);
        this.answersValues = answersValues;
        this.singleAnswer = singleAnswer;
    }
}

