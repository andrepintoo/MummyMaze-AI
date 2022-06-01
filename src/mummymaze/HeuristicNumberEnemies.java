package mummymaze;

import agent.Heuristic;

public class HeuristicNumberEnemies extends Heuristic<MummyMazeProblem, MummyMazeState> {
    @Override
    public double compute(MummyMazeState state) {
        return state.computeNumberEnemies();
    }

    @Override
    public String toString() {
        return "Number of enemies";
    }
}
