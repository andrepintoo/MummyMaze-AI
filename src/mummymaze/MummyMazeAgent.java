package mummymaze;
import agent.Agent;

import java.io.File;
import java.io.IOException;

public class MummyMazeAgent extends Agent<MummyMazeState> {

    protected MummyMazeState initialEnvironment;

    public MummyMazeAgent(MummyMazeState environment) {
        super(environment);
        initialEnvironment = (MummyMazeState) environment.clone();
    }

    public MummyMazeState resetEnvironment(){
        environment = (MummyMazeState) initialEnvironment.clone();
        return environment;
    }

    public MummyMazeState readInitialStateFromFile(File file) throws IOException {
        java.util.Scanner scanner = new java.util.Scanner(file);

        /*char[][] matrix = new char [13][13];
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 13; j++) {
                matrix[i][j] = (char) scanner.nextByte();
            }
            scanner.nextLine();
        }*/

        // String -> Matriz
        int i=0, j=0;
        char matrix[][] = new char[13][13];
        for (char t:  scanner.nextLine().toCharArray()){
            if(t!='\n') {
                matrix[i][j] = t;
                j++;
            }else{
                j=0;
                i++;
            }
        }

        initialEnvironment = new MummyMazeState(matrix);
        resetEnvironment();
        return environment;
    }

}
