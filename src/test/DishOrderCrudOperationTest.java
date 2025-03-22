package test;

import dao.DishOrderCudOperation;
import entity.DishOrder;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DishOrderCrudOperationTest {
    DishOrderCudOperation subject = new DishOrderCudOperation();

    @Test
    public void getAllDishOrder() {

    }

    @Test
    public void saveAll(){
        System.out.println(subject.saveAll(List.of(
                new DishOrder("6", subject.findDishById("1"), 2),
                new DishOrder("7", subject.findDishById("2"), 1)
        )));
    }
}
