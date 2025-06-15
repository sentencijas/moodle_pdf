package by.sentencija.entity.parser;

import by.sentencija.entity.Section;
import by.sentencija.entity.element.CourseElement;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SectionsParser {
    private final static Logger logger = LoggerFactory.getLogger(SectionsParser.class);

    public static List<Section> parse(String path, Map<Integer, CourseElement> courseElements)
            throws ParserConfigurationException, IOException, SAXException {
        val folder = new File(path);
        if(!folder.exists()) throw new RuntimeException("Указанный файл не существует");
        val files = folder.listFiles();
        if(files == null) throw new RuntimeException("Указанный путь должен быть каталогом");
        val result = new ArrayList<Section>();
        for (val file : files) {
            val sectionPath = file.getAbsolutePath()+"/section.xml";
            val section = new SectionParser(courseElements).parse(sectionPath);
            if(section.elements().contains(null)){
                logger.info("Секция содержит {} элементов, которые не имеют обработчиков (и поэтому равны null)",
                        section.elements().stream().filter(Objects::isNull).count());
            }
            if(!section.elements().isEmpty()) result.add(section);
        }
        return result;
    }
}
