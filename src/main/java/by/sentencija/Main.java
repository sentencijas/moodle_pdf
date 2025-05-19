package by.sentencija;

import by.sentencija.parser.Parser;
import lombok.val;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        if(args.length == 0) {
            System.out.println("Provide the path to the unarchived folder of the backup");
            return;
        }
        val result = Parser.parse(args[0]);
    }
}
