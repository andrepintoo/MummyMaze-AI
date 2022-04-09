package mummymaze.src.mummymaze;

import agent.Action;
import agent.State;
import eightpuzzle.EightPuzzleState;

public class MummyMazeState extends State implements Cloneable{

    private final char[][] matrix;

    public MummyMazeState(char[][] matrix) {
        this.matrix = new char[matrix.length][matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                this.matrix[i][j] = matrix[i][j];
//                if (this.matrix[i][j] == 0) { //guarda a info onde está a peça branca
//                    lineBlank = i;
//                    columnBlank = j;
//                }
            }
        }
    }

    @Override
    public void executeAction(Action action) {
        action.execute(this);
        //firePuzzleChanged(null); //para atualizar a interface gráfica
    }

    @Override
    public Object clone() {
        return new MummyMazeState(matrix);
    }
}
