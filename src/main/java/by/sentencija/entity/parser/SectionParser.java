package by.sentencija.entity.parser;

import by.sentencija.entity.Section;
import by.sentencija.entity.XMLParser;
import lombok.val;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SectionParser extends XMLParser<Section> {
    @Override
    protected Section parse(Document document) {
        val sequence = getValue((Element) document,"sequence");
        return null;
    }
}
