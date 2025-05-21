package by.sentencija.entity.parser;

import by.sentencija.entity.XMLParser;
import by.sentencija.entity.element.Quiz;
import by.sentencija.entity.question.Question;
import lombok.AllArgsConstructor;
import lombok.val;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
public class QuizParser extends XMLParser<Quiz> {
    private final Set<String> files;
    private final Map<Integer, Question> questions;
    @Override
    protected Quiz parse(Document document) {
        val quiz = (Element) document.getElementsByTagName("quiz").item(0);
        val questions = quiz.getElementsByTagName("questionbankentryid");
        val quizQuestions = new ArrayList<Question>();
        for(int i = 0;i<questions.getLength();i++){
            val id = Integer.parseInt(questions.item(i).getTextContent());
            quizQuestions.add(this.questions.get(id));
        }
        return new Quiz(
                FileParser.replaceFileReference(
                        getValue(quiz, "intro"),
                        files
                ),
                getValue(quiz, "name"),
                quizQuestions
        );
    }
}
