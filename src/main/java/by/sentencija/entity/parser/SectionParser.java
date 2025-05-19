package by.sentencija.entity.parser;

import by.sentencija.entity.XMLParser;
import lombok.val;
import org.w3c.dom.Document;

import java.util.List;
import java.util.stream.Stream;

public class SectionParser extends XMLParser<List<Integer>> {
    @Override
    protected List<Integer> parse(Document document) {
        val sequence = document.getElementsByTagName("sequence").item(0).getTextContent();
        return "".equals(sequence) ? List.of() :
                Stream.of(sequence.split(",")).map(Integer::valueOf).toList();
    }
}
