package by.sentencija.entity.question;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
public class MultipleChoiceQuestion extends Question {
    private final Map<String, Double> answersValues;
}

