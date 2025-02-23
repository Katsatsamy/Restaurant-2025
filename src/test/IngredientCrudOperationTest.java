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
        List<Ingredient> expected = List.of(
                SaucisseIngredient(),
                HuileIngredient()
        );

        List<Ingredient> actual = subject.getAll(1, 2);

        assertTrue(actual.containsAll(expected));
    }

    @Test
    public void read_one_ingredient_by_id(){
        Ingredient expected = SaucisseIngredient();

        Ingredient actual = subject.findById("1");

        System.out.println(expected);
        System.out.println(actual);

        assertTrue(expected.equals(actual));
    }

    @Test
    public void read_one_ingredient_by_name(){
        Ingredient expected = SaucisseIngredient();

        List<Ingredient> actual = subject.findByName("Saucisse");

        assertTrue(actual.contains(expected));
    }

    @Test
    public void create_or_update_ingredient(){
        List<Ingredient> expected = List.of(
                createIngredient("1", "Saucisse", LocalDateTime.of(2025,01,01,0,0),20, Unity.G),
                createIngredient("2","Huile", LocalDateTime.of(2025,01,01,0,0),10000, Unity.L)
        );

        List<Ingredient> actual = subject.saveAll(expected);

        assertTrue(actual.containsAll(expected));
    }

    public Ingredient SaucisseIngredient(){
        return createIngredient("1","Saucisse", LocalDateTime.of(2025,01,01,0,0),20, Unity.G);
    }

    public Ingredient HuileIngredient(){
        return createIngredient("2","Huile", LocalDateTime.of(2025,01,01,0,0),10000, Unity.L);
    }

    public Ingredient createIngredient(String id, String name, LocalDateTime updateDatetime, int unitPrice, Unity unity) {
        Ingredient ingredient =  new Ingredient(id,name,updateDatetime,unitPrice,unity);
        return ingredient;
    }
}
