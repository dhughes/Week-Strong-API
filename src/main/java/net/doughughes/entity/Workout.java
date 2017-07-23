package net.doughughes.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Workout {

    private Long id;
    private LocalDate date;
    private List<WorkoutRound> rounds = new ArrayList<>();

    public Workout(long id, LocalDate date) {
        this.id = id;
        this.date = date;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<WorkoutRound> getRounds() {
        return this.rounds;
    }

    public void setRounds(List<WorkoutRound> rounds) {
        this.rounds = rounds;
    }
}
