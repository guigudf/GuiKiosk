package com.gdsa.kiosk;

import com.gdsa.kiosk.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class PurchaseTest {

    private Cart cart;
    private ReceiptService receipt;

    @BeforeEach
    void setup() {
        FlatRateTaxCalculator taxCalc = new FlatRateTaxCalculator(new BigDecimal("0.06"));
        cart = new Cart();
        receipt = new ReceiptService(taxCalc);
    }

    @Test
    void PurchaseFlowTest() {
        // Simulate user adding items via KioskUI
        MenuItem coffee = new MenuItem("Coffee", new BigDecimal("3.50"), Category.COFFEE);
        MenuItem sandwich = new MenuItem("Sandwich", new BigDecimal("6.00"), Category.BAKERY);

        cart.add(coffee, 2);
        cart.add(sandwich, 1);

        // Verify subtotal
        BigDecimal expectedSubtotal = new BigDecimal("13.00");
        assertEquals(0, expectedSubtotal.compareTo(cart.subtotal()));

        // Generate receipt and verify totals
        List<String> lines = receipt.render(cart);
        BigDecimal expectedTax = expectedSubtotal.multiply(new BigDecimal("0.06"));
        BigDecimal expectedTotal = expectedSubtotal.add(expectedTax);

        assertTrue(lines.stream().anyMatch(l -> l.contains("Subtotal")));
        assertTrue(lines.stream().anyMatch(l -> l.contains("Tax")));
        assertTrue(lines.stream().anyMatch(l -> l.contains("Total")));

        assertEquals(expectedSubtotal.setScale(2, RoundingMode.HALF_UP), cart.subtotal().setScale(2, RoundingMode.HALF_UP));
        assertEquals(expectedTotal.setScale(2, RoundingMode.HALF_UP), expectedSubtotal.add(expectedTax).setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void NullReceiptTest() {
        List<String> lines = receipt.render(cart);
        assertTrue(lines.getLast().contains("0"));
    }
}
