package com.gdsa.kiosk;

import com.gdsa.kiosk.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class CartAddTest {

    private Cart cart;
    private MenuItem coffee;
    private MenuItem muffin;

    @BeforeEach
    void setup() {
        cart = new Cart();
        coffee = new MenuItem("Coffee", new BigDecimal("3.50"), Category.DRINK);
        muffin = new MenuItem("Muffin", new BigDecimal("2.25"), Category.BAKERY);
    }

    @Test
    void AddSingleItemTest() {
        cart.add(coffee, 1);
        assertEquals(1, cart.items().size());
        assertEquals(new BigDecimal("3.50"), cart.getSubtotal());
    }

    @Test
    void AddMultipleItemsTest() {
        cart.add(coffee, 2);
        cart.add(muffin, 1);
        assertEquals(2, cart.items().size());
        assertEquals(new BigDecimal("9.25"), cart.getSubtotal());
    }

    @Test
    void AddDuplicateItemsTest() {
        cart.add(coffee, 1);
        cart.add(coffee, 2);
        assertEquals(1, cart.items().size());
        assertEquals(3, cart.items().getFirst().getQuantity());
    }

    @Test
    void EmptyCartSubtotalTest() {
        assertEquals(new BigDecimal("0"), cart.getSubtotal());
    }
}
