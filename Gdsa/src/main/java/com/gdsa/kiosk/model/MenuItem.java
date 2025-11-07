package com.gdsa.kiosk.model;
import java.math.BigDecimal;
import java.util.Objects;

public class MenuItem {
    private final String name;
    private final BigDecimal price;
    private final Category category;

    public MenuItem(String name, BigDecimal price, Category category) {
        if(name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is not valid");
        }

        if(price == null || price.signum() <=0) {
            throw new IllegalArgumentException("Price is not valid");
        }

        if (category == null) throw new IllegalArgumentException("category required");

        this.name = name;
        this.price = price;
        this.category = category;

    }

    public String getName() {

        return this.name;
    }

    public BigDecimal getPrice() {

        return  this.price;
    }

    public Category getCategory() {
        return category; }


    @Override
    public String toString() {
        return  String.format("Name: " + this.name + " Price: " + this.price);
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) return true;

        if(!(o instanceof MenuItem m)) {
            return false;
        }

        return this.name.equals(m.name) && this.price.equals(m.price);
    }

    @Override
    public int hashCode(){ return Objects.hash(name,price,category); }

}
