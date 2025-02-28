package test;

import dao.IngredientCrudOperation;
import entity.*;
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
                HuileIngredient(),
                OeufIngredient(),
                PainIngredient()
        );

        List<Ingredient> actual = subject.getAll(1, 4);

        assertTrue(actual.containsAll(expected));
    }

    @Test

    public void read_one_ingredient_by_id(){
        Ingredient expected = SaucisseIngredient();

        Ingredient actual = subject.findById("1");

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

    @Test
    public  void get_available_quantity_with_date(){
        Ingredient ingredient = PainIngredient();
        QuantityStock expected = PainStock();

        QuantityStock actual = ingredient.getAvalaibleQuantity(LocalDateTime.of(2025,02,24,8,0,0));

        assertTrue(expected.equals(actual));
    }

    @Test
    public  void get_available_quantity_without_date(){
        Ingredient ingredient = OeufIngredient();
        QuantityStock expected = OeufStock();

        QuantityStock actual = ingredient.getAvalaibleQuantity();

        assertTrue(expected.equals(actual));
    }

    @Test
    public void find_by_criteria(){
        List<Criteria> criteria = List.of(
                new Criteria("unity", Operator.EQUAL, "U", Connector.AND),
                new Criteria("name", Order.ASC),
                new Criteria("name", Operator.EQUAL, "oeu", Connector.AND)
        );
        List<Ingredient> expected = List.of(
                OeufIngredient()
        );

        List<Ingredient> actual = subject.findByCriteria(criteria);

        assertTrue(actual.containsAll(expected));
    }

    public Ingredient SaucisseIngredient(){
        return createIngredient("1","Saucisse", LocalDateTime.of(2025,01,01,0,0),20, Unity.G);
    }

    public Ingredient HuileIngredient(){
        return createIngredient("2","Huile", LocalDateTime.of(2025,01,01,0,0),10000, Unity.L);
    }

    public Ingredient OeufIngredient(){
        return createIngredient("3","Oeuf", LocalDateTime.of(2025,01,01,0,0),1000, Unity.U);
    }

    public Ingredient PainIngredient(){
        return createIngredient("4","Pain", LocalDateTime.of(2025,01,01,0,0),1000, Unity.U);
    }

    public Ingredient createIngredient(String id, String name, LocalDateTime updateDatetime, int unitPrice, Unity unity) {
        Ingredient ingredient =  new Ingredient(id,name,updateDatetime,unitPrice,unity);
        return ingredient;
    }

    public QuantityStock SaucisseStock(){
        return createQuantityStock(10000.0, Unity.G);
    }

    public QuantityStock HuileStock(){
        return createQuantityStock(20.0, Unity.L);
    }

    public QuantityStock OeufStock(){
        return createQuantityStock(80.0, Unity.U);
    }

    public QuantityStock PainStock(){
        return createQuantityStock(30.0, Unity.U);
    }

    public QuantityStock createQuantityStock(Double quantity, Unity unity) {
        return new QuantityStock(quantity,unity);
    }
}
