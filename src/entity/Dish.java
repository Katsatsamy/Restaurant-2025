package entity;

import java.util.List;
import java.util.Objects;

public class Dish {
    private String id;
    private String name;
    private final int unitPrice;
    private List<IngredientWithQuantity> ingredients;

    public Dish(int unitPrice, String id, String name, List<IngredientWithQuantity> ingredients) {
        this.unitPrice = unitPrice;
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public List<IngredientWithQuantity> getIngredients() {
        return ingredients;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return unitPrice == dish.unitPrice && Objects.equals(id, dish.id) && Objects.equals(name, dish.name) && Objects.equals(ingredients, dish.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, unitPrice, ingredients);
    }

    @Override
    public String
    toString() {
        return "Dish{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", unitPrice=" + unitPrice +
                ", ingredients=" + ingredients +
                '}';
    }
}
