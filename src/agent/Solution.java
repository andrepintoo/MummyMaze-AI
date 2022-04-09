package agent;

import java.util.LinkedList;
import java.util.List;
import searchmethods.Node;

public class Solution {
    private final Problem problem;
    private final LinkedList<Action> actions; //ações que permitem passar do estado inicial para o estado final

    public Solution(Problem problem, Node goalNode){
        this.problem = problem;
        Node node = goalNode;
        actions = new LinkedList<>();
        while(node.getParent() != null){ //vai se perguntar a cada nó qual é o pai dele,até chegar ao estado inicial
            actions.addFirst(node.getState().getAction()); //temos que saber o caminho do inicio até ao fim, e lista ligada resultante vai ser a solução
            node = node.getParent();
        }        //vai colocar a sequência de ações que permite passar do estado inicial até ao estado final
    }

    public double getCost(){
        return problem.computePathCost(actions);
    }

    public List<Action> getActions(){
        return actions;
    }
}