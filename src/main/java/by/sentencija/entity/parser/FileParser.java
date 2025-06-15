package by.sentencija.entity.parser;

import by.sentencija.entity.PluginFile;
import by.sentencija.parser.LaTeXParser;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileParser {
    private final static Logger logger = LoggerFactory.getLogger(FileParser.class);

    public static void createFilesFolder(HashMap<Long, HashMap<String, String>> map, String target) throws IOException {
        val targetFolder = Paths.get(target);
        Files.createDirectories(targetFolder);

        for(val context : map.entrySet()) {
            for (val entry : context.getValue().entrySet()) {
                val sourceFile = Paths.get(entry.getValue());
                val contextFolder = targetFolder.resolve(Long.toString(context.getKey()));
                Files.createDirectories(contextFolder);
                val aFolder = contextFolder.resolve(entry.getKey());
                logger.info("Copied file from {} to {}", sourceFile, aFolder);
                Files.copy(sourceFile, aFolder, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }
    public static String replaceFileReference(String str, Long contextId) {
        Pattern pattern = Pattern.compile("src=([\"'])(.*?)\\1");
        Matcher matcher = pattern.matcher(str);

        val result = new StringBuilder();

        while (matcher.find()) {
            val value = matcher.group();
            if(value.split("\"")[1].startsWith("data")) continue;
            val split = value.split("/");
            var fileName = split[split.length-1];
            if(fileName.contains("?")) fileName = fileName.split("\\?")[0];
            String replacement = "src=\"" + "temp/files/" + contextId + "/" + fileName + "\"";
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }

        matcher.appendTail(result);
        val res = result.toString().replaceAll("\\s*dir=\"ltr\"", "");
        return LaTeXParser.parse(res);
    }
}
