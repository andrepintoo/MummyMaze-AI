package mummymaze;

import agent.Action;
import agent.State;
import eightpuzzle.EightPuzzleState;

import java.util.List;

public class MummyMazeState extends State implements Cloneable{

    private final char[][] matrix; //posições válidas : do [1][1] ao [1][11] (na horizontal) e [1][1] ao [11][1] (na vertical)
    private int columnExit;
    private int lineExit;
    private int lineHero; //variables to store where the hero is
    private int columnHero;

    private List<String> states;

    public MummyMazeState(char[][] matrix) { //será [13][13]
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

    public boolean canMoveRight(){
        int columnRigth = columnHero + 2;
        //verificar se não sai fora dos limites (line < 12 && line > 0 && column < 12 && column > 0)
        if(columnRigth > matrix.length - 2){
            return false;
        }
        //verificar se tem parede (| ou -), porta fechada (" ou =)
        return matrix[lineHero][columnRigth] != '|' && matrix[lineHero][columnRigth] != '-' && matrix[lineHero][columnRigth] != '=' && matrix[lineHero][columnRigth] != '"';
    }

    public boolean canMoveLeft(){
        //moves off limits?
        if(columnHero == 1){
            return false;
        }
        int columnLeft = columnHero - 2;
        //has something blocking hero's path?
        return matrix[lineHero][columnLeft] != '|' && matrix[lineHero][columnLeft] != '-' && matrix[lineHero][columnLeft] != '=' && matrix[lineHero][columnLeft] != '"';
    }

    public boolean canMoveUp(){
        //moves off limits?
        if(lineHero == 1){
            return false;
        }
        int lineUp = lineHero - 2;
        //has something blocking hero's path?
        return matrix[lineUp][columnHero] != '|' && matrix[lineUp][columnHero] != '-' && matrix[lineUp][columnHero] != '=' && matrix[lineUp][columnHero] != '"';
    }

    public boolean canMoveDown(){
        //moves off limits?
        if(lineHero == matrix.length - 2){
            return false;
        }
        int lineDown = lineHero + 2;
        //has something blocking hero's path?
        return matrix[lineDown][columnHero] != '|' && matrix[lineDown][columnHero] != '-' && matrix[lineDown][columnHero] != '=' && matrix[lineDown][columnHero] != '"';
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
