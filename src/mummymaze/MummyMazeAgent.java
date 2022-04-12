package mummymaze;
import agent.Agent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MummyMazeAgent extends Agent<MummyMazeState> {

    protected MummyMazeState initialEnvironment;

    public MummyMazeAgent(MummyMazeState environment) {
        super(environment);
        initialEnvironment = (MummyMazeState) environment.clone();
        heuristics.add(new HeuristicExitDistance());
        heuristic = heuristics.get(0);
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
 /*java.util.Scanner scanner = new java.util.Scanner(file);

        char[][] matrix = new char [13][13];
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 13; j++) {
                    matrix[i][j] = (char) scanner.nextByte();
            }
            scanner.nextLine();
        }
*/
        // String -> Matriz
        /*
        int i=0, j=0;
        char matrix[][] = new char[13][13];
        for (char t:  scanner.toString().toCharArray()){
            if(t!='\n') {
                matrix[i][j] = t;
                j++;
            }else{
                j=0;
                i++;
            }
        }
*/
        initialEnvironment = new MummyMazeState(matrix);
        resetEnvironment();
        return environment;
    }

    public MummyMazeState setInitialStateFromFile(File file){
        try{
            return readInitialStateFromFile(file);
        }catch (IOException e){
            e.printStackTrace(System.err);
        }

        return null;
    }

}
