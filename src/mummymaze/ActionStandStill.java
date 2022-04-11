package mummymaze;

import agent.Action;

import java.util.List;

public class ActionStandStill extends Action<MummyMazeState> {
    public ActionStandStill(double cost) {
        super(cost);
    }

    @Override
    public void execute(MummyMazeState state) {
        List<String> movements = state.moveStandStill();
        addMovement(movements);
        state.setAction(this);
    }

    @Override
    public boolean isValid(MummyMazeState state) {
        return true;
    }
}
