package by.sentencija.entity.parser;

import by.sentencija.entity.XMLParser;
import by.sentencija.entity.element.Assignment;
import lombok.val;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class AssignmentParser extends XMLParser<Assignment> {
    @Override
    protected Assignment parse(Document document) {
        val element = (Element) document.getElementsByTagName("assign").item(0);
        val contextId = getContextId(document);
        return new Assignment(
                contextId,
                getValue(element, "name"),
                FileParser.replaceFileReference(getValue(element, "intro"), contextId)
        );
    }
}
