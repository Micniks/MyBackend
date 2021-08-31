package entities;

import dto.IngredientDTO;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name = "Ingredient.deleteAllRows", query = "DELETE from Ingredient")
/*
 * As the assignment suggested, I have made an entity class for the ingredients
 * to easier save the shoppinglists of menu plans
 */
public class Ingredient implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /* 
     * If I were to speak with the product owner, I would recommend 
     * adding measurement units to the external api for user friendliness
    */
    private int amount;
    private String name;
    @ManyToOne
    private MenuPlan menuPlan;

    public Ingredient(int amount, String name) {
        this.amount = amount;
        this.name = name;
    }

    public Ingredient(IngredientDTO ingredient, int numberOfServings) {
        this.amount = ingredient.getAmount() * numberOfServings;
        this.name = ingredient.getName();
    }

    public Ingredient(IngredientDTO ingredient) {
        this.amount = ingredient.getAmount();
        this.name = ingredient.getName();
    }

    public Ingredient(int id, int amount, String name) {
        this.id = id;
        this.amount = amount;
        this.name = name;
    }

    public Ingredient() {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MenuPlan getMenuPlan() {
        return menuPlan;
    }

    public void setMenuPlan(MenuPlan menuPlan) {
        this.menuPlan = menuPlan;
    }

}
