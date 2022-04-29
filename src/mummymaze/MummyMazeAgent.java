package mummymaze;
import agent.Action;
import agent.Agent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MummyMazeAgent extends Agent<MummyMazeState> {

    protected MummyMazeState initialEnvironment;
    private List<String> movements;
    private double solutionCost;

    public MummyMazeAgent(MummyMazeState environment) {
        super(environment);
        initialEnvironment = (MummyMazeState) environment.clone();
        heuristics.add(new HeuristicExitDistance());
        heuristic = heuristics.get(0);
        this.movements = new ArrayList<>();
    }

    public MummyMazeState resetEnvironment(){
        environment = (MummyMazeState) initialEnvironment.clone();

        return environment;
    }

    public MummyMazeState readInitialStateFromFile(File file) throws IOException {
        java.util.Scanner scanner = new java.util.Scanner(file).useDelimiter("\r\n");
        List<String> stringList = new ArrayList<>();
        String str;
        while (scanner.hasNext()){
            str = scanner.nextLine();
            stringList.add(str);
        }

        char[][] matrix = new char [13][13];
        int i = 0;
        for (String s: stringList) {
            for (int k = 0; k < s.length(); k++) {
                matrix[i][k] = s.toCharArray()[k];
            }
            i++;
        }

        initialEnvironment = new MummyMazeState(matrix);
        return resetEnvironment();
        //return environment;
    }

    public MummyMazeState setInitialStateFromFile(File file){
        try{
            return readInitialStateFromFile(file);
        }catch (IOException e){
            e.printStackTrace(System.err);
        }

        return null;
    }

    @Override
    public void executeSolution() {
        // Definir a lista de turnos
        //super.executeSolution();
        this.movements.clear();
        for(Action action : solution.getActions()){ //para passar por todos os passos intermedios até chegar ao estado final
            environment.executeAction(action);
            List<String> actionMovements = action.getMovements();
            for (String movement: actionMovements) {
                this.movements.add(movement);
            }

            //definir o custo da solução
            solutionCost = solution.getCost();
        }
    }

    public List<String> getMovements() {
        return movements;
    }

    public double getSolutionCost() {
        return solutionCost;
    }
}
