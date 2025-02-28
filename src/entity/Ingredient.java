package entity;

import dao.IngredientCrudOperation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Ingredient {
    private String id;
    private String name;
    private LocalDateTime updateDateTime;
    private Double unitPrice;
    private Unity unity;

    public Ingredient(String id, String name, LocalDateTime updateDateTime, Double unitPrice, Unity unity) {
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

    public Double getUnitPrice() {
        return unitPrice;
    }

    public Double getUnitPrice(LocalDateTime date) {
        IngredientCrudOperation ingredientCrudOperation = new IngredientCrudOperation();
        IngredientPrice price =  ingredientCrudOperation.getIngredientPrice(id, date);
        if (price == null) {
            return 0.0;
        }
        return price.getUnit_price();
    }

    public Unity getUnity() {
        return unity;
    }

    public QuantityStock getAvalaibleQuantity() {
        IngredientCrudOperation ingredientCrudOperation = new IngredientCrudOperation();
        List<Stock> stocks = ingredientCrudOperation.getAvailableStocks(id);
        double quantity = 0;
        for (Stock stock : stocks) {
            if(stock.getMovement() == Movement.EXIT){
                quantity -= stock.getQuantity();
            }else {
                quantity += stock.getQuantity();
            }
        }
        return new QuantityStock(quantity, unity);
    }

    public QuantityStock getAvalaibleQuantity(LocalDateTime date) {
        IngredientCrudOperation ingredientCrudOperation = new IngredientCrudOperation();
        List<Stock> stocks = ingredientCrudOperation.getAvailableStocks(id, date);
        double quantity = 0;
        for (Stock stock : stocks) {
            if(stock.getMovement() == Movement.EXIT){
                quantity -= stock.getQuantity();
            }else {
                quantity += stock.getQuantity();
            }
        }
        return new QuantityStock(quantity, unity);
    }

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
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(updateDateTime, that.updateDateTime) && Objects.equals(unitPrice, that.unitPrice) && unity == that.unity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, updateDateTime, unitPrice, unity);
    }
}
