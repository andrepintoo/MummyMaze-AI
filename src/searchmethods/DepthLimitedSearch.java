package searchmethods;

import agent.State;
import java.util.List;

public class DepthLimitedSearch extends DepthFirstSearch {

    private int limit;

    public DepthLimitedSearch() {
        this(28);
    }

    public DepthLimitedSearch(int limit) {
        this.limit = limit;
    }

    @Override
    public void addSuccessorsToFrontier(List<State> successors, Node parent) {

        //TODO
        /*for (State successor: successors){
            Node no = new Node(successor, parent);
            //      se não estiver na fronteira nem na lista dos nós explorados
            if(!frontier.containsState(successor) && no.getDepth() != limit && !parent.isCycle(successor)){
                //          adicionar ao INICIO da lista ligada (pilha (?) )
                frontier.addFirst(new Node(successor,parent)); //até seria o mesmo que fazer .add() ou .addLast()
                //é preciso criar o node com o parent para depois poder construir a solution
            }
        }*/
        if(parent.getDepth() < limit){ //se a profundidade do nó que está a ser expandido for menor que o limite
            super.addSuccessorsToFrontier(successors,parent);
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "Limited depth first search";
    }
}
