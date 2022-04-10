package mummymaze;

import agent.Action;
import agent.Problem;

import java.util.ArrayList;
import java.util.List;

public class MummyAgentProblem extends Problem<MummyMazeState> {

    //definir um goalState? ->  será quando 'H' (o heroi) se mover para a 'S' (a saída)
    private int[] exitPosition;

    public MummyAgentProblem(MummyMazeState initialState) {
        super(initialState, new ArrayList<>(5));
        actions.add(new ActionUp(1));
        actions.add(new ActionRight(1));
        actions.add(new ActionDown(1));
        actions.add(new ActionLeft(1));
        actions.add(new ActionStandStill(0));
        exitPosition = initialState.getExitPosition();
    }


    @Override
    public List<MummyMazeState> executeActions(MummyMazeState state) {
        List<MummyMazeState> successors_list = new ArrayList<>(5); //tem-se 5 ações
        for (Action a: actions) {
            if(a.isValid(state)){
                MummyMazeState successor = (MummyMazeState) state.clone();
                successor.executeAction(a);
                successors_list.add(successor);
            }
        }
        return successors_list;
    }

    @Override
    public boolean isGoal(MummyMazeState state) {
        return state.getExitPosition() == this.exitPosition;
    }
}
