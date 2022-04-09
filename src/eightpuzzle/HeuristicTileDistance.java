package eightpuzzle;

import agent.Heuristic;

public class HeuristicTileDistance extends Heuristic<EightPuzzleProblem, EightPuzzleState>{

    @Override
    public double compute(EightPuzzleState state){ //delega o calculo do custo da heuristica para o estado que é passado
        return state.computeTileDistances(problem.getGoalState());
    }
    
    @Override
    public String toString(){
        return "Tiles distance to final position";
    }
}