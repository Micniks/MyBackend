package dto;

import entities.MenuPlan;
import java.util.ArrayList;
import java.util.List;

public class MenuPlansDTO {

    private List<MenuPlanDTO> menuPlans = new ArrayList();

    public static MenuPlansDTO getMenuPlansDTO(List<MenuPlan> menuPlans) {
        MenuPlansDTO instance = new MenuPlansDTO();
        for (MenuPlan menuPlan : menuPlans) {
            instance.getMenuPlans().add(new MenuPlanDTO(menuPlan));
        }
        return instance;
    }

    public List<MenuPlanDTO> getMenuPlans() {
        return menuPlans;
    }

    public void setMenuPlans(List<MenuPlanDTO> menuPlans) {
        this.menuPlans = menuPlans;
    }

}
