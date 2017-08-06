package net.doughughes.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;

public class User {

    private Long id;
    private String name;
    private String email;
    private LocalDate created;
    @JsonIgnore
    private String password;


    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.created = LocalDate.now();
    }

    public User(long id, String name, String email, LocalDate created) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.created = created;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getCreated() {
        return this.created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    @JsonIgnore
    public String getPassword() {
        return this.password;
    }
}
