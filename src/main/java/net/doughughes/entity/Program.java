package net.doughughes.entity;

import net.doughughes.bean.Event;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Program {

    private Long id;
    private final List<Integer> selectedDays;
    private final Integer weeks;
    private List<Goal> goals;
    private LocalDate created;
    private Test test;
    private List<Workout> workouts = new ArrayList<>();


    public Program(List<Integer> selectedDays, Integer weeks, ArrayList<Goal> goals, LocalDate created) {
        this.selectedDays = selectedDays;
        this.weeks = weeks;
        this.goals = goals;
        this.created = created;
    }

    public Program(Long id, List<Integer> selectedDays, Integer weeks, ArrayList<Goal> goals, LocalDate created, Test test, List<Workout> workouts) {
        this.id = id;
        this.selectedDays = selectedDays;
        this.weeks = weeks;
        this.goals = goals;
        this.created = created;
        this.test = test;
        this.workouts = workouts;
    }

    public List<Event> getEventHistory() {
        List<Event> eventHistory = new ArrayList<>();

        // find the sunday of or before this program was created

        return eventHistory;
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

    public LocalDate getCreated() {
        return this.created;
    }

    public void setCreated(LocalDate created) {
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
