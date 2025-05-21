package by.sentencija.parser;

import by.sentencija.entity.MoodleCourse;
import by.sentencija.entity.parser.CourseElementsParser;
import by.sentencija.entity.parser.CourseParser;
import by.sentencija.entity.parser.FileParser;
import by.sentencija.entity.parser.PluginFilesParser;
import by.sentencija.entity.parser.QuestionsParser;
import by.sentencija.entity.parser.QuizParser;
import by.sentencija.entity.parser.SectionsParser;
import lombok.val;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Map;

public class Parser {
    public static MoodleCourse parse(String path) throws ParserConfigurationException, IOException, SAXException {
        val course = new CourseParser().parse(path + "/course/course.xml");
        val fileMap = new PluginFilesParser(path+"/files").parse(path+"/files.xml");
        FileParser.createFilesFolder(fileMap, "./temp/files");
        val files = fileMap.keySet();
        val questions = new QuestionsParser(files).parse(path + "/questions.xml");
        val courseElements = CourseElementsParser.parse(
                path+"/activities",
                Map.of(
                        "quiz",  new QuizParser(files, questions)
                )
        );
        val sections = SectionsParser.parse(path+"/sections", courseElements);
        return new MoodleCourse(course, fileMap, sections);
    }
}
