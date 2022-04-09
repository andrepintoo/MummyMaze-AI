package mummymaze.src.agent;

import java.util.List;

public abstract class Problem <S extends State>{
    // esta classe vai ser estendida depois para os problemas concretos


    /*  Please, implement the Problem class, which describes a generic problem. A problem has an
    initial state. In order to avoid compilation errors, add also a heuristic (class Heuristic) as attribute;
    we will later deal with heuristics). There should also exist methods that:
        • Apply all the (valid) actions to some state and return the resulting states (executeActions() method);
        • Verify if some state is a goal state (isGoal() method);
        • Calculate the cost of a solution, which is basically a list of actions (computePathCost() method).
    */

    //TODO
    protected S initialState; //S é uma subclasse de State
    protected Heuristic heuristic;
    protected List<Action> actions; //ações possíveis (válidas e inválidas) - no puzzle de 8 são 4 ações possíveis

    public Problem(S initialState, List<Action> actions) {
        this.initialState = initialState;
        this.actions = actions;
    }

    //executa ações válidas para passar de um estado para o outro
    public abstract List<S> executeActions(S state); //recebe um estado sobre o qual vai executar as ações e devolve a lista de estados para onde é possivel transitar

    public abstract boolean isGoal(S state); //depende do problema --> abstract

                    //é a lista das ações que constitui o caminho
    protected double computePathCost(List<Action> path){//calcula a soma de todas as ações que custa ir do estado inicial até ao final
        double cost = 0;
        for (Action a: path) {
            cost += a.getCost();
        }
        return cost;               //vai ser igual para todos
    } //no eigthPuzzle, cada ação custa o mesmo (1) -> neste caso o custo vai ser igual ao tamanho da lista (1+1+1+1+...)

    public S getInitialState() {
        return initialState;
    }

    public Heuristic getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(Heuristic heuristic) {
        this.heuristic = heuristic;
    }
}
