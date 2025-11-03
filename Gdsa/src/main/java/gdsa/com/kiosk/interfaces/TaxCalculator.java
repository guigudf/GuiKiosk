package gdsa.com.kiosk.interfaces;

import java.math.BigDecimal;

public interface TaxCalculator {
    BigDecimal tax(BigDecimal amount);
}
