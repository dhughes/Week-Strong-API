package net.doughughes.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Test {
    private Long id;
    private LocalDate date;
    private List<TestRound> rounds = new ArrayList<>();

    public Test(long id, LocalDate date) {
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

    public List<TestRound> getRounds() {
        return this.rounds;
    }

    public void setRounds(List<TestRound> rounds) {
        this.rounds = rounds;
    }
}
