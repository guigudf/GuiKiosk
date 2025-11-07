package com.gdsa.kiosk;

import com.gdsa.kiosk.model.*;
import com.gdsa.kiosk.repo.SqliteReceiptRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.math.BigDecimal;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class SqliteReceiptRepositoryTest {
    @TempDir Path temp;

    @Test void saves_receipt_and_returns_id() throws Exception {
        // Arrange
        Cart cart = new Cart();
        cart.add(new MenuItem("Americano", new BigDecimal("3.50"),  Category.COFFEE), 2);
        ReceiptService svc = new ReceiptService(new FlatRateTaxCalculator(new BigDecimal("0.06")));
        var repo = new SqliteReceiptRepository(temp.resolve("receipts.db"));
        var saver = new ReceiptDbSaver(svc, repo);

        // Act
        long id = saver.renderAndSave(cart, "Alice");

        // Assert
        assertTrue(id > 0);
    }
}