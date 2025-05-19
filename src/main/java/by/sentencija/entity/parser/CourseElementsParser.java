package by.sentencija.entity.parser;

import by.sentencija.entity.XMLParser;
import by.sentencija.entity.element.CourseElement;
import lombok.val;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CourseElementsParser {
    public static Map<Integer, CourseElement> parse(String path, Map<String, XMLParser<? extends CourseElement>> parsableElements)
            throws ParserConfigurationException, IOException, SAXException {
        val folder = new File(path);
        if(!folder.exists()) throw new RuntimeException("Activities file doesn't exist");
        val files = folder.listFiles();
        if(files == null) throw new RuntimeException("Activities path should be a folder");
        val result = new HashMap<Integer,CourseElement>();
        for(val file: files){
            val fileName = file.getName();
            for(val entry : parsableElements.entrySet()){
                val prefix = entry.getKey();
                if(fileName.startsWith(prefix)){
                   val fileEnd = fileName.split("_")[1];
                   result.put(Integer.valueOf(fileEnd), entry.getValue().parse(file.getAbsolutePath()+"/"+prefix+".xml"));
                }
            }
        }
        return result;
    }
}
