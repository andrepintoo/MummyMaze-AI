package mummymaze;

import agent.Action;

import java.util.List;

public class ActionRight extends Action<MummyMazeState> {

    public ActionRight(double cost) {
        super(cost); //TODO definir o custo das ações
    }

    @Override
    public void execute(MummyMazeState state) {
        List<String> movements = state.moveRight();
        addMovement(movements);
        state.setAction(this);
    }

    @Override
    public boolean isValid(MummyMazeState state) {
        return state.canMoveRight();
    }
}
