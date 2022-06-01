package mummymaze;

import agent.Heuristic;

public class HeuristicWallsAroundEnemies extends Heuristic<MummyMazeProblem, MummyMazeState> {
    @Override
    public double compute(MummyMazeState state) {
        return state.computeWallsAroundEnemies();
    }

    @Override
    public String toString() {
        return "Walls around enemies";
    }
}

