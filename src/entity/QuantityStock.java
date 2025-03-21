package entity;

import java.util.Objects;

public class QuantityStock {
    private double quantity;
    private Unity unity;

    public QuantityStock(double quantity, Unity unity) {
        this.quantity = quantity;
        this.unity = unity;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Unity getUnity() {
        return unity;
    }

    public void setUnity(Unity unity) {
        this.unity = unity;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        QuantityStock that = (QuantityStock) o;
        return Double.compare(quantity, that.quantity) == 0 && unity == that.unity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity, unity);
    }

    @Override
    public String toString() {
        return "QuantityStock{" +
                "quantity=" + quantity +
                ", unity=" + unity +
                '}';
    }
}
