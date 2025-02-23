package entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class IngredientPrice {
    private int unitPrice;
    private LocalDateTime date;

    public IngredientPrice(int unitPrice, LocalDateTime date) {
        this.unitPrice = unitPrice;
        this.date = date;
    }

    public int getUnit_price() {
        return unitPrice;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IngredientPrice that = (IngredientPrice) o;
        return unitPrice == that.unitPrice && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unitPrice, date);
    }

    @Override
    public String toString() {
        return "IngredientPrice{" +
                "unitPrice=" + unitPrice +
                ", date=" + date +
                '}';
    }
}
