package by.sentencija.entity.question;

import lombok.Getter;

import java.util.Map;

@Getter
public class MultipleChoiceQuestion extends Question {
    private final Map<String, Double> answersValues;

    public MultipleChoiceQuestion(String text, Map<String, Double> answersValues) {
        super(text);
        this.answersValues = answersValues;
    }
}

