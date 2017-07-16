package net.doughughes.entity;


public class Exercise {
    private final Long id;
    private final String name;
    private final String image;
    private final int defaultGoal;
    private final int minimum;
    private final int step;
    private final String description;

    public Exercise(Long id, String name, String description, String image, int defaultGoal, int minimum, int step) {

        this.id = id;
        this.name = name;
        this.image = image;
        this.defaultGoal = defaultGoal;
        this.minimum = minimum;
        this.step = step;
        this.description = description;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getImage() {
        return this.image;
    }

    public int getDefaultGoal() {
        return this.defaultGoal;
    }

    public int getMinimum() {
        return this.minimum;
    }

    public int getStep() {
        return this.step;
    }

    public String getDescription() {
        return this.description;
    }
}
