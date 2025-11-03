package gdsa.com.kiosk.model;
import java.math.BigDecimal;
import java.util.Objects;

public class MenuItem {
    private final String name;
    private final BigDecimal price;


    public MenuItem(String name, BigDecimal price) {
        if(name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is not valid");
        }

        if(price == null || price.signum() <=0) {
            throw new IllegalArgumentException("Price is not valid");
        }

        this.name = name;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public BigDecimal getPrice() {
        return  this.price;
    }

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

        if(this.name.equals(m.name) && this.price.equals(m.price)) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.price);
    }

}
