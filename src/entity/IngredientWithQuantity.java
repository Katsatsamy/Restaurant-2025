package entity;

import java.util.Objects;

public class IngredientWithQuantity {
    private Ingredient ingredient;
    private double required_quantity;
    private Unity unity;

    public IngredientWithQuantity(Ingredient ingredient, double requiredQuantity, Unity unity) {
        this.ingredient = ingredient;
        this.required_quantity = requiredQuantity;
        this.unity = unity;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public double getRequeredquantity() {
        return required_quantity;
    }

    public Unity getUnity() {
        return unity;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IngredientWithQuantity that = (IngredientWithQuantity) o;
        return required_quantity == that.required_quantity && Objects.equals(ingredient, that.ingredient) && unity == that.unity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredient, required_quantity, unity);
    }

    @Override
    public String toString() {
        return "IngredientWithQuantity{" +
                "ingredient=" + ingredient +
                ", requeredquantity=" + required_quantity +
                ", unity=" + unity +
                '}';
    }
}