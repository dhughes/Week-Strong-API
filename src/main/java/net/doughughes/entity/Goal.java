package net.doughughes.entity;

import com.fasterxml.jackson.annotation.JsonView;
import net.doughughes.util.View;

public class Goal {
    @JsonView(View.Program.class)
    private Exercise exercise;

    @JsonView(View.Program.class)
    private int goal;

    public Goal(Exercise exercise, Integer goal) {
        this.exercise = exercise;
        this.goal = goal;
    }

    public Exercise getExercise() {
        return this.exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public int getGoal() {
        return this.goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }
}
