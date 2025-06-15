package by.sentencija.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public class FileHelper {
    private final static Logger logger = LoggerFactory.getLogger(FileHelper.class);

    public static void deleteDirectoryRecursively(Path path) throws IOException {
        if (Files.exists(path)) {
            Files.walk(path)
                    .sorted(Comparator.reverseOrder()) // delete files before directories
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            System.err.println("Failed to delete " + p + ": " + e.getMessage());
                        }
                    });
        }
    }
    public static void createLaTeXFilesFolder(){
        try {
            Files.createDirectories(Path.of("./temp/files/latex"));
        } catch (IOException exception){
            logger.error("Cannot create the LaTeX files directory");
        }
    }
}
