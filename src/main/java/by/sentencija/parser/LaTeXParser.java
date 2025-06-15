package by.sentencija.parser;

import lombok.val;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LaTeXParser {
    private final static Logger logger = LoggerFactory.getLogger(LaTeXParser.class);

    private static int fileCounter = 0;

    public static String parse(String html){
        val result = new StringBuilder();

        Pattern pattern = Pattern.compile("\\$(.+?)\\$");
        Matcher matcher = pattern.matcher(html);

        while (matcher.find()) {
            val latex = matcher.group(1);
            matcher.appendReplacement(result, Matcher.quoteReplacement(createReplacement()));
            createAndSave(latex, Path.of("./temp/files/latex/file" + fileCounter + ".png"));
            fileCounter++;
        }

        return result.toString();
    }
    private static String createReplacement(){
        return "<img src=\"temp/files/latex/file" + fileCounter + ".png\"/>";
    }
    private static void createAndSave(String latex, Path path) {
        // Parse LaTeX and create icon
        TeXFormula formula = new TeXFormula(latex);
        var icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20);

        // Create BufferedImage and draw icon
        BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        val g2 = image.createGraphics();
        g2.setColor(Color.WHITE); // Optional: background color
        g2.fillRect(0, 0, icon.getIconWidth(), icon.getIconHeight());
        icon.paintIcon(null, g2, 0, 0);

        try {
            ImageIO.write(image, "png", path.toFile());
        } catch (IOException e) {
           logger.error("Cannot save LaTeX image file at {}", path);
        }
    }
}
