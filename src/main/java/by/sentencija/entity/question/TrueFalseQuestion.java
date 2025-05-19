package by.sentencija.entity.question;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TrueFalseQuestion extends Question {
    private final boolean correctAnswer;
}
