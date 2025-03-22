package entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class DishOrder {
    private String id;
    private Dish dish;
    private double quantity;
    private Order order;
    private List<DishOrderStatusHistory> statusHistory;

    public DishOrder(String id, Dish dish, double quantity, List<DishOrderStatusHistory> statusHistory) {
        this.id = id;
        this.dish = dish;
        this.quantity = quantity;
        this.statusHistory = statusHistory;
    }
    public DishOrder(String id, Dish dish, double quantity) {
        this.id = id;
        this.dish = dish;
        this.quantity = quantity;
        this.statusHistory = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public List<DishOrderStatusHistory> getStatusHistory() {
        return statusHistory;
    }

    public void setStatusHistory(List<DishOrderStatusHistory> statusHistory) {
        this.statusHistory = statusHistory;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public DishOrderStatusHistory getActualStatus() {
        return statusHistory.stream()
                .max(Comparator.comparing(DishOrderStatusHistory::getDate))
                .orElse(null);
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DishOrder dishOrder = (DishOrder) o;
        return Double.compare(quantity, dishOrder.quantity) == 0 && Objects.equals(id, dishOrder.id) && Objects.equals(dish, dishOrder.dish) && Objects.equals(statusHistory, dishOrder.statusHistory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dish, quantity, statusHistory);
    }

    @Override
    public String toString() {
        return "DishOrder{" +
                "id='" + id + '\'' +
                ", dish=" + dish +
                ", quantity=" + quantity +
                ", statusHistory=" + statusHistory +
                '}';
    }
}
