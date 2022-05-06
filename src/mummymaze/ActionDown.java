package mummymaze;

import agent.Action;

import java.util.List;

public class ActionDown extends Action<MummyMazeState> implements Cloneable  {
    public ActionDown(double cost) {
        super(cost);
    }

    public ActionDown(ActionDown actionDown) {
        super(actionDown.getCost());
    }

    @Override
    public void execute(MummyMazeState state) {
        this.movements =  state.moveDown();
//        addMovement(movements);
        state.setAction(new ActionDown(this));
    }

    @Override
    public boolean isValid(MummyMazeState state) {
        return state.canMoveDownHero();
    }
}
