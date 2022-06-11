package mummymaze;

import agent.Action;
import agent.State;
import gui.MainFrame;

import java.util.*;

public class MummyMazeState extends State implements Cloneable{

    private final char[][] matrix; //posições válidas : do [1][1] ao [1][11] (na horizontal) e [1][1] ao [11][1] (na vertical)

    private Cell cellHero; //cell where the hero is
    private Cell cellExit;
    private Cell cellKey;

    private LinkedList<Enemy> cellEnemies;
    private LinkedList<Cell> cellDoors;
    private LinkedList<Cell> cellTraps;

    private boolean gameOver = false;

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public MummyMazeState(MummyMazeState m){
        this.matrix = new char[m.getNumLines()][m.getNumColumns()];
        for (int i = 0; i < m.getNumLines(); i++) {
            for (int j = 0; j < m.getNumColumns(); j++) {
                this.matrix[i][j] = m.matrix[i][j];
            }
        }

        if(m.cellHero != null){
            this.cellHero = (Cell) m.cellHero.clone();
        }
        if(m.cellExit != null){
            this.cellExit = (Cell) m.cellExit.clone();
        }

        if(m.cellKey != null){
            this.cellKey = (Cell) m.cellKey.clone();
        }
        this.cellEnemies = new LinkedList<>();
        this.cellTraps = new LinkedList<>();
        this.cellDoors = new LinkedList<>();
        for (Enemy e: m.cellEnemies) {
            if(e != null){
                if(e instanceof WhiteMummy){
                    this.cellEnemies.add((WhiteMummy) e.clone());
                }else if(e instanceof RedMummy){
                    this.cellEnemies.add((RedMummy) e.clone());
                }else if(e instanceof Scorpion){
                    this.cellEnemies.add((Scorpion) e.clone());
                }
            }
        }
        for (Cell d: m.cellDoors) {
            if(d != null){
                this.cellDoors.add((Cell) d.clone());
            }
        }
        for (Cell t: m.cellTraps) {
            if(t != null){
                this.cellTraps.add((Cell) t.clone());
            }
        }
    }

