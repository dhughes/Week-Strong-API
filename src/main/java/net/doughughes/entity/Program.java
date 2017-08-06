package net.doughughes.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import net.doughughes.bean.ProgramState;
import net.doughughes.util.View;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class Program {

    @JsonView(View.Program.class)
    private Long id;

    @JsonView(View.Program.class)
    private final List<Integer> selectedDays;

    @JsonView(View.Program.class)
    private final Integer weeks;

    @JsonView(View.Program.class)
    private List<Goal> goals;

    @JsonView(View.Program.class)
    private LocalDate created;

    // todo: implement this
    @JsonView(View.Program.class)
    private ProgramState state = ProgramState.REST;

    public Program(List<Integer> selectedDays, Integer weeks, ArrayList<Goal> goals, LocalDate created) {
        this.selectedDays = selectedDays;
        this.weeks = weeks;
        this.goals = goals;
        this.created = created;
    }

    public Program(Long id, List<Integer> selectedDays, Integer weeks, ArrayList<Goal> goals, LocalDate created) {
        this.id = id;
        this.selectedDays = selectedDays;
        this.weeks = weeks;
        this.goals = goals;
        this.created = created;
    }

    @JsonView(View.Program.class)
    public LocalDate getNextWorkoutDate() {
        return getNextWorkoutDates().get(0);
    }

    @JsonIgnore
    public List<LocalDate> getNextWorkoutDates() {
        return getSelectedDays().stream()
                // convert the selected days to actual dates of the next workout
                .map(day -> LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.of(day))))
                // sort the dates
                .sorted(Comparator.naturalOrder())
                // collect into a list
                .collect(Collectors.toList());
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

}

