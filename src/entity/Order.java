package entity;

import dao.DishOrderCudOperation;
import dao.OrderCrudOperation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Order {
    private String id;
    private OrderStatus status;
    private LocalDateTime date;
    private List<DishOrder> dishOrders;

    public Order(String id) {
        this.id = id;
        this.status = OrderStatus.CREE;
        this.date = LocalDateTime.now();
        this.dishOrders = new ArrayList<>();
    }

    public Order(String id, List<DishOrder> dishOrders) {
        this.id = id;
        this.status = OrderStatus.CREE;
        this.date = LocalDateTime.now();
        this.dishOrders = dishOrders;
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

    public List<DishOrder> getDishOrders() {
        return dishOrders;
    }

    public void setDishOrders(List<DishOrder> dishOrders) {
        this.dishOrders = dishOrders;
    }

    public OrderStatus getActualStatus() {
        return dishOrders.stream()
                .map(DishOrder::getActualStatus)
                .filter(Objects::nonNull)
                .map(DishOrderStatusHistory::getStatus)
                .max(Comparator.comparingInt(OrderStatus::ordinal))
                .orElse(this.status);
    }

    public Double getTotalAmount(){
        double totalAmount = 0;
        for(DishOrder dishOrder : dishOrders){
            totalAmount += dishOrder.getQuantity() * dishOrder.getDish().getUnitPrice();
        }
        return totalAmount;
    }

    public Order addOrderDish(List<DishOrder> dishOrders) {
        OrderCrudOperation orderDAO = new OrderCrudOperation();
        DishOrderCudOperation dishOrderDAO = new DishOrderCudOperation();

        orderDAO.addOrder(this);
        dishOrders.forEach(d -> d.setOrder(this));
        dishOrderDAO.saveAll(dishOrders);

        return orderDAO.findById(this.id);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && status == order.status && Objects.equals(date, order.date) && Objects.equals(dishOrders, order.dishOrders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, date, dishOrders);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", status=" + status +
                ", date=" + date +
                ", dishOrders=" + dishOrders +
                '}';
    }
}
