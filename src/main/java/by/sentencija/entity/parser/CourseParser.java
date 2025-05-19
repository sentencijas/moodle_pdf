package by.sentencija.entity.parser;

import by.sentencija.entity.Course;
import by.sentencija.entity.XMLParser;
import org.w3c.dom.Document;

public class CourseParser extends XMLParser<Course> {
    @Override
    protected Course parse(Document document) {
        return new Course(
                document.getElementsByTagName("shortname").item(0).getFirstChild().getNodeValue(),
                document.getElementsByTagName("fullname").item(0).getFirstChild().getNodeValue()
        );
    }
}
