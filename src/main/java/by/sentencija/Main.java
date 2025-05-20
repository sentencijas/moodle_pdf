package by.sentencija;

import by.sentencija.parser.Extractor;
import by.sentencija.parser.FileHelper;
import by.sentencija.parser.Parser;
import lombok.val;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Paths;

public class Main {
    private final static String TEMPORARY_FOLDER = "temp";
    private final static String BACKUP_FOLDER_NAME = "/backup";
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        Extractor.extract("src/main/resources/backup-moodle2-course-2-coursesn-20250519-1901.mbz");
        val result = Parser.parse(TEMPORARY_FOLDER + BACKUP_FOLDER_NAME);
        FileHelper.deleteDirectoryRecursively(Paths.get(TEMPORARY_FOLDER));
    
     //запуск
        String fontPath = "moodle_pdf\\src\\pdfmaker\\FreeSans.ttf";
        String outputFilePath = "moodle_pdf\\src\\pdfmaker\\output.pdf";
        PdfCourseRenderer.generate(result, fontPath, outputFilePath);
    
    
    
    
    }
}