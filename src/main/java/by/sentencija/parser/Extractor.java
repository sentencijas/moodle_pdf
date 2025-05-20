package by.sentencija.parser;

import lombok.val;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;

public class Extractor {
    public static void extract(String gzipFile) {
        val outputDir = "temp";

        try {
            Path tempDir = Paths.get(outputDir, "temp");
            Files.createDirectories(tempDir);

            Path innerArchive = tempDir.resolve("inner.archive");

            // Step 1: Decompress .gz
            try (
                    GZIPInputStream gis = new GZIPInputStream(new FileInputStream(gzipFile));
                    FileOutputStream fos = new FileOutputStream(innerArchive.toFile())
            ) {
                gis.transferTo(fos);
            }

            // Step 2: Try various archive formats
            Path finalOutput = Paths.get(outputDir, "backup");
            Files.createDirectories(finalOutput);

            try (InputStream is = new BufferedInputStream(new FileInputStream(innerArchive.toFile()));
                 ArchiveInputStream ais = tryDetectArchiveFormat(is)) {

                if (ais == null) {
                    System.err.println("Unknown archive format.");
                    return;
                }

                ArchiveEntry entry;
                while ((entry = ais.getNextEntry()) != null) {
                    if (!ais.canReadEntryData(entry)) {
                        continue;
                    }
                    Path entryPath = finalOutput.resolve(entry.getName()).normalize();
                    if (entry.isDirectory()) {
                        Files.createDirectories(entryPath);
                    } else {
                        Files.createDirectories(entryPath.getParent());
                        try (OutputStream os = Files.newOutputStream(entryPath)) {
                            ais.transferTo(os);
                        }
                    }
                }
            }

        } catch (IOException | ArchiveException e) {
            e.printStackTrace();
        }

    }

    private static ArchiveInputStream tryDetectArchiveFormat(InputStream is) throws IOException, ArchiveException {
        is.mark(10240);  // mark for reset
        try {
            return new TarArchiveInputStream(new BufferedInputStream(is));
        } catch (Exception e) {
            is.reset();
        }

        is.mark(10240);
        try {
            return new ArArchiveInputStream(new BufferedInputStream(is));
        } catch (Exception e) {
            is.reset();
        }

        is.mark(10240);
        try {
            return new CpioArchiveInputStream(new BufferedInputStream(is));
        } catch (Exception e) {
            is.reset();
        }

        return null; // unknown format
    }
}