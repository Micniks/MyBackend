package dto;

import entities.Ingredient;
import java.util.ArrayList;
import java.util.List;

public class IngredientsDTO {

    private List<IngredientDTO> ingredients = new ArrayList();

    public IngredientsDTO(List<IngredientDTO> ingredients) {
        this.ingredients = ingredients;
    }
    
    public static IngredientsDTO getIngredientsDTO(List<Ingredient> ingredients) {
        IngredientsDTO instance = new IngredientsDTO();
        for (Ingredient ingredient : ingredients) {
            instance.getIngredients().add(new IngredientDTO(ingredient));
        }
        return instance;
    }

    public IngredientsDTO() {
    }

    public List<IngredientDTO> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientDTO> ingredients) {
        this.ingredients = ingredients;
    }

}
