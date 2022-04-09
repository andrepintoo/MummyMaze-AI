package mummymaze;

import agent.Action;

public class ActionUp extends Action<MummyMazeState> {
    public ActionUp(double cost) {
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
