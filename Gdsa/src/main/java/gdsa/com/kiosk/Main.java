package gdsa.com.kiosk;

import gdsa.com.kiosk.UI.KioskUI;
import gdsa.com.kiosk.model.MenuItem;

import java.awt.*;
import java.math.BigDecimal;

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
