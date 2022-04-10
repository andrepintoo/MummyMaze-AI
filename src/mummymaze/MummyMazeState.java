package mummymaze;

import agent.Action;
import agent.State;
import eightpuzzle.EightPuzzleState;

public class MummyMazeState extends State implements Cloneable{

    private final char[][] matrix;
    private int columnExit;
    private int lineExit;
    private int lineHero; //variables to store where the hero is
    private int columnHero;

    public MummyMazeState(char[][] matrix) {
        this.matrix = new char[matrix.length][matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                this.matrix[i][j] = matrix[i][j];
                if (this.matrix[i][j] == 'H') { //stores the hero's position in the matrix
                    lineHero = i;
                    columnHero = j;
                }
                if (this.matrix[i][j] == 'S'){ //stores the exit position in the matrix
                    lineExit = i;
                    columnExit = j;
                }
            }
        }
    }

    public int[] getExitPosition(){
        int[] exit = new int[2];
        exit[0] = lineExit - 2;
        exit[1] = columnExit - 2;
        return exit;
    }

    public int getColumnExit() {
        return columnExit - 2;
    }

    public int getLineExit() {
        return lineExit - 2;
    }

    @Override
    public void executeAction(Action action) {
        action.execute(this);
        //firePuzzleChanged(null); //para atualizar a interface gráfica
    }

    public void moveUp() { //TODO estes moves ainda nao estao bem, pq por cada movimento penso que se mexe + que 1 casa (por causa do espaçamento das paredes...)
        matrix[lineHero][columnHero] = matrix[lineHero-=2][columnHero];
        matrix[lineHero][columnHero] = 'H';
    }

    public void moveRight() {
        matrix[lineHero][columnHero] = matrix[lineHero][columnHero+=2];
        matrix[lineHero][columnHero] = 'H';
    }

    public void moveDown() {
        matrix[lineHero][columnHero] = matrix[lineHero+=2][columnHero];
        matrix[lineHero][columnHero] = 'H';
    }

    public void moveLeft() {
        matrix[lineHero][columnHero] = matrix[lineHero][columnHero-=2];
        matrix[lineHero][columnHero] = 'H';
    }

    public double computeExitDistance() {
        return ((Math.abs(lineHero - lineExit) + Math.abs(columnHero - columnExit))-1)/2;
    }

    @Override
    public Object clone() {
        return new MummyMazeState(matrix);
    }

    public int getNumLines() {
        return matrix.length;
    }

    public int getNumColumns() {
        return matrix[0].length;
    }
}