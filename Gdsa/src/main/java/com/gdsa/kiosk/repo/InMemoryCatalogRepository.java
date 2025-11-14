package com.gdsa.kiosk.repo;

import com.gdsa.kiosk.interfaces.CatalogRepository;
import com.gdsa.kiosk.model.*;
import java.math.BigDecimal;
import java.util.List;

public class InMemoryCatalogRepository implements CatalogRepository {
    private final List<MenuItem> data = List.of(
            new MenuItem("Americano", new BigDecimal("3.50"), Category.DRINK),
            new MenuItem("Latte", new BigDecimal("4.20"), Category.DRINK),
            new MenuItem("Croissant", new BigDecimal("4.25"), Category.FOOD),
            new MenuItem("Apple Pie", new BigDecimal("5.50"), Category.DESERT),
            new MenuItem("Cupcake", new BigDecimal("2.40"), Category.DESERT)
    );
    @Override public List<MenuItem> all(){ return data; }
    @Override public List<MenuItem> byCategory(Category c){ return data.stream().filter(mi -> mi.getCategory()==c).toList(); }
}