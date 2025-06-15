package by.sentencija.entity.parser;

import by.sentencija.entity.XMLParser;
import by.sentencija.entity.element.Label;
import lombok.val;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class LabelParser extends XMLParser<Label> {
    @Override
    protected Label parse(Document document) {
        val element = (Element) document.getElementsByTagName("label").item(0);
        val contextId = getContextId(document);
        return new Label(
                contextId,
                FileParser.replaceFileReference(getValue(element, "intro", "b"), contextId)
        );
    }
}
