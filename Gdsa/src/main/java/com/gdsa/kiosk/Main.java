package com.gdsa.kiosk;

import com.gdsa.kiosk.UI.KioskUI;

public class Main {
//    public static void main(String[] args) {
//        MenuItem coffee = new MenuItem("Coffee", new BigDecimal("3.99"));
//
//        System.out.println(coffee.toString());
//
//        String name= "Guilherme";
//
//        name.equals("Guilherme");
//    }

    public static void main() {
        KioskUI kiosk = new KioskUI();
        kiosk.setSize(1024, 720);
        kiosk.setVisible(true);
    }
}
