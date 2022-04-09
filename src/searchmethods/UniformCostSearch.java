package searchmethods;

import agent.State;
import java.util.List;
import utils.NodePriorityQueue;

public class UniformCostSearch extends GraphSearch<NodePriorityQueue> {

    public UniformCostSearch(){
        frontier = new NodePriorityQueue();
    }    
    
    // f = g
    // priority = cost
    @Override
    public void addSuccessorsToFrontier(List<State> successors, Node parent) {

        //TODO
        // para cada nó sucessor
        //      obter o custo do nó (g)
        //          se o nó não estiver na fronteira
        //              se o nó não estiver nos explorados
        //                  acrescenta o nó à fronteira (com f=g -> prioridade é igual ao custo)
        //          senão (se o nó estiver na fronteira)
        //                  se este nó tem menor custo que o que está na fronteira
        //                      remover o nó que está na fronteira
        //                          acrescenta o nó à fronteira (com f=g)
        //                                              // pois pode haver casos em que encontra-se uma melhor solução para um nó que já está na fronteira mas que tem um custo maior
        for (State state: successors) {
            double g = parent.getG() + state.getAction().getCost(); //vê o custo do pai (o custo do pai vai desde o inicial até onde o estado pai está)  +  o custo para passar do pai para o estado em que estou
            if(!frontier.containsState(state)){
                if(!explored.contains(state)){
                    frontier.add(new Node(state, parent, g, g/*prioridade f=g*/)); //pois neste algoritmo, f=g (prioridade é o mesmo que o g)
                }
            }else{ //se está na fronteira, de certeza que ainda não está no conjunto de nós explorados
                double g2 = frontier.getNode(state).getG();
                if(g < g2){
                    frontier.removeNode(state); //remove o nó que tem este estado
                    frontier.add(new Node(state, parent, g, g));
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Uniform cost search";
    }
}
