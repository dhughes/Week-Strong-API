package net.doughughes.repositories;

import net.doughughes.entity.Workout;
import net.doughughes.entity.WorkoutRound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WorkoutRepository {

    private final JdbcTemplate template;

    @Autowired
    public WorkoutRepository(JdbcTemplate template) {
        this.template = template;
    }

    public List<Workout> getWorkoutsForProgram(Long programId) {
        return this.template.query("" +
                        "SELECT w.date, r.*  " +
                        "FROM workout AS w JOIN workout_round AS r " +
                        "   ON w.id = r.workout_id " +
                        "WHERE w.program_id = ? " +
                        "ORDER BY w.id, r.round, r.exercise_id",
                (resultSet) -> {
                    List<Workout> workouts = new ArrayList<>();

                    Workout workout = null;

                    while (resultSet.next()) {

                        if (workout == null || workout.getId() != resultSet.getLong("workout_id")) {
                            // no workout yet? create the first one!
                            workout = new Workout(
                                    resultSet.getLong("workout_id"),
                                    resultSet.getDate("date").toLocalDate()
                            );

                            workouts.add(workout);
                        }

                        workout.getRounds().add(new WorkoutRound(
                                resultSet.getLong("id"),
                                resultSet.getInt("round"),
                                resultSet.getInt("reps"),
                                resultSet.getInt("exercise_id")
                        ));
                    }

                    return workouts;
                },
                programId);
    }
}
