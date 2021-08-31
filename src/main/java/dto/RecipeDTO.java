package dto;

public class RecipeDTO {
    
    private String category;
    private String name;
    private int preparation_time;
    private int id;
    private String[] directions;
    private IngredientDTO[] ingredient_list;

    public RecipeDTO() {
    }

    public RecipeDTO(String category, String name, int preparation_time, int id, String[] directions, IngredientDTO[] ingredient_list) {
        this.category = category;
        this.name = name;
        this.preparation_time = preparation_time;
        this.id = id;
        this.directions = directions;
        this.ingredient_list = ingredient_list;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPreparation_time() {
        return preparation_time;
    }

    public void setPreparation_time(int preparation_time) {
        this.preparation_time = preparation_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String[] getDirections() {
        return directions;
    }

    public void setDirections(String[] directions) {
        this.directions = directions;
    }

    public IngredientDTO[] getIngredient_list() {
        return ingredient_list;
    }

    public void setIngredient_list(IngredientDTO[] ingredient_list) {
        this.ingredient_list = ingredient_list;
    }
    
    
    
}
