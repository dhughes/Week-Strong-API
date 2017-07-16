package net.doughughes.repositories;

import net.doughughes.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserRepository {

    private final JdbcTemplate template;
    private final ProgramRepository programRepository;

    @Autowired
    public UserRepository(JdbcTemplate template, ProgramRepository programRepository) {
        this.template = template;
        this.programRepository = programRepository;
    }

    public void saveUser(User user) {
        if (user.getId() == null) {
            Long userId = this.template.queryForObject(
                    "INSERT INTO \"user\" (name, email, password) VALUES (?,?,?) RETURNING id",
                    long.class,
                    user.getName(),
                    user.getEmail(),
                    user.getPassword());
            user.setId(userId);
        } else {
            this.template.update(
                    "UPDATE \"user\" SET name = ?, email = ?, password = ? WHERE id = ?",
                    user.getName(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getId());
        }
    }

    public boolean userEmailAvailable(String email) {
        Integer count = this.template.queryForObject("SELECT count(*) AS count FROM \"user\" WHERE email = ?", Integer.class, email);
        return count == 0;
    }

    public User getUser(String email, String password) {
        try {
            return this.template.queryForObject(
                    "SELECT * FROM \"user\" WHERE email = ? AND password = ?",
                    (resultSet, i) ->
                            new User(
                                    resultSet.getLong("id"),
                                    resultSet.getString("name"),
                                    resultSet.getString("email"),
                                    this.programRepository.getProgramForUser(resultSet.getLong("id"))

                            ), email, password);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
