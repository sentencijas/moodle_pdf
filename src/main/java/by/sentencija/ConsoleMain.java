package by.sentencija;

import by.sentencija.ui.ArchiveExtractorUI;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class ConsoleMain {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        ArchiveExtractorUI.parse("./src/main/resources/backup-moodle2-course-2-coursesn-20250519-1901.mbz", "src/main/resources", false);
    }
}
