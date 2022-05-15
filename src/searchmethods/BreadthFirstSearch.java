package searchmethods;

import agent.State;
import java.util.List;
import utils.NodeLinkedList;

public class BreadthFirstSearch extends GraphSearch<NodeLinkedList> {

    public BreadthFirstSearch() {
        frontier = new NodeLinkedList();
    }

    @Override
    public void addSuccessorsToFrontier(List<State> successors, Node parent) {
        //TODO
        //para cada nó sucessor
        for (State successor: successors){
            //      se não estiver na fronteira nem na lista dos nós explorados
            if(!frontier.containsState(successor) && !explored.contains(successor)){
                //          adicionar ao FIM da lista ligada
                frontier.addLast(new Node(successor,parent)); //até seria o mesmo que fazer .add() ou .addLast()
                    //é preciso criar o node com o parent para depois poder construir a solution
            }
//            else {
//                System.out.println("debug");
//            }
        }
    }

    @Override
    public String toString() {
        return "Breadth first search";
    }
}