package com.gdsa.kiosk.interfaces;

import com.gdsa.kiosk.model.MenuItem;
import com.gdsa.kiosk.model.Category;
import java.util.List;

public interface CatalogRepository {
    List<MenuItem> all();
    List<MenuItem> byCategory(Category category);
}