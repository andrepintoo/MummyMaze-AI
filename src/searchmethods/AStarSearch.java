package searchmethods;

import agent.State;
import java.util.List;

public class AStarSearch extends InformedSearch {

    //f = g + h
    @Override
    public void addSuccessorsToFrontier(List<State> successors, Node parent) {

        //TODO
        for (State state: successors) {
            double g = parent.getG() + state.getAction().getCost();
            double h = heuristic.compute(state);
            if(!frontier.containsState(state)){
                if(!explored.contains(state)){
                    frontier.add(new Node(state, parent, g, g + h));  /*prioridade f = g + h*/
                }
            }else{ //podia-se eliminar este else caso se tivesse só heuristicas admissiveis (e consistentes)
                    //mas no mummyMaze é melhor manter o else, pois as heuristicas podem nao estar bem definidas

//                double h2 = frontier.getNode(state).getF();
                double g2 = frontier.getNode(state).getG();
                if(g < g2){
                    frontier.removeNode(state); //remove o nó que tem este estado
                    frontier.add(new Node(state, parent, g, g + h));
                }
            }
        }

    }

    @Override
    public String toString() {
        return "A* search";
    }
}
