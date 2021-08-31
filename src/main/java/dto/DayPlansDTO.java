package dto;

import entities.DayPlan;
import java.util.ArrayList;
import java.util.List;

public class DayPlansDTO {
    
    private List<DayPlanDTO> dayPlans = new ArrayList();

    public DayPlansDTO() {
    }
    
    public DayPlansDTO(List<DayPlanDTO> dayPlans) {
        this.dayPlans = dayPlans;
    }

    public List<DayPlanDTO> getDayPlans() {
        return dayPlans;
    }

    public void setDayPlans(List<DayPlanDTO> dayPlans) {
        this.dayPlans = dayPlans;
    }
    
    public static DayPlansDTO getDayPlansDTO(List<DayPlan> dayPlans) {
        DayPlansDTO instance = new DayPlansDTO();
        for (DayPlan dayPlan : dayPlans) {
            instance.getDayPlans().add(new DayPlanDTO(dayPlan));
        }
        return instance;
    }
    
}
