package entity;

import dao.IngredientCrudOperation;

import java.time.LocalDateTime;
import java.util.Objects;

public class Ingredient {
    private String id;
    private String name;
    private LocalDateTime updateDateTime;
    private int unitPrice;
    private Unity unity;

    public Ingredient(String id, String name, LocalDateTime updateDateTime, int unitPrice, Unity unity) {
        this.id = id;
        this.name = name;
        this.updateDateTime = updateDateTime;
        this.unitPrice = unitPrice;
        this.unity = unity;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getUpdateDateTime() {
        return updateDateTime;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public int getUnitPrice(LocalDateTime date) {
        IngredientCrudOperation ingredientCrudOperation = new IngredientCrudOperation();
        IngredientPrice price =  ingredientCrudOperation.getIngredientPrice(id, date);
        if (price == null) {
            return 0;
        }
        return price.getUnit_price();
    }

    public Unity getUnity() {
        return unity;
    }

    public

    @Override
    public String toString() {
        return "Ingredient{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", updateDateTime=" + updateDateTime +
                ", unitPrice=" + unitPrice +
                ", unity=" + unity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return unitPrice == that.unitPrice && Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(updateDateTime, that.updateDateTime) && unity == that.unity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, updateDateTime, unitPrice, unity);
    }
}
