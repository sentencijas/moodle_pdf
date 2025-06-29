package by.sentencija.entity.question;

import lombok.Getter;

@Getter
public class TrueFalseQuestion extends Question {
    private final Boolean correctAnswer;

    public TrueFalseQuestion(String text, boolean correctAnswer) {
        super(text);
        this.correctAnswer = correctAnswer;
    }
}
