package net.doughughes.repositories;

import net.doughughes.bean.Event;
import net.doughughes.entity.Exercise;
import net.doughughes.entity.Goal;
import net.doughughes.entity.Program;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Component
public class ProgramRepository {

    private final JdbcTemplate template;

    @Autowired
    public ProgramRepository(JdbcTemplate template) {
        this.template = template;
    }

    public void saveProgram(Program program, long userId) {
        // save program
        Long programId = this.template.queryForObject(
                "INSERT INTO program (weeks, user_id, created) VALUES (?,?,?) RETURNING id",
                long.class,
                program.getWeeks(),
                userId,
                Date.valueOf(program.getCreated()));
        // set the program ID to the generated PK id
        program.setId(programId);

        // save days (and set programId correctly)
        for (Integer day : program.getSelectedDays()) {
            this.template.update(
                    "INSERT INTO program_day (program_id, day_id) VALUES (?, ?)",
                    program.getId(),
                    day
            );
        }

        // save goals (and set programId correctly)
        for (Goal goal : program.getGoals()) {
            this.template.update(
                    "INSERT INTO goal (program_id, exercise_id, goal) VALUES (?, ?, ?)",
                    program.getId(),
                    goal.getExercise().getId(),
                    goal.getGoal()
            );
        }
    }

    public Program getLatestProgramForUser(long userId) {

        return this.template.query(
                "SELECT p.id AS program_id, p.weeks, p.created, pd.day_id, g.goal, e.id AS exercise_id, e.name, e.description, e.image, e.defaultgoal, e.minimum, e.step " +
                        "FROM program AS p JOIN program_day AS pd " +
                        "  ON p.id = pd.program_id " +
                        "JOIN goal AS g " +
                        "  ON p.id = g.program_id " +
                        "JOIN exercise AS e " +
                        "  ON g.exercise_id = e.id " +
                        "WHERE user_id = ? AND p.id = (SELECT MAX(id) FROM program WHERE user_id = ?)" +
                        "ORDER BY pd.day_id",
                (resultSet) -> {
                    Integer weeks = null;
                    Long id = null;
                    LocalDate created = null;
                    HashSet<Integer> selectedDays = new HashSet<>();
                    Map<Long, Goal> goals = new HashMap<>();

                    // extract days and goals
                    while (resultSet.next()) {
                        // get the weeks (over and over)
                        weeks = resultSet.getInt("weeks");
                        id = resultSet.getLong("program_id");
                        created = resultSet.getDate("created").toLocalDate();

                        // collect the days
                        selectedDays.add(resultSet.getInt("day_id"));

                        // do we already have this goal?
                        if (!goals.containsKey(resultSet.getLong("exercise_id"))) {
                            // nope, create the goal
                            Goal goal = new Goal(
                                    new Exercise(
                                            resultSet.getLong("exercise_id"),
                                            resultSet.getString("name"),
                                            resultSet.getString("description"),
                                            resultSet.getString("image"),
                                            resultSet.getInt("defaultGoal"),
                                            resultSet.getInt("minimum"),
                                            resultSet.getInt("step")
                                    ),
                                    resultSet.getInt("goal")
                            );
                            // collect the goals!
                            goals.put(resultSet.getLong("exercise_id"), goal);
                        }

                    }

                    if (id == null) return null;

                    // create a program with the days and goals and all that
                    return new Program(
                            id,
                            new ArrayList<Integer>(selectedDays),
                            weeks,
                            new ArrayList<Goal>(goals.values()),
                            created
                    );
                }
                , userId
                , userId);
    }

    public List<Event> getProgramHistory(Integer id) {
        return this.template.query(
                "SELECT * FROM workout",
                (resultSet, i) ->
                        new Event());
    }
}
