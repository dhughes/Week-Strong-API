package net.doughughes.repositories;

import net.doughughes.entity.Exercise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExerciseRepository {

    private final
    JdbcTemplate template;

    @Autowired
    public ExerciseRepository(JdbcTemplate template) {
        this.template = template;
    }

    public List<Exercise> listExercises() {
        return this.template.query(
                "SELECT * FROM exercise",
                (resultSet, i) ->
                        new Exercise(
                                resultSet.getLong("id"),
                                resultSet.getString("name"),
                                resultSet.getString("description"),
                                resultSet.getString("image"),
                                resultSet.getInt("defaultgoal"),
                                resultSet.getInt("minimum"),
                                resultSet.getInt("step")

                        ));
    }

    public Exercise getExercise(Integer key) {
        return this.template.queryForObject(
                "SELECT * FROM exercise WHERE id = ?",
                (resultSet, i) ->
                        new Exercise(
                                resultSet.getLong("id"),
                                resultSet.getString("name"),
                                resultSet.getString("description"),
                                resultSet.getString("image"),
                                resultSet.getInt("defaultgoal"),
                                resultSet.getInt("minimum"),
                                resultSet.getInt("step")

                        ), key);
    }
}
