package test;

import dao.DishCrudOperation;
import entity.Dish;
import entity.Ingredient;
import entity.IngredientWithQuantity;
import entity.Unity;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DishCrudOperationTest {
    DishCrudOperation subject = new DishCrudOperation();

    @Test
    public void get_all_ingredients_in_dish() {
        List<IngredientWithQuantity> expected = List.of(
                SaucisseIngredientWithQuantity(),
                HuileIngredientWithQuantity(),
                OuefIngredientWithQuantity(),
                PainIngredientWithQuantity()
        );

        List<IngredientWithQuantity> actual = subject.getIngredientWithQuantity("1");

        assertTrue(actual.containsAll(expected));
    }

    @Test
    public void get_price_total_ingredients_in_dish_without_date() {
        double expected = 5500;
        Dish dish = subject.findById("1");

        double actual = dish.getIngredientsPriceTotal();

        assertEquals(expected, actual);
    }

    @Test void get_price_total_ingredient_in_dish_with_date(){
        double expected = 0;
        Dish dish = subject.findById("1");

        double actual = dish.getIngredientsPriceTotal(LocalDateTime.of(1999,01,01,0,0,0));

        assertEquals(expected, actual);

    }

    @Test
    public void get_gross_margin_test_without_date(){
        double expected = 9500;
        Dish dish = subject.findById("1");

        double actual = dish.getGrossMargin();

        assertEquals(expected, actual);;
    }

    @Test
    public void get_gross_margin_test_with_date(){
        double expected = 9500;
        Dish dish = subject.findById("1");

        double actual = dish.getGrossMargin(LocalDateTime.of(2026,01,01,0,0,0));

        assertEquals(expected, actual);;
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

    public IngredientWithQuantity SaucisseIngredientWithQuantity(){
        return createIngredientWithQuantity(SaucisseIngredient(), 100, Unity.G);
    }

    public IngredientWithQuantity HuileIngredientWithQuantity(){
        return createIngredientWithQuantity(HuileIngredient(), 0.15, Unity.L);
    }

    public IngredientWithQuantity OuefIngredientWithQuantity(){
        return createIngredientWithQuantity(OeufIngredient(), 1, Unity.U);
    }

    public IngredientWithQuantity PainIngredientWithQuantity(){
        return createIngredientWithQuantity(PainIngredient(), 1, Unity.U);
    }

    public Ingredient createIngredient(String id, String name, LocalDateTime updateDatetime, int unitPrice, Unity unity) {
        Ingredient ingredient =  new Ingredient(id,name,updateDatetime,unitPrice,unity);
        return ingredient;
    }

    public IngredientWithQuantity createIngredientWithQuantity(Ingredient ingredient, double requiredQuantity, Unity unity) {
        IngredientWithQuantity ingredientWithQuantity = new IngredientWithQuantity(ingredient,requiredQuantity,unity);
        return ingredientWithQuantity;
    }
}
