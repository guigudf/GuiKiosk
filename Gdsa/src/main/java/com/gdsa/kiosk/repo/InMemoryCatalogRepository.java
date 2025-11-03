package com.gdsa.kiosk.repo;

import com.gdsa.kiosk.interfaces.CatalogRepository;
import com.gdsa.kiosk.model.MenuItem;
import java.math.BigDecimal;
import java.util.List;

public class InMemoryCatalogRepository implements CatalogRepository {
    @Override public List<MenuItem> all(){
        return List.of(
                new MenuItem("Coffee", new BigDecimal("3.00")),
                new MenuItem("Tea", new BigDecimal("2.50")),
                new MenuItem("Croissant", new BigDecimal("4.25"))
        );
    }
}