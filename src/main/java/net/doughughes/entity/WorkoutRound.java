package net.doughughes.entity;


public class WorkoutRound {
    private Long id;
    private int round;
    private int reps;
    private int exerciseId;

    public WorkoutRound(long id, int round, int reps, int exerciseId) {
        this.id = id;
        this.round = round;
        this.reps = reps;
        this.exerciseId = exerciseId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRound() {
        return this.round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getReps() {
        return this.reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getExerciseId() {
        return this.exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }
}
