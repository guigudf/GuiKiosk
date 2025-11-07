package com.gdsa.kiosk.GUI;

import com.gdsa.kiosk.UI.KioskUI;

import javax.swing.SwingUtilities;

public class MainGUI {
    public static void main() {
        SwingUtilities.invokeLater(() -> {
            KioskUI ui = new KioskUI();
            ui.setVisible(true);
        });
    }
}