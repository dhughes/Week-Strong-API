package net.doughughes.repositories;

import net.doughughes.entity.Goal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class GoalRepository {

    @Autowired
    JdbcTemplate template;

    public void saveGoal(Goal goal) {

    }

}
