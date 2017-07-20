package net.doughughes.repositories;

import net.doughughes.entity.Exercise;
import net.doughughes.entity.Goal;
import net.doughughes.entity.Program;
import net.doughughes.entity.Workout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

@Component
public class ProgramRepository {

    private final JdbcTemplate template;
    private final WorkoutRepository workoutRepository;
    private final TestRepository testRepository;

    @Autowired
    public ProgramRepository(JdbcTemplate template, WorkoutRepository workoutRepository, TestRepository testRepository) {
        this.template = template;
        this.workoutRepository = workoutRepository;
        this.testRepository = testRepository;
    }


    public void saveProgram(Program program, long userId) {
        // save program
        Long programId = this.template.queryForObject(
                "INSERT INTO program (weeks, user_id) VALUES (?,?) RETURNING id",
                long.class,
                program.getWeeks(),
                userId);
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

    public Program getProgramForUser(long userId) {

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
                    Date created = null;
                    HashSet<Integer> selectedDays = new HashSet<>();
                    Map<Long, Goal> goals = new HashMap<>();

                    // extract days and goals
                    while (resultSet.next()) {
                        // get the weeks (over and over)
                        weeks = resultSet.getInt("weeks");
                        id = resultSet.getLong("program_id");
                        created = resultSet.getDate("created");

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
                            created,
                            this.testRepository.getTestForProgram(id),
                            this.workoutRepository.getWorkoutsForProgram(id)
                    );
                }
                , userId
                , userId);
    }

    public void generateTestData() {

        System.out.println("--------");

        int userId = 16;
        int programId = 2;
        Random rand = new Random();
        LocalDate createdDate = LocalDate.now().minusDays(20);

        // remove all workouts and test records
        // delete the program's days
        this.template.update("" +
                        "DELETE FROM test_round;" +
                        "DELETE FROM test;" +
                        "DELETE FROM workout_round;\n" +
                        "DELETE FROM workout;" +
                        "DELETE FROM program_day WHERE program_id = ?",
                programId);

        // update the program creation date to 16 days ago
        this.template.update(
                "UPDATE program SET created = ? WHERE id = ?",
                java.sql.Date.valueOf(createdDate),
                programId);

        // set the days of the week
        int day1 = rand.nextInt(2);
        int day2 = rand.nextInt(3) + 2;
        int day3 = rand.nextInt(2) + 5;

        this.template.update("" +
                        "INSERT INTO program_day (program_id, day_id) VALUES (?, ?); " +
                        "INSERT INTO program_day (program_id, day_id) VALUES (?, ?); " +
                        "INSERT INTO program_day (program_id, day_id) VALUES (?, ?); ",
                programId, day1,
                programId, day2,
                programId, day3
        );


        // create a test record
        LocalDate testDate = createdDate.plusDays(2);
        Integer testId = this.template.queryForObject("" +
                        "INSERT INTO test (program_id, date) " +
                        "VALUES (?, ?) RETURNING id",
                Integer.class,
                2,
                java.sql.Date.valueOf(testDate)
        );

        // get the exercises and goals in this program
        List<Goal> goals = this.template.query("" +
                        "SELECT e.*, g.goal " +
                        "FROM exercise e JOIN goal g " +
                        "   ON g.exercise_id = e.id " +
                        "WHERE g.program_id = ?",
                (resultSet, i) -> new Goal(
                        new Exercise(
                                resultSet.getLong("id"),
                                resultSet.getString("name"),
                                resultSet.getString("description"),
                                resultSet.getString("image"),
                                resultSet.getInt("defaultGoal"),
                                resultSet.getInt("minimum"),
                                resultSet.getInt("step")
                        ),
                        resultSet.getInt("goal")
                ),
                programId
        );

        // create the test data

        // we're doing 5 rounds for each
        for (int round = 1; round <= 5; round++) {
            // add a record for each exercise/goal
            for (Goal goal : goals) {
                this.template.update("" +
                                "INSERT INTO test_round (test_id, round, reps, exercise_id) " +
                                "VALUES (?, ?, ?, ?)",
                        testId,
                        round,
                        rand.nextInt(goal.getGoal() / 10) + 1,
                        goal.getExercise().getId()
                );
            }
        }

        // create the workout data
        long between = DAYS.between(testDate, LocalDate.now()) - 1;
        boolean skipped = false;
        int skipCount = 0;
        for (int days = 1; days <= between; days++) {
            LocalDate thisDay = testDate.plusDays(days);
            int dayOfWeek = thisDay.getDayOfWeek().getValue();

            // is this a workout day?
            if (dayOfWeek == (day1 == 0 ? 7 : day1) ||
                    dayOfWeek == (day2 == 0 ? 7 : day2) ||
                    dayOfWeek == (day3 == 0 ? 7 : day3) || skipped) {

                //System.out.println(thisDay);

                // did we miss this day?
                if (rand.nextInt(6) == 0) {
                    System.out.printf("** Skipping workout on %s **\n", thisDay);
                    skipped = true;
                    skipCount++;
                    continue;
                }
                if (!(dayOfWeek == (day1 == 0 ? 7 : day1) ||
                        dayOfWeek == (day2 == 0 ? 7 : day2) ||
                        dayOfWeek == (day3 == 0 ? 7 : day3)))
                    System.out.printf("** Doing makeup workout on %s **\n", thisDay);

                // reset the skipped state
                skipped = false;

                // create the workout
                Integer workoutId = this.template.queryForObject("" +
                                "INSERT INTO workout (program_id, date) " +
                                "VALUES (?, ?) RETURNING id",
                        Integer.class,
                        2,
                        java.sql.Date.valueOf(thisDay)
                );

                // create workout details
                // we're doing 5 rounds for each
                for (int round = 1; round <= 5; round++) {
                    // add a record for each exercise/goal
                    for (Goal goal : goals) {
                        this.template.update("" +
                                        "INSERT INTO workout_round (workout_id, round, reps, exercise_id) " +
                                        "VALUES (?, ?, ?, ?)",
                                workoutId,
                                round,
                                rand.nextInt(goal.getGoal() / 10) + 1,
                                goal.getExercise().getId()
                        );
                    }
                }


            }

        }

        Program program = getProgramForUser(userId);

        System.out.println("");
        System.out.println("Generated Data Summary:");
        System.out.printf("Created: %s\n", program.getCreated());
        System.out.printf("Test Date: %s\n", program.getTest().getDate());
        System.out.printf("Workout Days: %s (%s), %s (%s), %s (%s)\n",
                DayOfWeek.of(day1 == 0 ? 7 : day1), day1,
                DayOfWeek.of(day2 == 0 ? 7 : day2), day2,
                DayOfWeek.of(day3 == 0 ? 7 : day3), day3);

        System.out.printf("Skipped Workouts: %s\n", skipCount);
        System.out.println("Workouts:");
        for (Workout workout : program.getWorkouts()) {
            System.out.printf("\t%s, %s\n", ((java.sql.Date) workout.getDate()).toLocalDate().getDayOfWeek(), workout.getDate());
        }
        
        System.out.println("--------");


    }
}
