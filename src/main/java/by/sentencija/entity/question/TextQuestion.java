package by.sentencija.entity.question;

import lombok.Getter;

@Getter
public class TextQuestion extends Question{
    private final String correctAnswer;
    public TextQuestion(String text, String correctAnswer) {
        super(text);
        this.correctAnswer = correctAnswer;
    }
}
