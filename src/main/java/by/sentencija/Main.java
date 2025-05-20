package by.sentencija;

import by.sentencija.ui.ArchiveExtractorUI;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ArchiveExtractorUI().createAndShowGUI());
    }
}
