package by.sentencija.entity.parser;

import by.sentencija.entity.question.MultipleChoiceQuestion;
import by.sentencija.entity.question.Question;
import by.sentencija.entity.XMLParser;
import by.sentencija.entity.question.TextQuestion;
import by.sentencija.entity.question.TrueFalseQuestion;
import lombok.AllArgsConstructor;
import lombok.val;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.lang.Math.round;

public class QuestionsParser extends XMLParser<Map<Integer,Question>> {
    public QuestionsParser(Set<String> files){
        this.files = files;
        PARSERS = Map.of(
                "truefalse", new TrueFalseQuestionParser(),
                "multichoice", new MultipleChoiceQuestionParser(files),
                "shortanswer", new TextQuestionParser()
        );
    }
    private final Set<String> files;

    private final Map<String, ? extends QuestionParser> PARSERS;

    @Override
    protected Map<Integer,Question> parse(Document document) {
        val result = new HashMap<Integer, Question>();
        val questions = document.getElementsByTagName("question");
        for(int i = 0;i<questions.getLength();i++){
            val thisQuestion = (Element)questions.item(i);
            val answersPosition = thisQuestion.getElementsByTagName("answers").item(0);
            val parserName = answersPosition.getParentNode().getNodeName().split("_")[2];
            if(PARSERS.containsKey(parserName)){
               result.put(
                       Integer.parseInt(thisQuestion.getAttribute("id")),
                       PARSERS.get(parserName).parse(
                               FileParser.replaceFileReference(
                                       getValue(thisQuestion, "questiontext"),
                                       files
                               ),
                               thisQuestion
                       )
               );
            }
        }
        return result;
    }
    interface QuestionParser{
        Question parse(String questionText, Element element);
    }
    static final class TrueFalseQuestionParser implements QuestionParser {
        @Override
        public TrueFalseQuestion parse(String questionText, Element element) {
            return new TrueFalseQuestion(questionText, round(Double.parseDouble(getValue(element, "fraction"))) == 1);
        }
    }
    @AllArgsConstructor
    static final class MultipleChoiceQuestionParser implements QuestionParser {
        private final Set<String> files;
        @Override
        public MultipleChoiceQuestion parse(String questionText, Element element) {
            val answers = element.getElementsByTagName("answer");
            val result = new HashSet<String>();
            for(int i = 0;i<answers.getLength();i++){
                val thisAnswer = (Element) answers.item(i);
                result.add(
                        FileParser.replaceFileReference(
                                getValue(thisAnswer, "answertext"),
                                files
                        )
                );
            }
            return new MultipleChoiceQuestion(questionText, result, "1".equals(getValue(element,"single")));
        }
    }
    static final class TextQuestionParser implements QuestionParser{
        @Override
        public TextQuestion parse(String questionText, Element element) {
            return new TextQuestion(questionText);
        }
    }
}
