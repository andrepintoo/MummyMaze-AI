package mummymaze;

import agent.Action;
import agent.State;
import eightpuzzle.EightPuzzleEvent;
import eightpuzzle.EightPuzzleListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MummyMazeState extends State implements Cloneable{

    private final char[][] matrix; //posições válidas : do [1][1] ao [1][11] (na horizontal) e [1][1] ao [11][1] (na vertical)
    private int columnExit;
    private int lineExit;
    private int lineHero; //variables to store where the hero is
    private int columnHero;
    private int[] lineWhiteMummies = new int[1];
    private int[] columnWhiteMummies = new int[1];
    private int whiteMummies = 0;

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
                if (this.matrix[i][j] == 'M'){ //stores the white mummy's position in the matrix
                    lineWhiteMummies = Arrays.copyOf(lineWhiteMummies, whiteMummies);
                    columnWhiteMummies = Arrays.copyOf(columnWhiteMummies, whiteMummies);
                    lineWhiteMummies[whiteMummies] = i;
                    columnWhiteMummies[whiteMummies] = j;
                    whiteMummies++;
                }
            }
        }
    }
    //

    @Override
    public void executeAction(Action action) {
        action.resetMovements();
        action.execute(this);
        firePuzzleChanged(null); //para atualizar a interface gráfica
    }

    public List<String> moveUp() {
        List<String> movements = new ArrayList<>();
        // Hero's movement
        matrix[lineHero][columnHero] = matrix[lineHero-=2][columnHero];
        matrix[lineHero][columnHero] = 'H';

        movements.add(convertMatrixToString(matrix)); //NOVA STRING RESULTANTE DO MOVIMENTO DO HEROI

        //TODO - moveMummy()
        for (int whiteM = 0; whiteM < whiteMummies; whiteM++) {
            moveWhiteMummy();
        }

        return movements;
    }

    private void moveWhiteMummy() {

    }

    public List<String> moveRight() {
        List<String> movements = new ArrayList<>();
        // Hero's movement
        matrix[lineHero][columnHero] = matrix[lineHero][columnHero+=2];
        matrix[lineHero][columnHero] = 'H';

        movements.add(convertMatrixToString(matrix));

        //TODO - moveMummy()

        return movements;
    }

    public List<String> moveDown() {
        List<String> movements = new ArrayList<>();
        // Hero's movement
        matrix[lineHero][columnHero] = matrix[lineHero+=2][columnHero];
        matrix[lineHero][columnHero] = 'H';

        movements.add(convertMatrixToString(matrix));

        //TODO - moveMummy()

        return movements;
    }

    public List<String> moveLeft() {
        List<String> movements = new ArrayList<>();
        // Hero's movement
        matrix[lineHero][columnHero] = matrix[lineHero][columnHero-=2];
        matrix[lineHero][columnHero] = 'H';

        movements.add(convertMatrixToString(matrix));

        //TODO - moveMummy()

        return movements;
    }

    public List<String> moveStandStill(){
        List<String> movements = new ArrayList<>();

        //TODO - moveMummy()

        return movements;
    }

    public boolean canMoveRight(int lineEntity, int columnEntity){

        //verificar se não sai fora dos limites (line < 12 && line > 0 && column < 12 && column > 0)
        if(columnEntity == matrix.length - 2){
            return false;
        }
        int columnRigth = columnEntity + 1;
        //verificar se tem parede (| ou -), porta fechada (" ou =)
        return matrix[lineEntity][columnRigth] != '|' && matrix[lineEntity][columnRigth] != '-' &&
                matrix[lineEntity][columnRigth] != '=' && matrix[lineEntity][columnRigth] != '"';
    }

    public boolean canMoveRightHero(){
        int columnRigth = columnHero + 1;
        return canMoveRight(lineHero, columnHero) && matrix[lineHero][columnRigth] != 'A';
    }

    public boolean canMoveLeft(int lineEntity, int columnEntity){
        //moves off limits?
        if(columnEntity == 1){
            return false;
        }
        int columnLeft = columnEntity - 1;
        //has something blocking hero's path?
        return matrix[lineEntity][columnLeft] != '|' && matrix[lineEntity][columnLeft] != '-' &&
                matrix[lineEntity][columnLeft] != '=' && matrix[lineEntity][columnLeft] != '"';
    }

    public boolean canMoveLeftHero(){
        int columnLeft = columnHero + 1;
        return canMoveLeft(lineHero, columnHero) && matrix[lineHero][columnLeft] != 'A';
    }

    public boolean canMoveUp(int lineEntity, int columnEntity){
        //moves off limits?
        if(lineEntity == 1){
            return false;
        }
        int lineUp = lineEntity - 1;
        //has something blocking hero's path?
        return matrix[lineUp][columnEntity] != '|' && matrix[lineUp][columnEntity] != '-' &&
                matrix[lineUp][columnEntity] != '=' && matrix[lineUp][columnEntity] != '"';
    }

    public boolean canMoveUpHero(){
        int lineUp = lineHero - 1;
        return canMoveUp(lineHero, columnHero) && matrix[lineUp][columnHero] != 'A';
    }

    public boolean canMoveDown(int lineEntity, int columnEntity){
        //moves off limits?
        if(lineEntity == matrix.length - 2){
            return false;
        }
        int lineDown = lineEntity + 1;
        //has something blocking hero's path?
        return matrix[lineDown][columnEntity] != '|' && matrix[lineDown][columnEntity] != '-' &&
                matrix[lineDown][columnEntity] != '=' && matrix[lineDown][columnEntity] != '"';
    }

    public boolean canMoveDownHero(){
        int lineDown = lineHero + 1;
        return canMoveDown(lineHero, columnHero) && matrix[lineDown][columnHero] != 'A';
    }

    public double computeExitDistance() {
        return ((Math.abs(lineHero - lineExit) + Math.abs(columnHero - columnExit))-1)/ (double) 2;
    }

    public String convertMatrixToString(char[][] matrix) {
        // Matriz -> String
        String s="";
        for (int k = 0; k < 13; k++) {
            s+=String.valueOf(matrix[k])+"\n";
        }

        return s;
    }

    public String getStateString() {
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

    public int getTileValue(int line, int column) {
        if (!isValidPosition(line, column)) {
            throw new IndexOutOfBoundsException("Invalid position!");
        }
        return matrix[line][column];
    }

    public boolean isValidPosition(int line, int column) {
        return line >= 0 && line < matrix.length && column >= 0 && column < matrix[0].length;
    }

    private transient ArrayList<MummyMazeListener> listeners = new ArrayList<MummyMazeListener>(3);

    public synchronized void removeListener(MummyMazeListener l) {
        if (listeners != null && listeners.contains(l)) {
            listeners.remove(l);
        }
    }

    public synchronized void addListener(MummyMazeListener l) {
        if (!listeners.contains(l)) {
            listeners.add(l);
        }
    }

    public void firePuzzleChanged(MummyMazeEvent pe) {
        for (MummyMazeListener listener : listeners) {
            listener.puzzleChanged(null);
        }
    }
}
