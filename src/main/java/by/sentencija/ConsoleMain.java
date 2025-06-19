package by.sentencija;

import by.sentencija.ui.UI;
import lombok.val;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.awt.image.ConvolveOp;
import java.io.IOException;
import java.nio.file.Path;

public class ConsoleMain {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        if(args.length == 0){
            System.out.println("""
                    Usage: provide 2 arguments which are:
                    1.Path to the backup .mbz file;
                    2.Path to the folder to save the result PDF file to. If this is not provided, the file will be save to the same directory as the source file.
                    """);
        }
        val source = args[0];
        val target = args.length == 2 ? args[1] : Path.of(source).getParent().toString();
        UI.parse(source, target, true);
    }
}
