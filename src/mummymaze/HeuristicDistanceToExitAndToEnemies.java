package mummymaze;

import agent.Heuristic;

public class HeuristicDistanceToExitAndToEnemies extends Heuristic<MummyMazeProblem, MummyMazeState> {
    @Override
    public double compute(MummyMazeState state) {
        return state.computeDistanceToEnemies() + state.computeExitDistance();
    }

    @Override
    public String toString() {
        return "Distance to enemies and to exit";
    }
}
