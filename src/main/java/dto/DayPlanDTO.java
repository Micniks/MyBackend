package dto;

import entities.DayPlan;

public class DayPlanDTO {

    private int recipeID;
    private String recipeName;
    private int NumberOfServings;

    public DayPlanDTO() {
    }

    public DayPlanDTO(int recipe, String recipeName, int NumberOfServings) {
        this.recipeID = recipe;
        this.recipeName = recipeName;
        this.NumberOfServings = NumberOfServings;
    }

    public DayPlanDTO(DayPlan dayPlan) {
        this.recipeID = dayPlan.getRecipeID();
        this.recipeName = dayPlan.getRecipeName();
        this.NumberOfServings = dayPlan.getNumberOfServings();
    }

    public int getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }

    public int getNumberOfServings() {
        return NumberOfServings;
    }

    public void setNumberOfServings(int NumberOfServings) {
        this.NumberOfServings = NumberOfServings;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

}
