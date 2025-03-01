package test;

import dao.StockCrudOperation;
import entity.Ingredient;
import entity.Movement;
import entity.Stock;
import entity.Unity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StockCrudOperationTest {
    StockCrudOperation subject = new StockCrudOperation();

    @Test
    public void save_all_stock_test() {
        List<Stock> stocks = List.of(
                new Stock("20", Movement.ENTER, 500.0, Unity.G, LocalDateTime.now(), SelIngredient()),
                new Stock("21", Movement.ENTER, 1000.0, Unity.G, LocalDateTime.now(),RizIngredient() ),
                new Stock("22", Movement.EXIT, 200.0, Unity.G, LocalDateTime.now(), SelIngredient())
        );
        List<Stock> actual = subject.saveAll(stocks);

        assertNotNull(actual);
        assertEquals(3, actual.size());


        List<Stock> conflictingStocks = List.of(
                new Stock("20", Movement.EXIT, 100.0, Unity.G, LocalDateTime.now(), SelIngredient()),
                new Stock("23", Movement.ENTER, 400.0, Unity.G, LocalDateTime.now(), SelIngredient())
        );

        List<Stock> savedConflictingStocks = subject.saveAll(conflictingStocks);

        assertNotNull(savedConflictingStocks);
        assertEquals(1, savedConflictingStocks.size());
    }

    public Ingredient RizIngredient(){
        return createIngredient("6", "riz", LocalDateTime.of(2025,02,28,0,0),3.5, Unity.G);

    }

    public Ingredient SelIngredient(){
        return createIngredient("5", "sel", LocalDateTime.of(2025,02,28,0,0),2.5, Unity.G);
    }

    public Ingredient createIngredient(String id, String name, LocalDateTime updateDatetime, Double unitPrice, Unity unity) {
        Ingredient ingredient =  new Ingredient(id,name,updateDatetime,unitPrice,unity);
        return ingredient;
    }
}
