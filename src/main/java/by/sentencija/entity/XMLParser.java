package by.sentencija.entity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public abstract class XMLParser<T> {
    abstract protected T parse(Document document);
    public T parse(String pathToXML) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(new File(pathToXML));
        doc.getDocumentElement().normalize();
        return parse(doc);
    }
    protected static String getValue(Element element, String nodeName){
        return element.getElementsByTagName(nodeName).item(0).getTextContent();
    }
    protected static String getValue(Element element, String nodeName, String wrapTag){
        return "<" + wrapTag + ">" + getValue(element, nodeName) + "</" + wrapTag + ">";
    }
    protected static Long getContextId(Document document){
        return Long.parseLong(((Element) document.getElementsByTagName("activity").item(0)).getAttribute("contextid"));
    }
}