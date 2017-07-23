package net.doughughes.controller;

import net.doughughes.bean.CreateProgramBean;
import net.doughughes.bean.CreateUserBean;
import net.doughughes.entity.Exercise;
import net.doughughes.entity.Goal;
import net.doughughes.entity.Program;
import net.doughughes.entity.User;
import net.doughughes.repositories.ExerciseRepository;
import net.doughughes.repositories.ProgramRepository;
import net.doughughes.repositories.UserRepository;
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

    @Autowired
    public ApiController(UserRepository userRepository, ExerciseRepository exerciseRepository, ProgramRepository programRepository) {
        this.userRepository = userRepository;
        this.exerciseRepository = exerciseRepository;
        this.programRepository = programRepository;
    }

    @GetMapping("/gen")
    public String generateTestData() {
        this.programRepository.generateTestData();
        return "done";
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
    public User createUser(@RequestBody CreateUserBean newUserBean) {

        // save the user
        User user = new User(newUserBean.getName(), newUserBean.getEmail(), newUserBean.getPassword());
        this.userRepository.saveUser(user);

        // save the user's program
        if (newUserBean.getProgram() != null) {
            CreateProgramBean programBean = newUserBean.getProgram();

            // extract the goals
            ArrayList<Goal> goals = new ArrayList<>();

            // iterate over the goals
            for (Map.Entry<Integer, Integer> exerciseGoal : programBean.getExercises().entrySet()) {
                Goal goal = new Goal(this.exerciseRepository.getExercise(exerciseGoal.getKey()), exerciseGoal.getValue());
                goals.add(goal);
            }

            Program program = new Program(programBean.getSelectedDays(), programBean.getWeeks(), goals, LocalDate.now());

            this.programRepository.saveProgram(program, user.getId());
        }

        return user;
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

}
