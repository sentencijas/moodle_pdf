package by.sentencija.entity.parser;

import by.sentencija.entity.PluginFile;
import by.sentencija.entity.XMLParser;
import lombok.AllArgsConstructor;
import lombok.val;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.HashMap;

@AllArgsConstructor
public class PluginFilesParser extends XMLParser<HashMap<Long, HashMap<String, String>>> {
    private final String filesFolder;

    @Override
    protected HashMap<Long, HashMap<String, String>> parse(Document document) {
       val result = new HashMap<Long, HashMap<String, String>>();
       val entries = document.getElementsByTagName("file");
       for(int i = 0;i<entries.getLength();i++){
           val entry = (Element)entries.item(i);
           val filename = getValue(entry, "filename");
           if(".".equals(filename)) continue;
           val hash = getValue(entry, "contenthash");
           val contextId = Long.parseLong(getValue(entry, "contextid"));
           if(!result.containsKey(contextId)){
               result.put(contextId, new HashMap<>());
           }
           result.get(contextId).put(filename,
                    filesFolder + "/" + hash.substring(0,2) + "/" + hash
           );
       }
       return result;
    }
}
