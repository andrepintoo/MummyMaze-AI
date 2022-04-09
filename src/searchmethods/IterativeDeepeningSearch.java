package searchmethods;

import agent.Problem;
import agent.Solution;
import agent.State;

import java.util.List;

public class IterativeDeepeningSearch extends DepthFirstSearch {
    /*
     * We do not use the code from DepthLimitedSearch because we can optimize
     * so that the algorithm only verifies if a state is a goal if its depth is
     * equal to the limit. Note that given a limit X we are sure not to
     * encounter a solution below this limit because a (failed) limited depth
     * search has already been done. That's why we do not extend this class from
     * DepthLimitedSearch. We extend from DepthFirstSearch so that we don't need
     * to rewrite method insertSuccessorsInFrontier again.
     * After the class, please see a version of the search algorithm without
     * this optimization.
     */

    private int limit;

    @Override
    public Solution search(Problem problem) {
        statistics.reset();
        stopped = false;
        limit = 0;

        //TODO
        // start with limit zero; if solution not found, tries again
        // with limit += 1
        Solution solution;
        do{
            /*if((solution = graphSearch(problem)) != null){
                return solution;
            }*/
            solution = graphSearch(problem);
            limit++;

        }while (solution == null);

        return solution;
    }

    //só verificamos se é o goal state se estiver no limit depth
    //só se expande os nós se estivermos abaixo do nivel limite

    @Override
    protected Solution graphSearch(Problem problem) {

        //TODO
        // only check if a node is the goal if at limit depth
        // only expand if node is below limit depth
        Node node = new Node(problem.getInitialState());
        frontier.clear();
        //explored.clear();
        frontier.add(node);

        while (!frontier.isEmpty() && !stopped) {
            Node frontierNode = frontier.remove();
            State frontierNodeState = frontierNode.getState();
            if (frontierNode.getDepth() == limit && problem.isGoal(frontierNodeState)) {
                return new Solution(problem, frontierNode);
            }
            //else{
            //explored.add(frontierNodeState);

            if (frontierNode.getDepth() < limit) {
                List<State> successors = problem.executeActions(frontierNodeState);
                addSuccessorsToFrontier(successors, frontierNode);
                computeStatistics(successors.size());
            }
        }

        //devolve null se não conseguir encontrar a solution
        return null;
    }

    @Override
    public String toString() {
        return "Iterative deepening search";
    }
}


/*
 * 
 public class IterativeDeepeningSearch implements SearchMethod {

    @Override
    public Solution search(Problem problem) {
        DepthLimitedSearch dls = new DepthLimitedSearch();
        Solution solution;
        for (int i = 0;; i++) {
            dls.setLimit(i);
            solution = dls.search(problem);
            if (solution != null) {
                return solution;
            }
        }
    }

    @Override
    public String toString() {
        return "Iterative deepening search";
    }
 *
 */