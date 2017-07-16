package net.doughughes.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User {

    private Long id;
    private String name;
    private String email;
    @JsonIgnore
    private String password;
    // one to one user to program (though a user could have multiple programs over time)
    // maybe this will end up being a list on the server side?
    private Program program = null;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(long id, String name, String email, Program program) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.program = program;
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

    public Program getProgram() {
        return this.program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    @JsonIgnore
    public String getPassword() {
        return this.password;
    }
}
