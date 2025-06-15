package by.sentencija;

import by.sentencija.ui.UI;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class ConsoleMain {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        UI.parse("./src/main/resources/b.mbz", "src/main/resources", false);
    }
    //TODO добавить тест с картинкой из теста 7 или итогового теста
}
