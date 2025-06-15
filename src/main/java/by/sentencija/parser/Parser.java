package by.sentencija.parser;

import by.sentencija.entity.MoodleCourse;
import by.sentencija.entity.parser.AssignmentParser;
import by.sentencija.entity.parser.CourseElementsParser;
import by.sentencija.entity.parser.CourseParser;
import by.sentencija.entity.parser.FileParser;
import by.sentencija.entity.parser.LabelParser;
import by.sentencija.entity.parser.PageParser;
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
        FileHelper.createLaTeXFilesFolder();
        val questions = new QuestionsParser().parse(path + "/questions.xml");
        val courseElements = CourseElementsParser.parse(
                path+"/activities",
                Map.of(
                        "quiz",  new QuizParser(questions),
                        "page", new PageParser(),
                        "label", new LabelParser(),
                        "assign", new AssignmentParser()
                )
        );
        val sections = SectionsParser.parse(path+"/sections", courseElements);
        return new MoodleCourse(course, sections);
    }
}
