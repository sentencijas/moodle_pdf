package by.sentencija.entity.parser;

import by.sentencija.entity.PluginFile;
import lombok.val;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileParser {
    public static void createFilesFolder(HashMap<String, PluginFile> map, String target) throws IOException {
        val targetFolder = Paths.get(target);
        Files.createDirectories(targetFolder);

        for(val entry : map.entrySet()){
            val sourceFile = Paths.get(entry.getValue().path());
            Files.copy(sourceFile, targetFolder.resolve(entry.getKey()), StandardCopyOption.REPLACE_EXISTING);
        }
    }
    public static String replaceFileReference(String str, Set<String> fileNames){
        Pattern pattern = Pattern.compile("src\\s*=\\s*\"(.*?)\"");
        Matcher matcher = pattern.matcher(str);
        while(matcher.find()){
            val value = matcher.group();
            val split = value.split("/");
            val fileName = split[split.length-1];
            str = matcher.replaceFirst("src=\"temp/files/" + fileName + "\"");
        }
        return str;
    }
}
