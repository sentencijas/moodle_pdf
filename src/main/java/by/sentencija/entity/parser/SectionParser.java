package by.sentencija.entity.parser;

import by.sentencija.entity.Section;
import by.sentencija.entity.XMLParser;
import by.sentencija.entity.element.CourseElement;
import lombok.AllArgsConstructor;
import lombok.val;
import org.w3c.dom.Document;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@AllArgsConstructor
public class SectionParser extends XMLParser<Section> {
    private final Map<Integer, CourseElement> courseElements;
    @Override
    protected Section parse(Document document) {
        val sequence = document.getElementsByTagName("sequence").item(0).getTextContent();
        return new Section(
                document.getElementsByTagName("name").item(0).getTextContent(),
                "".equals(sequence) ? List.of() :
                Stream.of(sequence.split(",")).map(Integer::valueOf).map(courseElements::get).toList()
        );
    }
}
