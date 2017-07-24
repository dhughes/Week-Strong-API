package net.doughughes.bean;


import java.time.LocalDate;

public class Event {

    // this is the date for this event
    private LocalDate date;

    // something is in program when it's on or after the program created date
    // and is before the final (completed) workout date
    private boolean inProgram;

    // indicates what type of action was taken: skipped, exercised, makeup exercised, repeated, restarted, etc
    // null  type used when we're not in the program
    private Type type = null;

    // the workout for this day, if any
    private Long workout = null;

    public Event(LocalDate date, boolean inProgram) {
        this.date = date;
        this.inProgram = inProgram;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isInProgram() {
        return this.inProgram;
    }

    public void setInProgram(boolean inProgram) {
        this.inProgram = inProgram;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Long getWorkout() {
        return this.workout;
    }

    public void setWorkout(Long workout) {
        this.workout = workout;
    }

    public static enum Type {
        TEST,
        SKIP,
        WORKOUT,
        REPEAT,
        MAKEUP,
        RESTORE,
        RESTART
    }
}
