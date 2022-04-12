package agent;

import java.util.ArrayList;
import searchmethods.*;

public class Agent<E extends State> {

    protected E environment;
    protected ArrayList<SearchMethod> searchMethods;
    protected SearchMethod searchMethod;
    protected ArrayList<Heuristic> heuristics; //conjunto de heuristicas
    protected Heuristic heuristic;
    protected Solution solution;

    public Agent(E environment) {
        this.environment = environment;
        searchMethods = new ArrayList<>();
        searchMethods.add(new AStarSearch());
        searchMethods.add(new BreadthFirstSearch());
        searchMethods.add(new UniformCostSearch());
        searchMethods.add(new DepthFirstSearch());
        searchMethods.add(new DepthLimitedSearch());
        searchMethods.add(new IterativeDeepeningSearch());
        searchMethods.add(new GreedyBestFirstSearch());
        searchMethods.add(new BeamSearch());
        searchMethods.add(new IDAStarSearch());
        searchMethod = searchMethods.get(0);
        heuristics = new ArrayList<>();
    }

    //recebe um problema e devolve uma solucao, através de um método de procura
    public Solution solveProblem(Problem problem) {
        if (heuristic != null) { //se tiver uma heuristica (caso o metodo de procura seja informado)
            problem.setHeuristic(heuristic);
            heuristic.setProblem(problem);
        }
        long start = System.currentTimeMillis();
        solution = searchMethod.search(problem); //procura atraves do searchmethod que temos selecionado (na interface grafica)
        long end = System.currentTimeMillis();
        searchMethod.getStatistics().setDuration(end - start);
        return solution;
    }

    public void executeSolution() {    
        for(Action action : solution.getActions()){ //para passar por todos os passos intermedios até chegar ao estado final
            environment.executeAction(action); //realiza a solução no estado inicial (environment)
        }
    }

    public boolean hasSolution() {
        return solution != null;
    }

    public void stop() {
        getSearchMethod().stop();
    }

    public boolean hasBeenStopped() {
        return getSearchMethod().hasBeenStopped();
    }

    public E getEnvironment() {
        return environment;
    }

    public void setEnvironment(E environment) {
        this.environment = environment;
    }

    public SearchMethod[] getSearchMethodsArray() {
        SearchMethod[] sm = new SearchMethod[searchMethods.size()];
        return searchMethods.toArray(sm);
    }

    public SearchMethod getSearchMethod() {
        return searchMethod;
    }

    public void setSearchMethod(SearchMethod searchMethod) {
        this.searchMethod = searchMethod;
    }

    public Heuristic[] getHeuristicsArray() {
        Heuristic[] sm = new Heuristic[heuristics.size()];
        return heuristics.toArray(sm);
    }

    public Heuristic getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(Heuristic heuristic) {
        this.heuristic = heuristic;
    }

    public String getSearchReport() {
        StringBuilder sb = new StringBuilder();
        sb.append(searchMethod + "\n");
        if (solution == null) {
            sb.append("No solution found\n");
        } else {
            sb.append("Solution cost: " + Double.toString(solution.getCost()) + "\n");
        }
        sb.append("Num of expanded nodes: " + searchMethod.getStatistics().numExpandedNodes + "\n"); //é onde descreve o comportamento
        sb.append("Max frontier size: " + searchMethod.getStatistics().maxFrontierSize + "\n");
        sb.append("Num of generated nodes: " + searchMethod.getStatistics().numGeneratedNodes+ "\n");
        sb.append("Total duration: " + searchMethod.getStatistics().getDurationInSeconds()+" seconds\n");

        return sb.toString();
    }
}
