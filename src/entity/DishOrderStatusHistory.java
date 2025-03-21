package entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class DishOrderStatusHistory {
    private String id;
    private OrderStatus status;
    private LocalDateTime date;

    public DishOrderStatusHistory(String id, OrderStatus status, LocalDateTime date) {
        this.id = id;
        this.status = status;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DishOrderStatusHistory that = (DishOrderStatusHistory) o;
        return Objects.equals(id, that.id) && status == that.status && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, date);
    }

    @Override
    public String toString() {
        return "DishOrderStatusHistory{" +
                "id='" + id + '\'' +
                ", status=" + status +
                ", date=" + date +
                '}';
    }
}
