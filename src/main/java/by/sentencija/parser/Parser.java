package by.sentencija.parser;

import by.sentencija.entity.MoodleCourse;
import by.sentencija.entity.parser.CourseParser;
import by.sentencija.entity.parser.PluginFilesParser;
import by.sentencija.entity.parser.SectionParser;
import lombok.val;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Parser {
    public static MoodleCourse parse(String path) throws ParserConfigurationException, IOException, SAXException {
        val course = new CourseParser().parse(path + "/course/course.xml");
        val fileMap = new PluginFilesParser(path+"/files").parse(path+"/files.xml");
        val sections = new SectionParser().parse(path+"");
        return new MoodleCourse(course, fileMap, null);
    }
}
