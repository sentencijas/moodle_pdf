package by.sentencija;

import by.sentencija.ui.ArchiveExtractorUI;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class ConsoleMain {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        ArchiveExtractorUI.parse("./src/main/resources/backup-moodle2-course-3-тестовый_курс-20250521-1305.mbz", "src/main/resources", true);
    }
}
