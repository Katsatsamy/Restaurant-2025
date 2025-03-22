package test;

import dao.DishOrderCudOperation;
import dao.DishOrderStatusHistoryCrudOperation;
import entity.Dish;
import entity.DishOrderStatusHistory;
import entity.OrderStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class DishOrderStatusHistoryCrudOperationTest {
    DishOrderStatusHistoryCrudOperation subject = new DishOrderStatusHistoryCrudOperation();

    @Test
    public void get_all_dish_order_status_history() {
        List<DishOrderStatusHistory> expected = List.of(
                Order1()
        );

        List<DishOrderStatusHistory> actual = subject.getAll(1, 1);

        System.out.println(actual);
        System.out.println(expected);
        assertTrue(actual.equals(expected));
    }

    public DishOrderStatusHistory Order1(){
        return createDishOrderStatusHistory(1L, OrderStatus.CREE, LocalDateTime.of(2025, 02, 07, 10, 00, 00, 00));
    }

    public DishOrderStatusHistory createDishOrderStatusHistory(Long id, OrderStatus orderStatus, LocalDateTime date) {
        return new DishOrderStatusHistory(id, orderStatus, date);
    }
}
