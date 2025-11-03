package gdsa.com.kiosk.interfaces;

import gdsa.com.kiosk.model.MenuItem;
import java.util.List;

public interface CatalogRepository {
    List<MenuItem> all();
}