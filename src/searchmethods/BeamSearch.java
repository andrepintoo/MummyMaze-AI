package searchmethods;

import agent.State;
import java.util.List;
import utils.NodePriorityQueue;

public class BeamSearch extends AStarSearch {

    private int beamSize;

    public BeamSearch() {
        this(100);
    }

    public BeamSearch(int beamSize) {
        this.beamSize = beamSize;
    }

    @Override
    public void addSuccessorsToFrontier(List<State> successors, Node parent) {
        super.addSuccessorsToFrontier(successors,parent);

        //TODO
        // elimina os piores da fronteira (i.e., os com menor prioridade) até a fronteira ficar com tamanho beamSize (é um parametro definido arbitrariamente)
        // beamSize serve de limite

        if(frontier.size() > beamSize){
            NodePriorityQueue melhores = new NodePriorityQueue();
            for (int i = 0; i < beamSize; i++) {
                melhores.add(frontier.remove());
            }
            frontier = melhores;
        }
    }

    public void setBeamSize(int beamSize) {
        this.beamSize = beamSize;
    }

    public int getBeamSize() {
        return beamSize;
    }

    @Override
    public String toString() {
        return "Beam search";
    }
}
