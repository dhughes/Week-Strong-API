package net.doughughes.controller;

import com.fasterxml.jackson.annotation.JsonView;
import net.doughughes.bean.CreateProgramBean;
import net.doughughes.bean.CreateUserBean;
import net.doughughes.bean.Event;
import net.doughughes.entity.Exercise;
import net.doughughes.entity.Goal;
import net.doughughes.entity.Program;
import net.doughughes.entity.User;
import net.doughughes.repositories.ExerciseRepository;
import net.doughughes.repositories.ProgramRepository;
import net.doughughes.repositories.UserRepository;
import net.doughughes.service.WeekStrongService;
import net.doughughes.util.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
public class ApiController {

    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;
    private final ProgramRepository programRepository;
    private final WeekStrongService weekStrongService;

    @Autowired
    public ApiController(UserRepository userRepository, ExerciseRepository exerciseRepository, ProgramRepository programRepository, WeekStrongService weekStrongService) {
        this.userRepository = userRepository;
        this.exerciseRepository = exerciseRepository;
        this.programRepository = programRepository;
        this.weekStrongService = weekStrongService;
    }

    @GetMapping("/exercises")
    public List<Exercise> listExercises() {
        return this.exerciseRepository.listExercises();
    }

    @GetMapping("/email/available")
    public boolean emailAvailable(String email) {
        return this.userRepository.userEmailAvailable(email);
    }

    @PostMapping("/user")
    public User createUser(@RequestBody CreateUserBean newUser) {

        // save the user
        User user = new User(newUser.getName(), newUser.getEmail(), newUser.getPassword());
        this.userRepository.saveUser(user);

        return user;
    }

    @PostMapping("/program")
    public Program createProgram(@RequestBody CreateProgramBean newProgram) {

        // extract the goals
        ArrayList<Goal> goals = new ArrayList<>();

        // iterate over the goals and create each one
        for (Map.Entry<Integer, Integer> exerciseGoal : newProgram.getExercises().entrySet()) {
            Goal goal = new Goal(this.exerciseRepository.getExercise(exerciseGoal.getKey()), exerciseGoal.getValue());
            goals.add(goal);
        }

        // create a program
        Program program = new Program(newProgram.getSelectedDays(), newProgram.getWeeks(), goals, LocalDate.now());

        // save the program
        this.programRepository.saveProgram(program, newProgram.getUserId());

        return program;
    }


    @PostMapping("/user/authenticate")
    public User authenticateUser(String email, String password, HttpServletResponse response) {

        User user = this.userRepository.getUser(email, password);

        if (user == null) {
            // set the response status
            response.setStatus(401);
        }

        System.out.println(user);

        return user;
    }

    @JsonView(View.Program.class)
    @GetMapping("/user/{id}/program/latest")
    public Program getLatestProgram(@PathVariable Integer id) {
        return this.programRepository.getLatestProgramForUser(id);
    }

    @GetMapping("/program/{id}/history")
    public List<Event> getProgramEventHistory(@PathVariable Integer id) {

        return this.weekStrongService.getProgramEventHistory(id);
    }


    @GetMapping("/gen")
    public String generateTestData() {
        //this.programRepository.generateTestData();
        return "done";
    }


}
