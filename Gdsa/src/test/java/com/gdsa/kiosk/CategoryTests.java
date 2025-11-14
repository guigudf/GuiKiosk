package com.gdsa.kiosk;
import com.gdsa.kiosk.model.*;
import com.gdsa.kiosk.repo.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class CategoryTests {
    @Test void filters_by_category_counts(){
        var repo = new InMemoryCatalogRepository();
        assertEquals(2, repo.byCategory(Category.DRINK).size());
        assertEquals(2, repo.byCategory(Category.DESERT).size());
        assertEquals(1, repo.byCategory(Category.FOOD).size());
    }
}