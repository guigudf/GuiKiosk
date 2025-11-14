package com.gdsa.kiosk;

import com.gdsa.kiosk.model.*;
import com.gdsa.kiosk.repo.FileReceiptRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import java.math.BigDecimal;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

class ReceiptFileTests {
    @TempDir
    Path temp;

    @Test
    void writes_receipt_file() throws Exception {
        var cart = new Cart();
        cart.add(new MenuItem("Coffee", new BigDecimal("3.00"), Category.DRINK), 2);
        var svc = new ReceiptService(new java.math.BigDecimal("0.00")::add); // dummy tax, or use FlatRateTaxCalculator
        var saver = new ReceiptSaver(svc, new FileReceiptRepository(temp));
        Path p = saver.renderAndSave(cart);
        assertTrue(Files.exists(p));
        assertTrue(Files.size(p) > 0);
    }
}