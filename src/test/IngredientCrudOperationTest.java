package test;

import dao.IngredientCrudOperation;
import entity.Ingredient;
import entity.Unity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IngredientCrudOperationTest {
    IngredientCrudOperation subject = new IngredientCrudOperation();

    @Test
    public void read_all_ingredients(){
        Ingredient expected = SaucisseIngredient();

        List<Ingredient> actual = subject.getAll(1, 1);

        assertTrue(actual.contains(expected));
    }

    public Ingredient SaucisseIngredient(){
        return createIngredient("1","Saucisse", LocalDateTime.of(2025,01,01,0,0),20, Unity.G);
    }

    public Ingredient createIngredient(String id, String name, LocalDateTime updateDatetime, int unitPrice, Unity unity) {
        Ingredient ingredient =  new Ingredient(id,name,updateDatetime,unitPrice,unity);
        return ingredient;
    }
}
