package com.gdsa.kiosk.model;

import com.gdsa.kiosk.interfaces.TaxCalculator;

import java.math.BigDecimal;
import java.util.*;

public class Cart {
    private final Map<String, CartItem> lines = new LinkedHashMap<>();
    private static final int MAX_QUANTITY_PER_ITEM = 10;

    public void add(MenuItem item, int qty){
        if(qty <= 0) throw new IllegalArgumentException("qty must be positive");

        String key = item.getName();
        CartItem existing = lines.get(key);

        int totalQty;

        if (existing != null) {
            totalQty = existing.getQuantity() + qty;
        } else {
            totalQty = qty;
        }

        if (totalQty > MAX_QUANTITY_PER_ITEM) {
            throw new IllegalArgumentException("Cannot add more than 10 units of the same item");
        }

        lines.merge(
                item.getName(),
                new CartItem(item, qty),
                (oldLine, newLine) -> {
            oldLine.setQuantity(totalQty);
            return oldLine;
        });
    }

    public void remove(String name){
         lines.remove(name);
    }

    public List<CartItem> items(){
         return List.copyOf(lines.values());
    }

    public boolean isEmpty(){
         return lines.isEmpty();
    }

    public BigDecimal getSubtotal(){
        return lines.values().stream()
                .map(CartItem::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void clear() {
         lines.clear();
    }

    public BigDecimal getTax(TaxCalculator taxCalculator){
        if(taxCalculator == null) throw new IllegalArgumentException("taxCalculator required");
        return taxCalculator.tax(getSubtotal());
    }

    public BigDecimal getTotal(TaxCalculator taxCalculator){
         return getSubtotal().add(getTax(taxCalculator));
    }

    public void updateQty(MenuItem item, int qty){
        if(item == null) throw new IllegalArgumentException("item required");
        if(!lines.containsKey(item.getName())){
            throw new IllegalArgumentException("item not in cart");
        }
        lines.computeIfPresent(item.getName(), (k, line) -> {
            line.setQuantity(qty);
            return line;
        });
    }
}