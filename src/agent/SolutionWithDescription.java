package agent;

public class SolutionWithDescription {

    private Solution solution;
    private String description;
    private String statistics;

    public SolutionWithDescription(Solution solution, String description, String statistics) {
        this.solution = solution;
        this.description = description;
        this.statistics = statistics;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return statistics;
    }
}
