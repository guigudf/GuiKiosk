package com.gdsa.kiosk;

import com.gdsa.kiosk.model.Cart;
import com.gdsa.kiosk.model.Category;
import com.gdsa.kiosk.model.MenuItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.math.BigDecimal;

public class CartTest {

    @Test
    public void addsAndSums() {
        MenuItem coffee = new MenuItem(
                "Coffee",
                new BigDecimal(3),
                Category.DRINK
        );
        int qty = 2;
        Cart cart = new Cart();


        cart.add(coffee, qty);

        BigDecimal subtotal = cart.getSubtotal();

        Assertions.assertEquals(
                coffee.getPrice().multiply(new BigDecimal(qty)),
                subtotal
        );
    }
}
