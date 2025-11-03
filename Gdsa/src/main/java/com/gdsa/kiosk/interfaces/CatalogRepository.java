package com.gdsa.kiosk.interfaces;

import com.gdsa.kiosk.model.MenuItem;
import java.util.List;

public interface CatalogRepository {
    List<MenuItem> all();
}