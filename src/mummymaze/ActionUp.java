package mummymaze;

import agent.Action;

import java.util.List;

public class ActionUp extends Action<MummyMazeState> {
    public ActionUp(double cost) {
        super(cost);
    }

    public ActionUp(ActionUp actionUp) {
        super(actionUp.getCost());
    }

    @Override
    public void execute(MummyMazeState state) {
        List<String> movements = state.moveUp();
        addMovement(movements);
        state.setAction(new ActionUp(this));
    }

    @Override
    public boolean isValid(MummyMazeState state) {
        return state.canMoveUpHero();
    }
}
