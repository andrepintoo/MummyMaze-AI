package searchmethods;

import agent.State;
import java.util.List;

public class GreedyBestFirstSearch extends InformedSearch {

    //f = h
    @Override
    public void addSuccessorsToFrontier(List<State> successors, Node parent) {

        //TODO
        // usar
        for (State state: successors) {
            double g = parent.getG() + state.getAction().getCost();
            double h = heuristic.compute(state);
            if(!frontier.containsState(state)){
                if(!explored.contains(state)){
                    frontier.add(new Node(state, parent, g, h));  /*prioridade f=h*/
                }
            }else{ //se está na fronteira, de certeza que ainda não está no conjunto de nós explorados
//                double h2 = frontier.getNode(state).getF();
                double g2 = frontier.getNode(state).getG();
                if(g < g2){
                    frontier.removeNode(state); //remove o nó que tem este estado
                    frontier.add(new Node(state, parent, g, h));
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Greedy best first search";
    }
}