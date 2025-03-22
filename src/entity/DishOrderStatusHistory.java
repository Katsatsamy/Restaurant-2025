package entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class DishOrderStatusHistory {
    private Long id;
    private OrderStatus status;
    private LocalDateTime date;

    public DishOrderStatusHistory(Long id, OrderStatus status, LocalDateTime date) {
        this.id = id;
        this.status = status;
        this.date = date;
    }

    public DishOrderStatusHistory() {
        this.id = null;
        this.status = null;
        this.date = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
