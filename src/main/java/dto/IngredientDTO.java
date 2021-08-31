package dto;

import entities.Ingredient;

public class IngredientDTO {
    
    private int amount;
    private String name;

    public IngredientDTO() {
    }

    public IngredientDTO(int amount, String name) {
        this.amount = amount;
        this.name = name;
    }
    
    public IngredientDTO(Ingredient ingredient) {
        this.amount = ingredient.getAmount();
        this.name = ingredient.getName();
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
