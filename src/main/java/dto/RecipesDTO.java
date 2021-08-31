package dto;

import java.util.ArrayList;
import java.util.List;

public class RecipesDTO {
    
    private List<RecipeDTO> recipes = new ArrayList();

    public RecipesDTO(List<RecipeDTO> recipes) {
        this.recipes = recipes;
    }

    public RecipesDTO() {
    }

    public List<RecipeDTO> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<RecipeDTO> recipes) {
        this.recipes = recipes;
    }
}
