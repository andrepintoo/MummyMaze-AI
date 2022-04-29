package mummymaze;

import agent.Action;

import java.util.List;

public class ActionRight extends Action<MummyMazeState> {

    public ActionRight(double cost) {
        super(cost); //TODO definir o custo das ações
    }

    public ActionRight(ActionRight actionRight) {
        super(actionRight.getCost());
    }

    @Override
    public void execute(MummyMazeState state) {
        List<String> movements = state.moveRight();
        addMovement(movements);
        state.setAction(new ActionRight(this));
    }

    @Override
    public boolean isValid(MummyMazeState state) {
        return state.canMoveRightHero();
    }
}
