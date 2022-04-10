package mummymaze;

import agent.Action;

public class ActionRight extends Action<MummyMazeState> {

    public ActionRight(double cost) {
        super(cost); //TODO definir o custo das ações
    }

    @Override
    public void execute(MummyMazeState state) {

    }

    @Override
    public boolean isValid(MummyMazeState state) {
        return state.canMoveRight();
    }
}
