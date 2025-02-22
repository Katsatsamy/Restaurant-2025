package entity;

import java.time.LocalDateTime;

public class Ingredient {
    private String id;
    private String name;
    private LocalDateTime updateDateTime;
    private final int unitPrice;
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

    public Unity getUnity() {
        return unity;
    }

    public int getProductCost(){
        return 1;
    }
}
