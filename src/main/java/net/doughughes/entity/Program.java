package net.doughughes.entity;

import net.doughughes.bean.Event;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Program {

    private Long id;
    private final List<Integer> selectedDays;
    private final Integer weeks;
    private List<Goal> goals;
    private LocalDate created;
    private Test test;
    private List<Workout> workouts = new ArrayList<>();


    public Program(List<Integer> selectedDays, Integer weeks, ArrayList<Goal> goals, LocalDate created) {
        this.selectedDays = selectedDays;
        this.weeks = weeks;
        this.goals = goals;
        this.created = created;
    }

    public Program(Long id, List<Integer> selectedDays, Integer weeks, ArrayList<Goal> goals, LocalDate created, Test test, List<Workout> workouts) {
        this.id = id;
        this.selectedDays = selectedDays;
        this.weeks = weeks;
        this.goals = goals;
        this.created = created;
        this.test = test;
        this.workouts = workouts;
    }

    public List<Event> getHistory() {
        List<Event> eventHistory = new ArrayList<>();

        LocalDate today = LocalDate.now();

        // find the sunday of or before this program was created
        LocalDate beginDate = getCreated().getDayOfWeek().getValue() == 7
                ? getCreated()
                : getCreated().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));

        // find the saturday of today or the end of the week.
        LocalDate endDate = today.getDayOfWeek().getValue() == 6 ? today : today.with(TemporalAdjusters.next(DayOfWeek.SATURDAY));

        // create our history
        for (int d = 0; d <= Period.between(beginDate, endDate).getDays(); d++) {
            LocalDate thisDate = beginDate.plusDays(d);
            Event event = new Event(thisDate, !thisDate.isBefore(getTest().getDate()));

            // is this a workout day?
            boolean isWorkoutDay = getSelectedDays().contains(thisDate.getDayOfWeek().getValue());
            Optional<Workout> workout = getWorkouts().stream().filter(work -> work.getDate().equals(event.getDate())).findFirst();
            boolean workedOut = false;
            if (workout.isPresent()) {
                workedOut = true;
                event.setWorkout(workout.get().getId());
            }

            // if we're in the program figure out what type of event this is
            if (event.isInProgram()) {
                // is this the test day?
                if (thisDate.equals(getTest().getDate())) {
                    event.setType(Event.Type.TEST);

                    // did we workout on a workout day? (that's just a regular workout)
                } else if (workedOut && isWorkoutDay) {
                    event.setType(Event.Type.WORKOUT);

                    // did we skip a workout day? (that's a skipped workout)
                } else if (!workedOut && isWorkoutDay) {
                    event.setType(Event.Type.SKIP);

                    // did we workout on a non-workout day? (that's a makeup workout)
                } else if (workedOut && !isWorkoutDay) {
                    event.setType(Event.Type.MAKEUP);
                }
            }

            // add the event to history
            eventHistory.add(event);
        }

        return eventHistory;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Integer> getSelectedDays() {
        return this.selectedDays;
    }

    public Integer getWeeks() {
        return this.weeks;
    }

    public List<Goal> getGoals() {
        return this.goals;
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }

    public LocalDate getCreated() {
        return this.created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public Test getTest() {
        return this.test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public List<Workout> getWorkouts() {
        return this.workouts;
    }

    public void setWorkouts(List<Workout> workouts) {
        this.workouts = workouts;
    }
}
