package by.sentencija.ui;

import by.sentencija.parser.Extractor;
import by.sentencija.parser.FileHelper;
import by.sentencija.parser.Parser;
import by.sentencija.pdfmaker.MoodlePdfGenerator;
import lombok.val;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class ArchiveExtractorUI {

    private JFrame frame;
    private JTextField filePathField;
    private JButton chooseFileButton;
    private JButton processButton;
    private JProgressBar progressBar;
    private File selectedFile;
    private JTextField folderPathField;
    private JButton chooseFolderButton;

    public void createAndShowGUI() {
        frame = new JFrame("Конвертация резервной копии в PDF");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 150);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === Top: File chooser row ===
        JPanel filePanel = new JPanel(new BorderLayout(5, 5));
        filePathField = new JTextField();
        filePathField.setEditable(false);
        chooseFileButton = new JButton("Выбрать файл резервной копии (.mbz)");

        chooseFileButton.addActionListener(e -> chooseFile());

        filePanel.add(filePathField, BorderLayout.CENTER);
        filePanel.add(chooseFileButton, BorderLayout.EAST);

        JPanel folderPanel = new JPanel(new BorderLayout(5, 5));
        folderPathField = new JTextField();
        folderPathField.setEditable(false);
        chooseFolderButton = new JButton("Choose Folder");
        chooseFolderButton.addActionListener(e -> chooseFolder());
        folderPanel.add(folderPathField, BorderLayout.CENTER);
        folderPanel.add(chooseFolderButton, BorderLayout.EAST);

        Box topBox = Box.createVerticalBox();
        topBox.add(filePanel);
        topBox.add(Box.createVerticalStrut(8)); // spacing between rows
        topBox.add(folderPanel);

        frame.add(topBox, BorderLayout.NORTH);

        // === Middle: Progress bar ===
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(false);
        progressBar.setStringPainted(true);
        frame.add(progressBar, BorderLayout.CENTER);

        // === Bottom: Process button aligned right ===
        processButton = new JButton("Обработать");
        processButton.setEnabled(false);

        processButton.addActionListener(e -> {
            try {
                parse(filePathField.getText(), folderPathField.getText(), true);
            } catch (ParserConfigurationException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (SAXException ex) {
                throw new RuntimeException(ex);
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.add(processButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("MBZ files (*.mbz)", "mbz");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (file.getName().toLowerCase().endsWith(".mbz")) {
                selectedFile = file;
                filePathField.setText(selectedFile.getAbsolutePath());
                processButton.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a valid .mbz file.", "Invalid File", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void chooseFolder() {
        JFileChooser folderChooser = new JFileChooser();
        folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        folderChooser.setAcceptAllFileFilterUsed(false);

        int result = folderChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File folder = folderChooser.getSelectedFile();
            folderPathField.setText(folder.getAbsolutePath());
        }
    }

    public static void parse(String archivePath, String targetPath, boolean deleteUnarchived) throws ParserConfigurationException, IOException, SAXException {
        final String TEMPORARY_FOLDER = "temp";
        final String BACKUP_FOLDER_NAME = "/backup";

        Extractor.extract(archivePath);
        val result = Parser.parse(TEMPORARY_FOLDER + BACKUP_FOLDER_NAME);

        //TODO use targetPath here to save PDF
        String fontPath = "src\\main\\resources\\FreeSans.ttf";
        new MoodlePdfGenerator().generatePdfFromCourse(result, fontPath, targetPath+"/output.pdf");
        if(deleteUnarchived) FileHelper.deleteDirectoryRecursively(Paths.get(TEMPORARY_FOLDER));
    }
}