    public MummyMazeState(char[][] matrix) { //será [13][13]
        this.matrix = new char[matrix.length][matrix.length];
        cellEnemies = new LinkedList<>();
        cellDoors = new LinkedList<>();
        cellTraps = new LinkedList<>();


        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                this.matrix[i][j] = matrix[i][j];

                switch (matrix[i][j]) {
                    case 'H'://stores the hero's position in the matrix
                        cellHero = new Hero(i,j);
                        break;
                    case 'S': //stores the exit position in the matrix
                        cellExit = new Cell(i,j);
                        break;
                    case 'M':
                        cellEnemies.add(new WhiteMummy(i,j));
                        break;
                    case 'E':
                        cellEnemies.add(new Scorpion(i,j));
                        break;
                    case 'V':
                        cellEnemies.add(new RedMummy(i,j));
                        break;
                    case '=','_','"',')':
                        cellDoors.add(new Cell(i,j));
                        break;
                    case 'C':
                        cellKey = new Cell(i,j);
                        break;
                    case 'A':
                        cellTraps.add(new Cell(i,j));
                        break;
                }
            }
        }
    }

    @Override
    public void executeAction(Action action) {
        action.execute(this);
        if (MainFrame.SHOWSOLUTION) {
            firePuzzleChanged(); //para atualizar a interface gráfica
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Cell getCellHero(){
        return cellHero;
    }

    public void killEnemy(Enemy enemy){
        int i=0;
        for (Enemy e: cellEnemies) {
            if(enemy != e && enemy.getLine() == e.getLine() && enemy.getColumn() == e.getColumn()){
                cellEnemies.remove(i);
                return;
            }
            i++;
        }
    }

    public void verifySpecialCells(Cell cellEntity) {
        int lineEntity = cellEntity.getLine();
        int columnEntity = cellEntity.getColumn();

        //TODO mudei a ordem

        if(cellKey!=null && cellKey.equals(cellEntity)){ //experimentar mudar para cellEntity.getLine() == lineKey && cellEntity.getColumn() == columnKey para ver se entra aqui no nivel 22
            matrix[lineEntity][columnEntity] = 'C';
        }else{
            matrix[lineEntity][columnEntity] = '.';
        }

        for (Cell trap: cellTraps) {
            if(trap.equals(cellEntity)){
                matrix[lineEntity][columnEntity] = 'A';
            }
        }
    }

    public boolean hasKilledHero(int lineEnemy, int columnEnemy) {
        if(columnEnemy == cellHero.getColumn() && lineEnemy == cellHero.getLine()){
            gameOver = true;
        }
        return gameOver;
    }

    public char getPosition(int line, int column){
        return matrix[line][column];
    }

    public void updateSymbol(int line, int column, char character){
        matrix[line][column] = character;
    }

    public char[][] getMatrix() {
        return matrix;
    }

    private LinkedList<String> moveEnemies(){
        LinkedList<String> newMoves = new LinkedList<>();

        for (Enemy e : (LinkedList<Enemy>) cellEnemies.clone()){
            LinkedList<String> strings = e.moveEnemy(this);
            if(strings != null) {
                for (String s : strings) {
                    newMoves.add(s);
                }
            }
        }
        return newMoves;
    }

    public LinkedList<String> moveUp() {
        LinkedList<String> moves = new LinkedList<>();
        moves.add(((Hero) cellHero).moveUpSuper(this, 'H'));
        for (String s: moveEnemies()) {
            moves.add(s);
        }
        return moves;
    }

    public LinkedList<String> moveRight() {
        LinkedList<String> moves = new LinkedList<>();
        moves.add(((Hero) cellHero).moveRightSuper(this, 'H'));
        for (String s: moveEnemies()) {
            moves.add(s);
        }
        return moves;
    }

    public LinkedList<String> moveDown() {
        LinkedList<String> moves = new LinkedList<>();
        moves.add(((Hero) cellHero).moveDownSuper(this, 'H'));
        for (String s: moveEnemies()) {
            moves.add(s);
        }
        return moves;
    }

    public LinkedList<String> moveLeft() {

        LinkedList<String> moves = new LinkedList<>();
        moves.add(((Hero) cellHero).moveLeftSuper(this, 'H'));
        for (String s: moveEnemies()) {
            moves.add(s);
        }
        return moves;
    }

    public LinkedList<String> moveStandStill(){
        LinkedList<String> movements = new LinkedList<>();
        for (String s: moveEnemies()) {
            movements.add(s);
        }
        return movements;
    }

    public void changeDoorsState() {
        for (Cell d : cellDoors) {
            switch (matrix[d.getLine()][d.getColumn()]){
                case '=':
                    matrix[d.getLine()][d.getColumn()] = '_';
                    break;
                case '_':
                    matrix[d.getLine()][d.getColumn()] = '=';
                    break;
                case '"':
                    matrix[d.getLine()][d.getColumn()] = ')';
                    break;
                case ')':
                    matrix[d.getLine()][d.getColumn()] = '"';
                    break;
            }
        }
    }
    public boolean canMoveRightHero(){
        return !gameOver && ((Hero)cellHero).canMoveRight(this);
    }

    public boolean canMoveLeftHero(){
        return !gameOver && ((Hero)cellHero).canMoveLeft(this);
    }

    public boolean canMoveUpHero(){
        return !gameOver && ((Hero)cellHero).canMoveUp(this);
    }

    public boolean canMoveDownHero(){
        return !gameOver && ((Hero)cellHero).canMoveDown(this);
    }

    public double computeExitDistance() {
        return ((Math.abs(cellHero.getLine() - cellExit.getLine()) + Math.abs(cellHero.getColumn() - cellExit.getColumn()))-1)/ (double) 2;
    }

    public double computeDistanceToEnemies() {
        double distance = 0;
        for (Enemy e: cellEnemies) {
            distance += Math.abs(cellHero.getLine() - e.getLine()) + Math.abs(cellHero.getColumn() - e.getColumn());
        }
        return 1/distance;
    }

    public double computeWallsAroundEnemies() {
        double walls = 0;
        for (Enemy e: cellEnemies) {
            if(matrix[e.getLine()][e.getColumn()-1] == '|' ||
                    matrix[e.getLine()][e.getColumn()+1] == '|' ||
                    matrix[e.getLine()-1][e.getColumn()] == '-' ||
                    matrix[e.getLine()+1][e.getColumn()] == '-' ){
                walls++;
            }
        }
        return 1/walls;
    }

    public double computeNumberEnemies() {
        double valor = cellEnemies.size();
        return valor == 0 ? 0 : 1/valor;
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
        return new MummyMazeState(this);
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

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof MummyMazeState)){
            return false;
        }
        MummyMazeState s = (MummyMazeState) o;
        if(matrix.length != s.matrix.length){
            return false;
        }
        return Arrays.deepEquals(matrix,s.matrix);
    }

    @Override
    public int hashCode() {
        int result = 97 * 7 + Arrays.deepHashCode(this.matrix);
        result = 31 * result + Objects.hash(cellEnemies);
        result = 31 * result + Objects.hash(cellHero);
        return result;
    }
}
