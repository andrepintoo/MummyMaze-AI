package mummymaze;

import agent.Heuristic;

public class HeuristicDistanceToEnemies extends Heuristic<MummyMazeProblem, MummyMazeState> {
    @Override
    public double compute(MummyMazeState state) {
        return state.computeDistanceToEnemies();
    }

    @Override
    public String toString() {
        return "Distance to enemies";
    }
}
