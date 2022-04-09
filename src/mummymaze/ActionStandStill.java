package mummymaze;

import agent.Action;

public class ActionStandStill extends Action<MummyMazeState> {
    public ActionStandStill(double cost) {
        super(cost);
    }

    @Override
    public void execute(MummyMazeState State) {

    }

    @Override
    public boolean isValid(MummyMazeState State) {
        return false;
    }
}
