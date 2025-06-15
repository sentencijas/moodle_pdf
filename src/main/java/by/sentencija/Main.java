package by.sentencija;

import by.sentencija.ui.UI;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UI().createAndShowGUI());
    }
}
