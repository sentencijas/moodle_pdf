package by.sentencija.entity.parser;

import by.sentencija.entity.Section;
import by.sentencija.entity.element.CourseElement;
import lombok.val;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SectionsParser {
    public static List<Section> parse(String path, Map<Integer, CourseElement> courseElements) throws ParserConfigurationException, IOException, SAXException {
        val folder = new File(path);
        if(!folder.exists()) throw new RuntimeException("Sections file doesn't exist");
        val files = folder.listFiles();
        if(files == null) throw new RuntimeException("Sections path should be a folder");
        val result = new ArrayList<Section>();
        for (val file : files) {
            val sectionElements = new SectionParser().parse(file.getAbsolutePath()+"/section.xml");
            if(sectionElements.isEmpty()) continue;
            val elements = sectionElements.stream().map(courseElements::get).toList();
            result.add(new Section(elements));
        }
        return result;
    }
}
