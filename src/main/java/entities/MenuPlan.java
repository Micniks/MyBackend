package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQuery(name = "MenuPlan.deleteAllRows", query = "DELETE from MenuPlan")
public class MenuPlan implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "menuPlan", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<DayPlan> dayPlans = new ArrayList();

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "menuPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingredient> shoppingList = new ArrayList();

    
    /*
     * I have added this attribute for user friendliness, 
     * in order to easier manage between menu plans
    */
    private String menuName;

    public MenuPlan() {
    }

    public MenuPlan(int id, List<DayPlan> dayPlans, User user, List<Ingredient> shoppingList, String menuName) {
        this.id = id;
        this.dayPlans = dayPlans;
        this.user = user;
        this.shoppingList = shoppingList;
        this.menuName = menuName;
    }

    public MenuPlan(List<DayPlan> dayPlans, User user, List<Ingredient> shoppingList, String menuName) {
        this.dayPlans = dayPlans;
        this.user = user;
        this.shoppingList = shoppingList;
        this.menuName = menuName;
    }

    public MenuPlan(User user, String menuName) {
        this.user = user;
        this.menuName = menuName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public List<DayPlan> getDayPlans() {
        return dayPlans;
    }

    public void setDayPlans(List<DayPlan> dayPlans) {
        this.dayPlans = dayPlans;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Ingredient> getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(List<Ingredient> shoppingList) {
        this.shoppingList = shoppingList;
    }
    
}
