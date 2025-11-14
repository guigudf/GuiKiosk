package com.gdsa.kiosk;

import com.gdsa.kiosk.UI.KioskUI;
import com.gdsa.kiosk.UI.MainFrame;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            MainFrame f = new MainFrame();
            f.setVisible(true);
        });
    }

//    public static void main() {
//        KioskUI kiosk = new KioskUI();
//        kiosk.setSize(1024, 720);
//        kiosk.setVisible(true);
//    }
}
