package dto;

import entities.MenuPlan;
import java.util.List;

public class MenuPlanDTO {

    private int id;
    private String menuName;
    private DayPlansDTO dayPlans;
    private IngredientsDTO shoppingList;
    private String user;

    public MenuPlanDTO() {
    }

    public MenuPlanDTO(String menuName) {
        this.menuName = menuName;
    }

    public MenuPlanDTO(DayPlansDTO dayPlans, IngredientsDTO shoppingList, String menuName, String user) {
        this.dayPlans = dayPlans;
        this.shoppingList = shoppingList;
        this.menuName = menuName;
        this.user = user;
    }

    public MenuPlanDTO(MenuPlan menuPlan) {
        this.id = menuPlan.getId();
        this.dayPlans = DayPlansDTO.getDayPlansDTO(menuPlan.getDayPlans());
        this.shoppingList = IngredientsDTO.getIngredientsDTO(menuPlan.getShoppingList());
        this.menuName = menuPlan.getMenuName();
        this.user = menuPlan.getUser().getUserName();
    }

    public DayPlansDTO getDayPlans() {
        return dayPlans;
    }

    public void setDayPlans(DayPlansDTO dayPlans) {
        this.dayPlans = dayPlans;
    }

    public IngredientsDTO getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(IngredientsDTO shoppingList) {
        this.shoppingList = shoppingList;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
    
    

}
