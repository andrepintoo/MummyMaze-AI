package mummymaze;

import agent.Action;
import agent.Heuristic;
import agent.Problem;

import java.util.ArrayList;
import java.util.List;

public class MummyMazeProblem extends Problem<MummyMazeState> {


    public MummyMazeProblem(MummyMazeState initialState) {
        super(initialState, new ArrayList<>());
        actions.add(new ActionUp(1));
        actions.add(new ActionRight(1));
        actions.add(new ActionDown(1));
        actions.add(new ActionLeft(1));
        actions.add(new ActionStandStill(1));
    }


    @Override
    public List<MummyMazeState> executeActions(MummyMazeState state) {
        List<MummyMazeState> successors_list = new ArrayList<>(5); //tem-se 5 ações possíveis
        for (Action a: actions) {
            if(a.isValid(state)){
                MummyMazeState successor = (MummyMazeState) state.clone();
                successor.executeAction(a);
                successors_list.add(successor);
                if(successor.isGameOver()){
                    break;
                }
            }
        }
        return successors_list;
    }

    @Override
    public boolean isGoal(MummyMazeState state) {
        return state.computeExitDistance() == 0; //AND Hero can't die in the action
    }
}
