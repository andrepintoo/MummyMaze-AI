package searchmethods;

import agent.Problem;
import agent.Solution;
import agent.State;
import java.util.List;
import utils.NodeLinkedList;

public class DepthFirstSearch extends GraphSearch<NodeLinkedList> {

    public DepthFirstSearch() {
        frontier = new NodeLinkedList();
    }

    //Graph Search without explored list - é preciso fazer override ao super.graphSearch()
    @Override
    protected Solution graphSearch(Problem problem) {
        //TODO

        Node node = new Node(problem.getInitialState()); //os nós servem só para encapsular os states
        frontier.clear();
        //explored.clear();
        frontier.add(node);

        while (!frontier.isEmpty() && !stopped){
            Node frontierNode = frontier.remove();
            State frontierNodeState = frontierNode.getState();
            if(problem.isGoal(frontierNodeState)){
                return new Solution(problem, frontierNode);
            }

            //explored.add(frontierNodeState);

            List<State> successors = problem.executeActions(frontierNodeState);
            addSuccessorsToFrontier(successors, frontierNode); //como depende do algoritmo, chamo o método abstrato e depois cada um faz à sua maneira

            computeStatistics(successors.size());
        }

        //devolve null se não conseguir encontrar a solution
        return null;
    }

    @Override
    public void addSuccessorsToFrontier(List<State> successors, Node parent) {

        //TODO
        //node.isCycle()          - não se usa lista de nós explorados
        //para cada nó sucessor
        for (State successor: successors){
            //Node no = new Node(successor, parent);
            if(!frontier.containsState(successor) && parent != null/*!explored.contains(successor)*/){
                if(!parent.isCycle(successor)){ //este é muito pesado - problemas que nao precisem do isCycle correm bem neste algoritmo
                    //          adicionar ao INICIO da lista ligada (pilha)
                    frontier.addFirst(new Node(successor,parent));
                    //é preciso criar o node com o parent para depois poder construir a solution
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Depth first search";
    }
}
