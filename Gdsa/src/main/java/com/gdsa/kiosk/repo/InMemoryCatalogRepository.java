package com.gdsa.kiosk.repo;

import com.gdsa.kiosk.interfaces.CatalogRepository;
import com.gdsa.kiosk.model.*;
import java.math.BigDecimal;
import java.util.List;

public class InMemoryCatalogRepository implements CatalogRepository {
    private final List<MenuItem> data = List.of(
            new MenuItem("Americano", new BigDecimal("3.50"), Category.DRINK),
            new MenuItem("Latte", new BigDecimal("3.80"), Category.DRINK),
            new MenuItem("Orange Juice", new BigDecimal("5.50"), Category.DRINK),
            new MenuItem("Caipirinha", new BigDecimal("4.20"), Category.DRINK),

            new MenuItem("Croissant", new BigDecimal("4.25"), Category.BAKERY),
            new MenuItem("Pastel", new BigDecimal("16.00"), Category.BAKERY),
            new MenuItem("Coxinha", new BigDecimal("6.50"), Category.BAKERY),
            new MenuItem("Pizza", new BigDecimal("2.25"), Category.BAKERY),

            new MenuItem("Chips", new BigDecimal("5.50"), Category.SNACKS),
            new MenuItem("Cookies", new BigDecimal("2.40"), Category.SNACKS),
            new MenuItem("Brigadeiro", new BigDecimal("1.50"), Category.SNACKS),
            new MenuItem("Suspiro", new BigDecimal("0.75"), Category.SNACKS),
            new MenuItem("Granola Bar", new BigDecimal("2.99"), Category.SNACKS)
    );
    @Override public List<MenuItem> all(){ return data; }
    @Override public List<MenuItem> byCategory(Category c){ return data.stream().filter(mi -> mi.getCategory()==c).toList(); }
}