package by.sentencija.entity.parser;

import by.sentencija.entity.question.MultipleChoiceQuestion;
import by.sentencija.entity.question.Question;
import by.sentencija.entity.XMLParser;
import by.sentencija.entity.question.TrueFalseQuestion;
import lombok.val;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.round;

public class QuestionsParser extends XMLParser<Map<Integer,Question>> {
    private final Map<String, ? extends QuestionParser> PARSERS = Map.of(
            "truefalse", new TrueFalseQuestionParser(),
            "multichoice", new MultipleChoiceQuestionParser()
    );

    @Override
    protected Map<Integer,Question> parse(Document document) {
        val result = new HashMap<Integer, Question>();
        val questions = document.getElementsByTagName("question");
        for(int i = 0;i<questions.getLength();i++){
            val thisQuestion = (Element)questions.item(i);
            val answersPosition = thisQuestion.getElementsByTagName("answers").item(0);
            val parserName = answersPosition.getParentNode().getNodeName().split("_")[2];
            if(PARSERS.containsKey(parserName)){
               result.put(Integer.parseInt(thisQuestion.getAttribute("id")), PARSERS.get(parserName).parse(thisQuestion));
            }
        }
        return result;
    }
    interface QuestionParser{
        Question parse(Element element);
    }
    static final class TrueFalseQuestionParser implements QuestionParser {
        @Override
        public TrueFalseQuestion parse(Element element) {
            return new TrueFalseQuestion(round(Double.parseDouble(getValue(element, "fraction"))) == 1);
        }
    }
    static final class MultipleChoiceQuestionParser implements QuestionParser {
        @Override
        public MultipleChoiceQuestion parse(Element element) {
            return new MultipleChoiceQuestion(Map.of()); //TODO
        }
    }
}
