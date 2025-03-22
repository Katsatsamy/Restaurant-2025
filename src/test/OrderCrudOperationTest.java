package test;

import dao.DishCrudOperation;
import dao.DishOrderCudOperation;
import dao.OrderCrudOperation;
import entity.Dish;
import entity.DishOrder;
import entity.Order;
import entity.OrderStatus;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class OrderCrudOperationTest {
    OrderCrudOperation subject = new OrderCrudOperation();

    @Test
    public void getTotalAmount(){
        Order order = subject.findById("1");
        order.setDishOrders(subject.findDishOrderByOrderId(order.getId()));

        Double actual = order.getTotalAmount();
        assertTrue(actual.equals(30000.0));
    }

    @Test
    public void getActualStatus(){
        Order order = subject.findById("3");
        order.setDishOrders(subject.findDishOrderByOrderId(order.getId()));

        OrderStatus actual = order.getActualStatus();

        System.out.println(actual);
    }

    @Test
    public void addOrderDish(){
        DishCrudOperation dishDAO = new DishCrudOperation();
        Order order = new Order("5");
        order.addOrderDish(List.of(
                new DishOrder("6", dishDAO.findById("1"), 2),
                new DishOrder("7", dishDAO.findById("1"), 1)
        ));
    }
}
