package mummymaze;

import agent.Action;
import agent.State;
import eightpuzzle.EightPuzzleState;

import java.util.List;

public class MummyMazeState extends State implements Cloneable{

    private final char[][] matrix;
    private int columnExit;
    private int lineExit;
    private int lineHero; //variables to store where the hero is
    private int columnHero;

    private List<String> states;

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


    @Override
    public void executeAction(Action action) {
        action.execute(this);
        //firePuzzleChanged(null); //para atualizar a interface gráfica
    }

    public void moveUp() {
        matrix[lineHero][columnHero] = matrix[lineHero-=2][columnHero];
        matrix[lineHero][columnHero] = 'H';

        //NOVA STRING RESULTANTE DO MOVIMENTO DO HEROI
        //
        //METODO POSSIVEIS MOVIMENTOS DA MUMIA (ALTERA A MATRIZ DESTE ESTADO)
        //NOVAS STRINGS DOS MOVIMENTOS DA MUMIA

        states.add(convertMatrixToString(matrix));

        //TODO - moveMummy()
    }

    public void moveRight() {
        matrix[lineHero][columnHero] = matrix[lineHero][columnHero+=2];
        matrix[lineHero][columnHero] = 'H';

        states.add(convertMatrixToString(matrix));
    }

    public void moveDown() {
        matrix[lineHero][columnHero] = matrix[lineHero+=2][columnHero];
        matrix[lineHero][columnHero] = 'H';

        states.add(convertMatrixToString(matrix));
    }

    public void moveLeft() {
        matrix[lineHero][columnHero] = matrix[lineHero][columnHero-=2];
        matrix[lineHero][columnHero] = 'H';

        states.add(convertMatrixToString(matrix));
    }

    public void moveStandStill(){
        //ações da mumia com states.add(convertMatrixToString(matrix)); respetivo
    }



    public boolean canMoveLeft(){
        //has something blocking hero's path?
        if(matrix[lineHero][columnHero] == '|' || matrix[lineHero][columnHero] == '-' || matrix[lineHero][columnHero] == '=' || matrix[lineHero][columnHero] == '"'){
            return false;
        }
        //moves off limits?
        if(columnHero == matrix.length - 1){
            return false;
        }
        return true;
    }
    public boolean canMoveUp(){
        //has something blocking hero's path?
        if(matrix[lineHero][columnHero] == '|' || matrix[lineHero][columnHero] == '-' || matrix[lineHero][columnHero] == '=' || matrix[lineHero][columnHero] == '"'){
            return false;
        }
        //moves off limits?
        if(lineHero == 1){
            return false;
        }
        return true;
    }
    public boolean canMoveDown(){
        //has something blocking hero's path?
        if(matrix[lineHero][columnHero] == '|' || matrix[lineHero][columnHero] == '-' || matrix[lineHero][columnHero] == '=' || matrix[lineHero][columnHero] == '"'){
            return false;
        }
        //moves off limits?
        if(columnHero == matrix.length - 2){
            return false;
        }
        return true;
    }

    public double computeExitDistance() {
        return ((Math.abs(lineHero - lineExit) + Math.abs(columnHero - columnExit))-1)/2;
    }

    private String convertMatrixToString(char[][] matrix) {
        // Matriz -> String
        String s="";
        for (int k = 0; k < 13; k++) {
            s+=String.valueOf(matrix[k])+"\n";
        }

        return s;
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
