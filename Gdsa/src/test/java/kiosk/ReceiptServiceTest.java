package kiosk;

import com.gdsa.kiosk.model.*;
import com.gdsa.kiosk.repo.*;
import org.junit.jupiter.api.*;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class ReceiptServiceTest {
    @Test void totals_and_tax(){
        var repo = new InMemoryCatalogRepository();
        var cart = new Cart();
        cart.add(repo.all().getFirst(), 2); // coffee
        var svc = new ReceiptService(new FlatRateTaxCalculator(new BigDecimal("0.06")));
        var lines = svc.render(cart);
        assertTrue(lines.getLast().contains("Total:"));
    }
}