package net.doughughes.repositories;

import net.doughughes.entity.Test;
import net.doughughes.entity.TestRound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TestRepository {

    private JdbcTemplate template;

    @Autowired
    public TestRepository(JdbcTemplate template) {
        this.template = template;
    }

    public Test getTestForProgram(Long programId) {
        return this.template.query("" +
                        "SELECT t.date, r.*  " +
                        "FROM test AS t JOIN test_round AS r " +
                        "   ON t.id = r.test_id " +
                        "WHERE t.program_id = ? " +
                        "ORDER BY r.round, r.exercise_id",
                (resultSet) -> {
                    Test test = null;

                    while (resultSet.next()) {
                        // if we don't have the test yet, then create it
                        if (test == null) {
                            test = new Test(
                                    resultSet.getLong("test_id"),
                                    resultSet.getDate("date")
                            );
                        }

                        test.getRounds().add(new TestRound(
                                resultSet.getLong("id"),
                                resultSet.getInt("round"),
                                resultSet.getInt("reps"),
                                resultSet.getInt("exercise_id")
                        ));
                    }


                    return test;
                },
                programId);
    }
}
