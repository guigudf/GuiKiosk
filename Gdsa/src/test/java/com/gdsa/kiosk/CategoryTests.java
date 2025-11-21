package com.gdsa.kiosk;
import com.gdsa.kiosk.model.*;
import com.gdsa.kiosk.repo.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class CategoryTests {
    @Test void filters_by_category_counts(){
        var repo = new InMemoryCatalogRepository();
        assertEquals(4, repo.byCategory(Category.DRINK).size());
        assertEquals(5, repo.byCategory(Category.SNACKS).size());
        assertEquals(4, repo.byCategory(Category.BAKERY).size());
    }
}