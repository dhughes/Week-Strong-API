package net.doughughes.bean;


import java.time.LocalDate;

public class Event {

    // this is the date for this event
    private LocalDate date;

    // the numeric day of the program (1 to 18 for a six week program)
    private Integer day = null;

    // indicates the state of the program on this day.
    private WorkoutState workoutState = null;

    // indicates what type of workout was completed
    private WorkoutType workoutType = null;

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public WorkoutState getWorkoutState() {
        return this.workoutState;
    }

    public void setWorkoutState(WorkoutState workoutState) {
        this.workoutState = workoutState;
    }

    public WorkoutType getWorkoutType() {
        return this.workoutType;
    }

    public void setWorkoutType(WorkoutType workoutType) {
        this.workoutType = workoutType;
    }

    public Integer getDay() {
        return this.day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }
}
