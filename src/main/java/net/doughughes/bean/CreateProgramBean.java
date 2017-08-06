package net.doughughes.bean;

import java.util.List;
import java.util.Map;

public class CreateProgramBean {
    // maps exercise id to goal
    private Map<Integer, Integer> exercises;

    // list of days (0 = sunday) to exercise
    private List<Integer> selectedDays;

    // number of weeks in the program
    private Integer weeks;

    // the id of the owning user
    private Integer userId;

    public Map<Integer, Integer> getExercises() {
        return this.exercises;
    }

    public void setExercises(Map<Integer, Integer> exercises) {
        this.exercises = exercises;
    }

    public List<Integer> getSelectedDays() {
        return this.selectedDays;
    }

    public void setSelectedDays(List<Integer> selectedDays) {
        this.selectedDays = selectedDays;
    }

    public Integer getWeeks() {
        return this.weeks;
    }

    public void setWeeks(Integer weeks) {
        this.weeks = weeks;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
