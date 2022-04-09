package mummymaze;

import agent.Action;

public class ActionLeft extends Action<MummyMazeState> {

    public ActionLeft(double cost) {
        super(cost); //TODO definir o custo das ações
    }

    @Override
    public void execute(MummyMazeState State) {

    }

    @Override
    public boolean isValid(MummyMazeState State) {
        return false;
    }
}
