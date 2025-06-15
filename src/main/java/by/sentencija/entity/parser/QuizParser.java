package by.sentencija.entity.parser;

import by.sentencija.entity.XMLParser;
import by.sentencija.entity.element.Quiz;
import by.sentencija.entity.question.Question;
import by.sentencija.entity.question.RandomQuestion;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class QuizParser extends XMLParser<Quiz> {
    private static final Logger logger = LoggerFactory.getLogger(QuizParser.class);
    private static final Random random = new Random();

    private final Map<Integer, HashMap<Integer, Question>> questions;
    private final Map<Integer, ArrayList<Question>> randomQuestions;

    public QuizParser(Map<Integer, HashMap<Integer, Question>> questions){
        this.questions = questions;
        randomQuestions = new HashMap<>();
        for(val entry: questions.entrySet()){
            val set = new ArrayList<Question>();
            for(val questionEntry : entry.getValue().entrySet()){
                val thisQuestion = questionEntry.getValue();
                if(thisQuestion.getClass() != RandomQuestion.class){
                    set.add(thisQuestion);
                }
            }
            randomQuestions.put(entry.getKey(), set);
        }
    }
    @Override
    protected Quiz parse(Document document) {

        val quiz = (Element) document.getElementsByTagName("quiz").item(0);
        val questions = quiz.getElementsByTagName("question_instance");
        val quizQuestions = new ArrayList<Question>();
        for(int i = 0;i<questions.getLength();i++){
            val question = (Element) questions.item(0);
            val categoryId = Integer.parseInt(getValue(question, "questioncategoryid"));
            if(!this.questions.containsKey(categoryId)){
                logger.error("Категория с идентификатором {} не найдена для вопроса {}", categoryId, question);
                continue;
            }
            val id = Integer.parseInt(getValue(question, "questionid"));
            if(!this.questions.get(categoryId).containsKey(id)){
                logger.error("Вопрос с идентификатором {} не найден для категории {}", id, categoryId);
                continue;
            }
            var questionToAdd = this.questions.get(categoryId).get(id);
            if(questionToAdd.getClass() == RandomQuestion.class){
                val categoryRandomQuestions = randomQuestions.get(categoryId);
                if(categoryRandomQuestions.isEmpty()){
                    logger.error("Была произведена попытка добавить случайный вопрос из категории {}" +
                                    ", но все вопросы уже были использованы, в тесте {}",
                            categoryId, quiz.getAttribute("id"));
                    continue;
                }
                questionToAdd = categoryRandomQuestions.remove(random.nextInt(categoryRandomQuestions.size()));
            }
            quizQuestions.add(questionToAdd);
        }
        val contextId = getContextId(document);
        return new Quiz(
                contextId,
                FileParser.replaceFileReference(getValue(quiz, "intro"), contextId),
                getValue(quiz, "name"),
                quizQuestions
        );

    }
}
