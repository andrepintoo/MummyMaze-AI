package searchmethods;

import agent.Problem;
import agent.Solution;
import agent.State;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import utils.NodeCollection;

public abstract class GraphSearch<L extends NodeCollection> implements SearchMethod {

    protected L frontier;
    protected Set<State> explored = new HashSet<>();
    protected Statistics statistics = new Statistics();    
    protected boolean stopped;

    @Override
    public Solution search(Problem problem) {
        statistics.reset();
        stopped = false;
        return graphSearch(problem);
    }

    protected Solution graphSearch(Problem problem) {
        // TODO
        Node node = new Node(problem.getInitialState()); //os nós servem só para encapsular os states
        frontier.clear();
        explored.clear();
        frontier.add(node);
        while (!frontier.isEmpty() && !stopped){
            //remove the first node from the frontier
            Node frontierNode = frontier.remove();

            State frontierNodeState = frontierNode.getState();

            //if the node contains a goal state then return the corresponding solution
            if(problem.isGoal(frontierNodeState)){
                return new Solution(problem, frontierNode);
            }

            // add the node to the explored set
            explored.add(frontierNodeState);

            //expandir o nó é expandir os nós sucessores
            List<State> successors = problem.executeActions(frontierNodeState);
            addSuccessorsToFrontier(successors, frontierNode); //como depende do algoritmo, chamo o método abstrato e depois cada um faz à sua maneira

            computeStatistics(successors.size());
        }
        //devolve null se não conseguir encontrar a solution
        return null;
    }

    public abstract void addSuccessorsToFrontier(List<State> successors, Node parent); //vai depender dos algoritmos

    protected void computeStatistics(int successorsSize) {
        statistics.numExpandedNodes++;      //nº de nós avaliados
        statistics.numGeneratedNodes += successorsSize;     //nº de nós avaliados + sucessores
        statistics.maxFrontierSize = Math.max(statistics.maxFrontierSize, frontier.size());
    }
    
    @Override
    public Statistics getStatistics(){
        return statistics;
    }

    @Override
    public void stop() {
        stopped = true;
    }

    @Override
    public boolean hasBeenStopped() {
        return stopped;
    }
}
    