package net.doughughes.entity;

public class Goal {
    private Exercise exercise;
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
