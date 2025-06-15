package by.sentencija.entity.parser;

import by.sentencija.entity.XMLParser;
import by.sentencija.entity.element.Page;
import lombok.val;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PageParser extends XMLParser<Page> {
    @Override
    protected Page parse(Document document) {
        val element = (Element) document.getElementsByTagName("page").item(0);
        val contextId = getContextId(document);
        val result = new Page(
                contextId,
                getValue(element, "name"),
                FileParser.replaceFileReference(getValue(element, "intro"), contextId),
                FileParser.replaceFileReference(getValue(element, "content"), contextId)
        );
        return result;
    }
}
