package entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name = "DayPlan.deleteAllRows", query = "DELETE from DayPlan")
public class DayPlan implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int numberOfServings;
    
    //As a interpertation choice from the domain model, I save the recipe in the form of ID and name
    private int recipeID;
    private String recipeName;
    
    @ManyToOne
    private MenuPlan menuPlan;

    public DayPlan(int recipeID, String recipeName, int numberOfServings) {
        this.recipeID = recipeID;
        this.recipeName = recipeName;
        this.numberOfServings = numberOfServings;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public DayPlan(int id, int recipeID, String recipeName, int numberOfServings) {
        this.id = id;
        this.recipeID = recipeID;
        this.recipeName = recipeName;
        this.numberOfServings = numberOfServings;
    }

    public DayPlan() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }

    public MenuPlan getMenuPlan() {
        return menuPlan;
    }

    public void setMenuPlan(MenuPlan menuPlan) {
        this.menuPlan = menuPlan;
    }

    public int getNumberOfServings() {
        return numberOfServings;
    }

    public void setNumberOfServings(int numberOfServings) {
        this.numberOfServings = numberOfServings;
    }

}
