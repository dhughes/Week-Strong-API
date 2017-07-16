package net.doughughes.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Program {

    private Long id;
    private final List<Integer> selectedDays;
    private final Integer weeks;
    private List<Goal> goals;
    private Date created;
    private Test test;
    private List<Workout> workouts = new ArrayList<>();


    public Program(List<Integer> selectedDays, Integer weeks, ArrayList<Goal> goals, Date created) {
        this.selectedDays = selectedDays;
        this.weeks = weeks;
        this.goals = goals;
        this.created = created;
    }

    public Program(Long id, List<Integer> selectedDays, Integer weeks, ArrayList<Goal> goals, Date created, Test test, List<Workout> workouts) {
        this.id = id;
        this.selectedDays = selectedDays;
        this.weeks = weeks;
        this.goals = goals;
        this.created = created;
        this.test = test;
        this.workouts = workouts;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Integer> getSelectedDays() {
        return this.selectedDays;
    }

    public Integer getWeeks() {
        return this.weeks;
    }

    public List<Goal> getGoals() {
        return this.goals;
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Test getTest() {
        return this.test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public List<Workout> getWorkouts() {
        return this.workouts;
    }

    public void setWorkouts(List<Workout> workouts) {
        this.workouts = workouts;
    }
}
