package mummymaze;

import agent.Action;
import agent.State;

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
    private int[] lineRedMummies = new int[1];
    private int[] columnRedMummies = new int[1];
    private int whiteMummies = 0;
    private int redMummies = 0;
    private boolean gameOver = false;

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
                    if(whiteMummies != 0) {
                        lineWhiteMummies = Arrays.copyOf(lineWhiteMummies, whiteMummies);
                        columnWhiteMummies = Arrays.copyOf(columnWhiteMummies, whiteMummies);
                    }
                    lineWhiteMummies[whiteMummies] = i;
                    columnWhiteMummies[whiteMummies] = j;
                    whiteMummies++;
                }

                if (this.matrix[i][j] == 'V'){ //stores the red mummy's position in the matrix
                    if(redMummies != 0) {
                        lineRedMummies = Arrays.copyOf(lineRedMummies, redMummies);
                        columnRedMummies = Arrays.copyOf(columnRedMummies, redMummies);
                    }
                    lineRedMummies[redMummies] = i;
                    columnRedMummies[redMummies] = j;
                    redMummies++;
                }
            }
        }
    }

    @Override
    public void executeAction(Action action) {
        action.resetMovements();
        action.execute(this);
        firePuzzleChanged(); //para atualizar a interface gráfica
    }


    public boolean isGameOver() {
        return gameOver;
    }

    private void moveWhiteMummy(int pos, List<String> movements) {
        int lineMummy = lineWhiteMummies[pos];
        int columnMummy = columnWhiteMummies[pos];

        for (int i = 0; i < 2; i++) {
            if(hasKilledHero(lineMummy, columnMummy)){
                break;
            }
            // Colunas primeiro
            if (columnMummy != columnHero) {
                if (columnHero > columnMummy) {
                    //Right
                    if (canMoveRight(lineMummy, columnMummy)) {
                        matrix[lineMummy][columnMummy] = matrix[lineMummy][columnMummy+=2];
                        matrix[lineMummy][columnMummy] = 'M';

                        movements.add(convertMatrixToString(matrix));
                        columnWhiteMummies[pos] = columnMummy;
                        continue;
                    }
                } else {
                    //Left
                    if (canMoveLeft(lineMummy, columnMummy)) {
                        matrix[lineMummy][columnMummy] = matrix[lineMummy][columnMummy-=2];
                        matrix[lineMummy][columnMummy] = 'M';

                        movements.add(convertMatrixToString(matrix));
                        columnWhiteMummies[pos] = columnMummy;
                        continue;
                    }
                }

            }
            if (lineHero != lineMummy) {
                if (lineHero > lineMummy) {
                    //Down
                    if (canMoveDown(lineMummy, columnMummy)) {
                        matrix[lineMummy][columnMummy] = matrix[lineMummy+=2][columnMummy];
                        matrix[lineMummy][columnMummy] = 'M';

                        movements.add(convertMatrixToString(matrix));
                        lineWhiteMummies[pos] = lineMummy;

                    }
                } else {
                    //Up
                    if (canMoveUp(lineMummy, columnMummy)) {
                        matrix[lineMummy][columnMummy] = matrix[lineMummy-=2][columnMummy];
                        matrix[lineMummy][columnMummy] = 'M';

                        movements.add(convertMatrixToString(matrix));
                        lineWhiteMummies[pos] = lineMummy;
                    }
                }
            }
        }
    }

    private void moveRedMummy(int pos, List<String> movements) {
        int lineMummy = lineRedMummies[pos];
        int columnMummy = columnRedMummies[pos];

        for (int i = 0; i < 2; i++) {
            if(hasKilledHero(lineMummy, columnMummy)){
                break;
            }
            //linhas primeiro
            if (lineHero != lineMummy) {
                if (lineHero > lineMummy) {
                    //Down
                    if (canMoveDown(lineMummy, columnMummy)) {
                        matrix[lineMummy][columnMummy] = matrix[lineMummy+=2][columnMummy];
                        matrix[lineMummy][columnMummy] = 'V';

                        movements.add(convertMatrixToString(matrix));
                        lineRedMummies[pos] = lineMummy;
                        continue;
                    }
                } else {
                    //Up
                    if (canMoveUp(lineMummy, columnMummy)) {
                        matrix[lineMummy][columnMummy] = matrix[lineMummy-=2][columnMummy];
                        matrix[lineMummy][columnMummy] = 'V';

                        movements.add(convertMatrixToString(matrix));
                        lineRedMummies[pos] = lineMummy;
                        continue;
                    }
                }
            }

            if (columnMummy != columnHero) {
                if (columnHero > columnMummy) {
                    //Right
                    if (canMoveRight(lineMummy, columnMummy)) {
                        matrix[lineMummy][columnMummy] = matrix[lineMummy][columnMummy+=2];
                        matrix[lineMummy][columnMummy] = 'V';

                        movements.add(convertMatrixToString(matrix));
                        columnRedMummies[pos] = columnMummy;

                    }
                } else {
                    //Left
                    if (canMoveLeft(lineMummy, columnMummy)) {
                        matrix[lineMummy][columnMummy] = matrix[lineMummy][columnMummy-=2];
                        matrix[lineMummy][columnMummy] = 'V';

                        movements.add(convertMatrixToString(matrix));
                        columnRedMummies[pos] = columnMummy;
                    }
                }
            }
        }
    }

    private boolean hasKilledHero(int lineEnemy, int columnEnemy) {
        if((canMoveUp(lineEnemy, columnEnemy) && matrix[lineEnemy-2][columnEnemy] == 'H') ||
            (canMoveDown(lineEnemy, columnEnemy) && matrix[lineEnemy+2][columnEnemy] == 'H') ||
            (canMoveLeft(lineEnemy, columnEnemy) && matrix[lineEnemy][columnEnemy-2] == 'H') ||
            (canMoveRight(lineEnemy, columnEnemy) && matrix[lineEnemy][columnEnemy+2] == 'H')){
            gameOver = true;
        }
        return gameOver;
    }

    public List<String> moveUp() {
        List<String> movements = new ArrayList<>();
        // Hero's movement
        int newLine = lineHero-2;
        if(matrix[newLine][columnHero] == 'A'){
            gameOver = true;
        }
        matrix[lineHero][columnHero] = matrix[lineHero-=2][columnHero];
        matrix[lineHero][columnHero] = 'H';

        movements.add(convertMatrixToString(matrix)); //NOVA STRING RESULTANTE DO MOVIMENTO DO HEROI

        for (int whiteM = 0; whiteM < whiteMummies; whiteM++) {
            moveWhiteMummy(whiteM, movements);
        }

        for(int redM = 0; redM < redMummies; redM++){
            moveRedMummy(redM, movements);
        }

        return movements;
    }


    public List<String> moveRight() {
        List<String> movements = new ArrayList<>();
        // Hero's movement
        int newColumn = columnHero+2;
        if(matrix[lineHero][newColumn] == 'A'){
            gameOver = true;
        }
        matrix[lineHero][columnHero] = matrix[lineHero][columnHero+=2];
        matrix[lineHero][columnHero] = 'H';

        movements.add(convertMatrixToString(matrix));

        for (int whiteM = 0; whiteM < whiteMummies; whiteM++) {
            moveWhiteMummy(whiteM, movements);
        }

        for(int redM = 0; redM < redMummies; redM++){
            moveRedMummy(redM, movements);
        }

        return movements;
    }

    public List<String> moveDown() {
        List<String> movements = new ArrayList<>();
        // Hero's movement
        int newLine = lineHero+2;
        if(matrix[newLine][columnHero] == 'A'){
            gameOver = true;
        }
        matrix[lineHero][columnHero] = matrix[lineHero+=2][columnHero];
        matrix[lineHero][columnHero] = 'H';

        movements.add(convertMatrixToString(matrix));

        for (int whiteM = 0; whiteM < whiteMummies; whiteM++) {
            moveWhiteMummy(whiteM, movements);
        }

        for(int redM = 0; redM < redMummies; redM++){
            moveRedMummy(redM, movements);
        }

        return movements;
    }

    public List<String> moveLeft() {
        List<String> movements = new ArrayList<>();
        // Hero's movement
        int newColumn = columnHero-2;
        if(matrix[lineHero][newColumn] == 'A'){
            gameOver = true;
        }
        matrix[lineHero][columnHero] = matrix[lineHero][columnHero-=2];
        matrix[lineHero][columnHero] = 'H';

        movements.add(convertMatrixToString(matrix));

        for (int whiteM = 0; whiteM < whiteMummies; whiteM++) {
            moveWhiteMummy(whiteM, movements);
        }

        for(int redM = 0; redM < redMummies; redM++){
            moveRedMummy(redM, movements);
        }

        return movements;
    }

    public List<String> moveStandStill(){
        List<String> movements = new ArrayList<>();

        for (int whiteM = 0; whiteM < whiteMummies; whiteM++) {
            moveWhiteMummy(whiteM, movements);
        }

        for(int redM = 0; redM < redMummies; redM++){
            moveRedMummy(redM, movements);
        }

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
        return canMoveRight(lineHero, columnHero);
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
        return canMoveLeft(lineHero, columnHero);
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
        return canMoveUp(lineHero, columnHero);
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
        return canMoveDown(lineHero, columnHero);
    }

    public double computeExitDistance() {
        return ((Math.abs(lineHero - lineExit) + Math.abs(columnHero - columnExit))-1)/ (double) 2;
    }

    public String convertMatrixToString(char[][] matrix) {
        // Matriz -> String
        StringBuilder s= new StringBuilder();
        for (int k = 0; k < 13; k++) {
            s.append(String.valueOf(matrix[k])).append("\n");
        }

        return s.toString();
    }

    public String getStateString() {
        // Matriz -> String
        StringBuilder s= new StringBuilder();
        for (int k = 0; k < 13; k++) {
            s.append(String.valueOf(matrix[k])).append("\n");
        }

        return s.toString();
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

    private final transient ArrayList<MummyMazeListener> listeners = new ArrayList<>(3);

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

    public void firePuzzleChanged() {
        for (MummyMazeListener listener : listeners) {
            listener.puzzleChanged(null);
        }
    }
}
