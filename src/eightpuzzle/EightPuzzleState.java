package eightpuzzle;

import agent.Action;
import agent.State;
import java.util.ArrayList;
import java.util.Arrays;

public class EightPuzzleState extends State implements Cloneable {

    static final int[][] GOAL_MATRIX = {{0, 1, 2},
                                       {3, 4, 5},
                                       {6, 7, 8}}; //o estado objetivo está aqui "hard coded"

    //cada indice é uma ->peça:     0  1  2  3  4  5  6  7  8 (se não tinha que se ter 2 ciclos for encadeados)
    final int[] linesfinalMatrix = {0, 0, 0, 1, 1, 1, 2, 2, 2}; //servem para a heuristica do tile distance
    final int[] colsfinalMatrix = {0, 1, 2, 0, 1, 2, 0, 1, 2}; //servem para a heuristica do tile distance

    public static final int SIZE = 3; //tamanho da matrix (3x3)
    private final int[][] matrix;
    private int lineBlank; //vars. auxiliares onde guarda onde está a PEÇA BRANCA
    private int columnBlank;

    public EightPuzzleState(int[][] matrix) {
        this.matrix = new int[matrix.length][matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                this.matrix[i][j] = matrix[i][j];
                if (this.matrix[i][j] == 0) { //guarda a info onde está a peça branca
                    lineBlank = i;
                    columnBlank = j;
                }
            }
        }
    }

    @Override
    public void executeAction(Action action) {
        action.execute(this); //método polimórfico
        firePuzzleChanged(null); //para atualizar a interface gráfica
    }

    public boolean canMoveUp() {
        return lineBlank != 0;
    }

    public boolean canMoveRight() {
        return columnBlank != matrix.length - 1;
    }

    public boolean canMoveDown() {
        return lineBlank != matrix.length - 1;
    }

    public boolean canMoveLeft() {
        return columnBlank != 0;
    }

    /*
     * In the next four methods we don't verify if the actions are valid.
     * This is done in method executeActions in class EightPuzzleProblem.
     * Doing the verification in these methods would imply that a clone of the
     * state was created whether the operation could be executed or not.
     */
    public void moveUp() {
        matrix[lineBlank][columnBlank] = matrix[--lineBlank][columnBlank];
        matrix[lineBlank][columnBlank] = 0;
    }

    public void moveRight() {
        matrix[lineBlank][columnBlank] = matrix[lineBlank][++columnBlank];
        matrix[lineBlank][columnBlank] = 0;
    }

    public void moveDown() {
        matrix[lineBlank][columnBlank] = matrix[++lineBlank][columnBlank];
        matrix[lineBlank][columnBlank] = 0;
    }

    public void moveLeft() {
        matrix[lineBlank][columnBlank] = matrix[lineBlank][--columnBlank];
        matrix[lineBlank][columnBlank] = 0;
    }

    public double computeTilesOutOfPlace(EightPuzzleState finalState) {

        //TODO
        // returns the number of tiles that are not where they should be
        //2 ciclos FOR encadeados, comparar este estado com o estado objetivo
        double tilesOutOfPlace = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if(matrix[i][j] != 0) {
                    if (matrix[i][j] != finalState.matrix[i][j]) {
                        tilesOutOfPlace++;
                    }
                }
            }
        }
        return tilesOutOfPlace;
    }

    public double computeTileDistances(EightPuzzleState finalState) {

        //TODO
        // sum of manhattan distances between where the tiles are and where they should be

        double tilesDistance = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                //diferença absoluta entre a linha onde está e a linha onde devia estar
                // + a diferença absoluta entre a coluna onde está e a coluna onde devia estar
                if(matrix[i][j] != 0){
                    tilesDistance = Math.abs(i - linesfinalMatrix[matrix[i][j]]) + Math.abs(j - colsfinalMatrix[matrix[i][j]]);
                    //pq pode dar um valor negativo, mas quero sempre a distancia
                }

            }
        }

        return tilesDistance;


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

    @Override
    public boolean equals(Object other) { //propriedades têm que ter os mesmos valores
        if (!(other instanceof EightPuzzleState)) {
            return false;
        }

        EightPuzzleState o = (EightPuzzleState) other;
        if (matrix.length != o.matrix.length) {
            return false;
        }

        return Arrays.deepEquals(matrix, o.matrix); //compara as posições uma a uma
    }

    @Override
    public int hashCode() {  //cria HASH -> serve para depois ter hash table ...
        return 97 * 7 + Arrays.deepHashCode(this.matrix); //o que importa é que o hash do objeto corresponda à hash da matriz
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < matrix.length; i++) {
            buffer.append('\n');
            for (int j = 0; j < matrix.length; j++) {
                buffer.append(matrix[i][j]);
                buffer.append(' ');
            }
        }
        return buffer.toString();
    }

    @Override
    public Object clone() {
        return new EightPuzzleState(matrix);
    }
    //Listeners --> para atualizar interface gráfica
    private transient ArrayList<EightPuzzleListener> listeners = new ArrayList<EightPuzzleListener>(3);

    public synchronized void removeListener(EightPuzzleListener l) {
        if (listeners != null && listeners.contains(l)) {
            listeners.remove(l);
        }
    }

    public synchronized void addListener(EightPuzzleListener l) {
        if (!listeners.contains(l)) {
            listeners.add(l);
        }
    }

    public void firePuzzleChanged(EightPuzzleEvent pe) {
        for (EightPuzzleListener listener : listeners) {
            listener.puzzleChanged(null);
        }
    }
}
