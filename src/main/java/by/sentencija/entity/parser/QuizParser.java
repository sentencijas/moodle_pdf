package by.sentencija.entity.parser;

import by.sentencija.entity.XMLParser;
import by.sentencija.entity.element.Quiz;
import org.w3c.dom.Document;

public class QuizParser extends XMLParser<Quiz> {
    @Override
    protected Quiz parse(Document document) {
        return new Quiz();
    }
}
