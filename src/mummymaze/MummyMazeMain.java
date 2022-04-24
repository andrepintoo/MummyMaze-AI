package mummymaze;

import showSolution.SolutionPanel;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MummyMazeMain {
    private static final String FILE_PATH = "nivel1.txt";

    public static void main(String[] args) throws IOException {
        //Instanciar o MummyMazeAgent
//        MummyMazeAgent agent = new MummyMazeAgent(null);
        MummyMazeAgent agent = new MummyMazeAgent(new MummyMazeState(new char[13][13]));

        //Ler o estado inicial de um ficheiro
        MummyMazeState state = agent.readInitialStateFromFile(new File(FILE_PATH));

        //Instanciar o problem
        MummyMazeProblem problem = new MummyMazeProblem(state);

        //executar algoitmo de procura para obter a solução
        agent.solveProblem(problem);

        //executar a solução = obter a lista de turnos
        agent.executeSolution();

        //obter a lista de turnos correspondente à solução
        List<String> movements = agent.getMovements();
        double cost = agent.getSolutionCost();

        // mostrar a lista de turnos na interface gráfica fornecida
        SolutionPanel.showSolution(movements, cost);
    }
}
