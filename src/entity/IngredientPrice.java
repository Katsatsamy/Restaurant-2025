package entity;

import java.time.LocalDateTime;

public class IngredientPrice {
    private int unit_price;
    private LocalDateTime date;

    public IngredientPrice(int unit_price, LocalDateTime date) {
        this.unit_price = unit_price;
        this.date = date;
    }

    public int getUnit_price() {
        return unit_price;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
