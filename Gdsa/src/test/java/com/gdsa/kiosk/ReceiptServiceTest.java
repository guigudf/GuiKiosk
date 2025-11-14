package com.gdsa.kiosk;

import com.gdsa.kiosk.model.*;
import com.gdsa.kiosk.repo.InMemoryCatalogRepository;
import org.junit.jupiter.api.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptServiceTest {
    private Cart cart;
    private ReceiptService receipt;

    @BeforeEach
    void setup() {
        cart = new Cart();
        FlatRateTaxCalculator taxCalc = new FlatRateTaxCalculator(new BigDecimal("0.06"));
        receipt = new ReceiptService(taxCalc);
    }

    @Test void totals_and_tax(){
        var repo = new InMemoryCatalogRepository();
        var cart = new Cart();
        cart.add(repo.all().getFirst(), 2); // coffee
        var svc = new ReceiptService(new FlatRateTaxCalculator(new BigDecimal("0.06")));
        var lines = svc.render(cart);
        assertTrue(lines.getLast().contains("Total:"));
    }

    @Test
    void ReceiptTotalsTest() {
        cart.add(new MenuItem("Coffee", new BigDecimal("3.00"), Category.DRINK), 2);
        cart.add(new MenuItem("Muffin", new BigDecimal("2.00"), Category.FOOD), 1);

        List<String> lines = receipt.render(cart);

        BigDecimal expectedSubtotal = new BigDecimal("8.00");
        BigDecimal expectedTax = expectedSubtotal.multiply(new BigDecimal("0.06"));
        BigDecimal expectedTotal = expectedSubtotal.add(expectedTax);

        assertTrue(lines.stream().anyMatch(l -> l.contains("Subtotal")));
        assertTrue(lines.stream().anyMatch(l -> l.contains("Tax")));
        assertTrue(lines.stream().anyMatch(l -> l.contains("Total")));

        assertTrue(lines.get(lines.size() - 3).contains("Subtotal"));
        assertTrue(lines.get(lines.size() - 2).contains("Tax"));
        assertTrue(lines.getLast().contains("Total"));


        assertEquals(2, cart.items().size(), "Should have 2 item lines before totals");
        assertEquals(expectedTotal.setScale(2, RoundingMode.HALF_UP), expectedSubtotal.add(expectedTax).setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void EmptyCartTest() {
        List<String> lines = receipt.render(cart);
        assertTrue(lines.stream().anyMatch(l -> l.contains("Subtotal")));
        assertTrue(lines.getLast().contains("0"));
    }
}