package mummymaze;

import agent.Action;

public class ActionDown extends Action<MummyMazeState> {
    public ActionDown(double cost) {
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
