package mummymaze;

import agent.Action;

import java.util.List;

public class ActionLeft extends Action<MummyMazeState> {

    public ActionLeft(double cost) {
        super(cost); //TODO definir o custo das ações
    }

    public ActionLeft(ActionLeft actionLeft) {
        super(actionLeft.getCost());
    }

    @Override
    public void execute(MummyMazeState state) {
        List<String> movements = state.moveLeft();
        addMovement(movements);
        state.setAction(new ActionLeft(this));
    }

    @Override
    public boolean isValid(MummyMazeState state) {
        return state.canMoveLeftHero();
    }


}
