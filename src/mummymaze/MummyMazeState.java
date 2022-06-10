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

//    private Cell[] cellWhiteMummies;
//    private int whiteMummies;
//
//    private Cell[] cellScorpions;
//    private int scorpions;
//
//    private Cell[] cellRedMummies;
//    private int redMummies;
//
//    private Cell[] cellHorizontalDoors;
//    private int horizontalDoors;
//
//    private Cell[] cellVerticalDoors;
//    private int verticalDoors;

//    private Cell[] cellTraps;
//    private int traps;

    private List<Enemy> cellEnemies;
    private List<Cell> cellDoors;
    private List<Cell> cellTraps;

//    private Scorpion cellScorpion;

    private boolean gameOver = false;

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

//    public MummyMazeState(MummyMazeState m){
//        this.matrix = m.matrix;
//        if(m.cellHero != null){
//            this.cellHero = (Cell) m.cellHero.clone();
//        }
//
//        if(m.cellExit != null){
//            this.cellExit = (Cell) m.cellExit.clone();
//        }
//        if(m.cellKey != null){
//            this.cellKey = (Cell) m.cellKey.clone();
//        }
//        if(m.cellWhiteMummies != null){
//            cellWhiteMummies = new Cell[m.whiteMummies];
//            for (int i = 0; i < m.whiteMummies; i++) {
//                cellWhiteMummies[i] = (Cell) m.cellWhiteMummies[i].clone();
//            }
////            this.cellWhiteMummies = m.cellWhiteMummies.clone();
//        }
//        this.whiteMummies = m.whiteMummies;
//
//        if(m.cellScorpions != null){
//            this.cellScorpions = m.cellScorpions.clone();
//        }
//        this.scorpions = m.scorpions;
//        if(m.cellRedMummies != null){
//            this.cellRedMummies = m.cellRedMummies.clone();
//        }
//        this.redMummies = m.redMummies;
//        if(m.cellHorizontalDoors != null){
//            this.cellHorizontalDoors = m.cellHorizontalDoors.clone();
//        }
//        this.horizontalDoors = m.horizontalDoors;
//        if(m.cellVerticalDoors != null){
//            this.cellVerticalDoors = m.cellVerticalDoors.clone();
//        }
//        this.verticalDoors = m.verticalDoors;
//        if(m.cellTraps != null){
//            this.cellTraps = m.cellTraps.clone();
//        }
//        this.traps = m.traps;
//    }

    public MummyMazeState(MummyMazeState m){
        this.matrix = m.matrix;
        if(m.cellHero != null){
            this.cellHero = (Cell) m.cellHero.clone();
        }
        if(m.cellExit != null){
            this.cellExit = (Cell) m.cellExit.clone();
        }

        if(m.cellKey != null){
            this.cellKey = (Cell) m.cellKey.clone();
        }
        this.cellEnemies = new ArrayList<>();
        this.cellTraps = new ArrayList<>();
        this.cellDoors = new ArrayList<>();
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
//        resetEnemies();
        cellEnemies = new ArrayList<>();
        cellDoors = new ArrayList<>();
        cellTraps = new ArrayList<>();


        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                this.matrix[i][j] = matrix[i][j];

                switch (matrix[i][j]) {
                    case 'H'://stores the hero's position in the matrix
                        cellHero = new Hero(i,j, matrix[i][j]);
                        break;
                    case 'S': //stores the exit position in the matrix
                        cellExit = new Cell(i,j, matrix[i][j]);
                        break;
                    case 'M': //stores the white mummy's position in the matrix
//                        if(whiteMummies!=0) {
//                            cellWhiteMummies = copyOf(cellWhiteMummies, whiteMummies + 1);
//                        }else{
//                            cellWhiteMummies = new Cell[1];
//                        }
//
//                        cellWhiteMummies[whiteMummies] = new Cell(i, j);
//                        whiteMummies++;
                        cellEnemies.add(new WhiteMummy(i,j, matrix[i][j]));
                        break;
                    case 'E': //stores the scorpion's position in the matrix
//                        if (scorpions != 0) {
//                            cellScorpions = copyOf(cellScorpions, scorpions+1);
//                        }else{
//                            cellScorpions = new Cell[1];
//                        }
//
//                        cellScorpions[scorpions] = new Cell(i,j);
//                        scorpions++;
                        cellEnemies.add(new Scorpion(i,j, matrix[i][j]));
                        break;
                    case 'V': //stores the red mummy's position in the matrix
//                        if (redMummies != 0) {
//                            cellRedMummies = copyOf(cellRedMummies, redMummies+1);
//                        }else{
//                            cellRedMummies = new Cell[1];
//                        }
//
//                        cellRedMummies[redMummies] = new Cell(i,j);
//                        redMummies++;
                        cellEnemies.add(new RedMummy(i,j, matrix[i][j]));
                        break;
                    case '=','_','"',')':
//                        if (horizontalDoors != 0) {
//                            cellHorizontalDoors = copyOf(cellHorizontalDoors, horizontalDoors+1);
//                        }else{
//                            cellHorizontalDoors = new Cell[1];
//                        }
//
//                        cellHorizontalDoors[horizontalDoors] = new Cell(i,j);
//                        horizontalDoors++;
                        cellDoors.add(new Cell(i,j, matrix[i][j]));
                        break;
                    case 'C':
                        cellKey = new Cell(i,j, matrix[i][j]);
                        break;
                    case 'A':
//                        if (traps != 0) {
//                            cellTraps = copyOf(cellTraps, traps+1);
//                        }else{
//                            cellTraps = new Cell[1];
//                        }
//
//                        cellTraps[traps] = new Cell(i,j);
//                        traps++;
                        cellTraps.add(new Cell(i,j, matrix[i][j]));
                        break;
                }
            }
        }
    }

    /*
    public MummyMazeState(char[][] matrix, Cell key, Cell[] trapsCell) { //será [13][13]
        this.matrix = new char[matrix.length][matrix.length];
        resetEnemies();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                this.matrix[i][j] = matrix[i][j];

                switch (matrix[i][j]) {
                    case 'H'://stores the hero's position in the matrix
                        cellHero = new Cell(i,j);
                        break;
                    case 'S': //stores the exit position in the matrix
                        cellExit = new Cell(i,j);
                        break;
                    case 'M': //stores the white mummy's position in the matrix
                        if(whiteMummies!=0) {
                            cellWhiteMummies = copyOf(cellWhiteMummies, whiteMummies + 1);
                        }else{
                            cellWhiteMummies = new Cell[1];
                        }

                        cellWhiteMummies[whiteMummies] = new Cell(i, j);
                        whiteMummies++;
                        break;
                    case 'E': //stores the scorpion's position in the matrix
                        if (scorpions != 0) {
                            cellScorpions = copyOf(cellScorpions, scorpions+1);
                        }else{
                            cellScorpions = new Cell[1];
                        }

                        cellScorpions[scorpions] = new Cell(i,j);
                        scorpions++;
                        break;

                    case 'V': //stores the red mummy's position in the matrix
                        if (redMummies != 0) {
                            cellRedMummies = copyOf(cellRedMummies, redMummies+1);
                        }else{
                            cellRedMummies = new Cell[1];
                        }

                        cellRedMummies[redMummies] = new Cell(i,j);
                        redMummies++;
                        break;
                    case '=','_':
                        if (horizontalDoors != 0) {
                            cellHorizontalDoors = copyOf(cellHorizontalDoors, horizontalDoors+1);
                        }else{
                            cellHorizontalDoors = new Cell[1];
                        }

                        cellHorizontalDoors[horizontalDoors] = new Cell(i,j);
                        horizontalDoors++;
                        break;
                    case '"',')':
                        if (verticalDoors != 0) {
                            cellVerticalDoors = copyOf(cellVerticalDoors, verticalDoors+1);
                        }else{
                            cellVerticalDoors = new Cell[1];
                        }

                        cellVerticalDoors[verticalDoors] = new Cell(i,j);
                        verticalDoors++;
                        break;
                }
            }
        }
        if(key!=null) {
            this.cellKey = new Cell(key.getLine(), key.getColumn());
        }
        if(trapsCell != null) {
            for (Cell t: trapsCell) {
                if (traps != 0) {
                    cellTraps = copyOf(cellTraps, traps+1);
                }else{
                    cellTraps = new Cell[1];
                }
                cellTraps[traps] = t;
                traps++;
            }
        }
    }
    */

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

//    private String moveScorpion(int pos){
//        int lineScorpion = cellScorpions[pos].getLine();
//        int columnScorpion = cellScorpions[pos].getColumn();
//
//        if(hasKilledHero(lineScorpion, columnScorpion) || gameOver){
//            return null;
//        }
//        // Colunas primeiro
//        int columnHero = cellHero.getColumn();
//        if (columnScorpion != columnHero) {
//            if (columnHero > columnScorpion) {
//                //Right
//                if (canMoveRight(lineScorpion, columnScorpion)) {
//                    verifySpecialCells(cellScorpions[pos]);
//
//                    // Analyse if there's an enemy to kill
//                    char nextPosition = matrix[lineScorpion][columnScorpion+=2];
//
//                    if(nextPosition=='C'){
//                        changeDoorsState();
//                    }
//
//                    pos = killEnemy(lineScorpion, columnScorpion, nextPosition,'E', pos);
//
//                    matrix[lineScorpion][columnScorpion] = 'E';
//
//                    cellScorpions[pos].setPosition(lineScorpion,columnScorpion);
//
//                    hasKilledHero(lineScorpion, columnScorpion);
//
//                    return convertMatrixToString(matrix);
//                }
//            } else {
//                //Left
//                if (canMoveLeft(lineScorpion, columnScorpion)) {
//                    verifySpecialCells(cellScorpions[pos]);
//
//                    // Analyse if there's an enemy to kill
//                    char nextPosition = matrix[lineScorpion][columnScorpion-=2];
//
//                    if(nextPosition=='C'){
//                        changeDoorsState();
//                    }
//
//                    pos = killEnemy(lineScorpion, columnScorpion, nextPosition,'E', pos);
//
//                    matrix[lineScorpion][columnScorpion] = 'E';
//
//                    cellScorpions[pos].setPosition(lineScorpion,columnScorpion);
//
//                    hasKilledHero(lineScorpion, columnScorpion);
//
//                    return convertMatrixToString(matrix);
//                }
//            }
//
//        }
//        int lineHero = cellHero.getLine();
//        if (lineHero != lineScorpion) {
//            if (lineHero > lineScorpion) {
//                //Down
//                if (canMoveDown(lineScorpion, columnScorpion)) {
//                    verifySpecialCells(cellScorpions[pos]);
//
//                    // Analyse if there's an enemy to kill
//                    char nextPosition = matrix[lineScorpion+=2][columnScorpion];
//
//                    if(nextPosition=='C'){
//                        changeDoorsState();
//                    }
//
//                    pos = killEnemy(lineScorpion, columnScorpion, nextPosition,'E', pos);
//
//                    matrix[lineScorpion][columnScorpion] = 'E';
//
//                    cellScorpions[pos].setPosition(lineScorpion,columnScorpion);
//
//                    hasKilledHero(lineScorpion, columnScorpion);
//
//                    return convertMatrixToString(matrix);
//                }
//            } else {
//                //Up
//                if (canMoveUp(lineScorpion, columnScorpion)) {
//                    verifySpecialCells(cellScorpions[pos]);
//
//                    // Analyse if there's an enemy to kill
//                    char nextPosition = matrix[lineScorpion-=2][columnScorpion];
//
//                    if(nextPosition=='C'){
//                        changeDoorsState();
//                    }
//
//                    pos = killEnemy(lineScorpion, columnScorpion, nextPosition,'E', pos);
//
//                    matrix[lineScorpion][columnScorpion] = 'E';
//
//                    cellScorpions[pos].setPosition(lineScorpion,columnScorpion);
//
//                    hasKilledHero(lineScorpion, columnScorpion);
//
//                    return convertMatrixToString(matrix);
//                }
//            }
//        }
//        return null;
//    }

    public void killEnemy(Enemy enemy){
        for (Enemy e: cellEnemies) {
            if(enemy != e && enemy.getLine() == e.getLine() && enemy.getColumn() == e.getColumn()){
                cellEnemies.remove(e);
                return;
            }
        }
    }
//
//    private void moveWhiteMummy(int pos, List<String> movements) {
//        int lineMummy = cellWhiteMummies[pos].getLine();
//        int columnMummy = cellWhiteMummies[pos].getColumn();
//
//        int columnHero = cellHero.getColumn();
//        int lineHero = cellHero.getLine();
//
//        for (int n = 0; n < 2; n++) {
//            if (gameOver || hasKilledHero(lineMummy, columnMummy)) {
//                return;
//            }
//
//            // Colunas primeiro
//            if (columnMummy != columnHero) {
//                if (columnHero > columnMummy) {
//                    //Right
//                    if (canMoveRight(lineMummy, columnMummy)) {
//                        verifySpecialCells(cellWhiteMummies[pos]);
//
//                        // Analyse if there's an enemy to kill
//                        char nextPosition = matrix[lineMummy][columnMummy+=2];
//
//                        if(nextPosition=='C'){
//                            changeDoorsState();
//                        }
//
//                        pos = killEnemy(lineMummy, columnMummy, nextPosition,'M', pos);
//
//                        matrix[lineMummy][columnMummy] = 'M';
//
//                        cellWhiteMummies[pos].setPosition(lineMummy,columnMummy);
//
//                        movements.add(convertMatrixToString(matrix));
//                        hasKilledHero(lineMummy, columnMummy);
//                        continue;
//                    }
//                } else {
//                    //Left
//                    if (canMoveLeft(lineMummy, columnMummy)) {
//                        verifySpecialCells(cellWhiteMummies[pos]);
//
//                        // Analyse if there's an enemy to kill
//                        char nextPosition = matrix[lineMummy][columnMummy-=2];
//
//                        if(nextPosition=='C'){
//                            changeDoorsState();
//                        }
//
//                        pos = killEnemy(lineMummy, columnMummy, nextPosition,'M', pos);
//
//                        matrix[lineMummy][columnMummy] = 'M';
//
//                        cellWhiteMummies[pos].setPosition(lineMummy,columnMummy);
//
//                        movements.add(convertMatrixToString(matrix));
//                        hasKilledHero(lineMummy, columnMummy);
//                        continue;
//                    }
//                }
//            }
//
//            if (lineHero != lineMummy) {
//                if (lineHero > lineMummy) {
//                    //Down
//                    if (canMoveDown(lineMummy, columnMummy)) {
//                        verifySpecialCells(cellWhiteMummies[pos]);
//
//                        // Analyse if there's an enemy to kill
//                        char nextPosition = matrix[lineMummy+=2][columnMummy];
//                        pos = killEnemy(lineMummy, columnMummy, nextPosition,'M', pos);
//
//                        matrix[lineMummy][columnMummy] = 'M';
//
//                        cellWhiteMummies[pos].setPosition(lineMummy,columnMummy);
//
//                        movements.add(convertMatrixToString(matrix));
//                    }
//                } else {
//                    //Up
//                    if (canMoveUp(lineMummy, columnMummy)) {
//                        verifySpecialCells(cellWhiteMummies[pos]);
//
//                        // Analyse if there's an enemy to kill
//                        char nextPosition = matrix[lineMummy-=2][columnMummy];
//
//                        if(nextPosition=='C'){
//                            changeDoorsState();
//                        }
//
//                        pos = killEnemy(lineMummy, columnMummy, nextPosition,'M', pos);
//
//                        matrix[lineMummy][columnMummy] = 'M';
//
//                        cellWhiteMummies[pos].setPosition(lineMummy,columnMummy);
//
//                        movements.add(convertMatrixToString(matrix));
//                    }
//                }
//            }
//
//            hasKilledHero(lineMummy, columnMummy);
//        }
//
//    }
//
//    private void moveRedMummy(int pos, List<String> movements) {
//        int lineMummy = cellRedMummies[pos].getLine();
//        int columnMummy = cellRedMummies[pos].getColumn();
//
//        int columnHero = cellHero.getColumn();
//        int lineHero = cellHero.getLine();
//
//        for (int n = 0; n < 2; n++) {
//            if(gameOver || hasKilledHero(lineMummy, columnMummy)){
//                break;
//            }
//            //linhas primeiro
//            if (lineHero != lineMummy) {
//                if (lineHero > lineMummy) {
//                    //Down
//                    if (canMoveDown(lineMummy, columnMummy)) {
//                        verifySpecialCells(cellRedMummies[pos]);
//
//                        // Analyse if there's an enemy to kill
//                        char nextPosition = matrix[lineMummy+=2][columnMummy];
//
//                        if(nextPosition=='C'){
//                            changeDoorsState();
//                        }
//
//                        pos = killEnemy(lineMummy, columnMummy, nextPosition,'V', pos);
//
//                        matrix[lineMummy][columnMummy] = 'V';
//
//                        movements.add(convertMatrixToString(matrix));
//
//                        cellRedMummies[pos].setPosition(lineMummy,columnMummy);
//                        hasKilledHero(lineMummy, columnMummy);
//                        continue;
//                    }
//                } else {
//                    //Up
//                    if (canMoveUp(lineMummy, columnMummy)) {
//                        verifySpecialCells(cellRedMummies[pos]);
//
//                        // Analyse if there's an enemy to kill
//                        char nextPosition = matrix[lineMummy-=2][columnMummy];
//
//                        if(nextPosition=='C'){
//                            changeDoorsState();
//                        }
//
//                        pos = killEnemy(lineMummy, columnMummy, nextPosition,'V', pos);
//
//                        matrix[lineMummy][columnMummy] = 'V';
//
//                        movements.add(convertMatrixToString(matrix));
//                        cellRedMummies[pos].setPosition(lineMummy,columnMummy);
//                        hasKilledHero(lineMummy, columnMummy);
//                        continue;
//                    }
//                }
//            }
//
//            if (columnMummy != columnHero) {
//                if (columnHero > columnMummy) {
//                    //Right
//                    if (canMoveRight(lineMummy, columnMummy)) {
//                        verifySpecialCells(cellRedMummies[pos]);
//
//                        // Analyse if there's an enemy to kill
//                        char nextPosition = matrix[lineMummy][columnMummy+=2];
//
//                        if(nextPosition=='C'){
//                            changeDoorsState();
//                        }
//
//                        pos = killEnemy(lineMummy, columnMummy, nextPosition,'V', pos);
//
//                        matrix[lineMummy][columnMummy] = 'V';
//
//                        movements.add(convertMatrixToString(matrix));
//                        cellRedMummies[pos].setPosition(lineMummy,columnMummy);
//                    }
//                } else {
//                    //Left
//                    if (canMoveLeft(lineMummy, columnMummy)) {
//                        verifySpecialCells(cellRedMummies[pos]);
//
//                        // Analyse if there's an enemy to kill
//                        char nextPosition = matrix[lineMummy][columnMummy-=2];
//
//                        if(nextPosition=='C'){
//                            changeDoorsState();
//                        }
//
//                        pos = killEnemy(lineMummy, columnMummy, nextPosition,'V', pos);
//
//                        matrix[lineMummy][columnMummy] = 'V';
//
//                        movements.add(convertMatrixToString(matrix));
//                        cellRedMummies[pos].setPosition(lineMummy,columnMummy);
//                    }
//                }
//            }
//
//            hasKilledHero(lineMummy, columnMummy);
//        }
//    }
//
//    private int killEnemy(int lineEnemy, int columnEnemy, char nextPosition, char typeEnemy, int pos) {
//        int newIndex = pos;
//        switch(nextPosition){
//            case 'M':
//                Cell[] newWhiteMummies;
//                if(whiteMummies > 1){
//                    newWhiteMummies = new Cell[whiteMummies-1];
//                }else{
//                    newWhiteMummies = new Cell[1];
//                }
//
//                for (int i=0,j=0; i<whiteMummies; i++,j++){
//                    if(pos==i){
//                        j--;
//                        continue;
//                    }
//
//                    int line = cellWhiteMummies[i].getLine();
//                    int column = cellWhiteMummies[i].getColumn();
//                    newWhiteMummies[j] = new Cell(line, column);
//                    if(line == lineEnemy && column == columnEnemy && typeEnemy=='M'){
//                        newIndex=j;
//                    }
//                }
//
//                whiteMummies--;
//                cellWhiteMummies = copyOf(newWhiteMummies, whiteMummies);
//                break;
//            case 'V':
//                Cell[] newRedMummies;
//                if(redMummies > 1){
//                    newRedMummies = new Cell[redMummies-1];
//                }else{
//                    newRedMummies = new Cell[1];
//                }
//
//                for (int i=0,j=0; i<redMummies; i++,j++){
//                    if(pos==i){
//                        j--;
//                        continue;
//                    }
//
//                    int line = cellRedMummies[i].getLine();
//                    int column = cellRedMummies[i].getColumn();
//                    newRedMummies[j] = new Cell(line,column);
//                    if(line == lineEnemy && column == columnEnemy && typeEnemy=='V'){
//                        newIndex = j;
//                    }
//
//                }
//                redMummies--;
//                cellRedMummies = copyOf(newRedMummies, redMummies);
//                break;
//            case 'E':
//                Cell[] newScorpions;
//                if(scorpions > 1){
//                    newScorpions = new Cell[scorpions-1];
//                }else {
//                    newScorpions = new Cell[1];
//                }
//
//                for (int i=0,j=0; i<scorpions; i++,j++){
//                    if(pos==i){
//                        j--;
//                        continue;
//                    }
//
//                    int line = cellScorpions[i].getLine();
//                    int column = cellScorpions[i].getColumn();
//                    newScorpions[j] = new Cell(line, column);
//                    if(line == lineEnemy && column == columnEnemy && typeEnemy=='E'){
//                        newIndex = j;
//                    }
//
//                }
//                scorpions--;
//                cellScorpions = copyOf(newScorpions, scorpions);
//                break;
//        }
//        return newIndex;
//    }

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

//        for (int i=0; i<traps;i++){
//            if(cellTraps[i].equals(cellEntity)){
//                matrix[lineEntity][columnEntity] = 'A';
//            }
//        }

//        if(cellKey!=null && cellKey.equals(cellEntity)){ //experimentar mudar para cellEntity.getLine() == lineKey && cellEntity.getColumn() == columnKey para ver se entra aqui no nivel 22
//            matrix[lineEntity][columnEntity] = 'C';
//        }else if(matrix[lineEntity][columnEntity] != 'A'){
//            matrix[lineEntity][columnEntity] = '.';
//        }
    }

//    private boolean hasKilledHero(Cell cell){
//        if(cell.equals(cellHero)){ //TODO - Equals não funciona bem
//            return gameOver = true;
//        }
//        return gameOver;
//    }

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

    private void moveEnemies(List<String> movements) {
        List<String> newMoves;
        for (Enemy e: cellEnemies) {
            newMoves = e.moveEnemy(this);
            if(newMoves != null){
                movements.addAll(newMoves);
            }
        }
//        int n = whiteMummies;
//        for (int whiteM = 0; whiteM < n; whiteM++) {
//            moveWhiteMummy(whiteM, movements);
//            if(whiteMummies < n){ //se um mummy morreu, o numero de mummies diminui (para nao fazer o ciclo com o nº de mummies anterior)
//                n--;
//            }
//        }
//
//        n = scorpions;
//        for (int scorpion = 0; scorpion < n; scorpion++) {
//            String movement = moveScorpion(scorpion);
//            if(movement!=null){
//                movements.add(movement);
//            }
//            if(scorpions < n){
//                n--;
//            }
//
//        }
//
//        n = redMummies;
//        for(int redM = 0; redM < n; redM++){
//            moveRedMummy(redM, movements);
//            if(redMummies < n){
//                n--;
//            }
//        }
    }

    public List<String> moveUp() {
        List<String> moves = new ArrayList<>();
        moves.add(((Hero) cellHero).moveUpSuper(this, 'H'));
        moveEnemies(moves);
        return moves;
//        List<String> movements = new ArrayList<>();
//        // Hero's movement
//        int columnHero = cellHero.getColumn();
//        int lineHero = cellHero.getLine();
//
//        char nextPosition = matrix[lineHero-2][columnHero];
//        if(nextPosition == 'A' || nextPosition == 'M' || nextPosition == 'V' || nextPosition == 'E'){
//            gameOver = true;
//        }
//
//        verifySpecialCells(cellHero);
//
//        lineHero-=2;
//
//        if(nextPosition=='C'){
//            changeDoorsState();
//        }
//
//        matrix[lineHero][columnHero] = 'H';
//        cellHero.setPosition(lineHero,columnHero);
//
//        movements.add(convertMatrixToString(matrix));
//
//        moveEnemies(movements);
//
//        return movements;
    }

    public List<String> moveRight() {
        List<String> moves = new ArrayList<>();
        moves.add(((Hero) cellHero).moveRightSuper(this, 'H'));
        moveEnemies(moves);
        return moves;
//        List<String> movements = new ArrayList<>();
//
//        int lineHero = cellHero.getLine();
//        int columnHero = cellHero.getColumn();
//
//        char nextPosition = matrix[lineHero][columnHero+2];
//        if(nextPosition == 'A' || nextPosition == 'M' || nextPosition == 'V' || nextPosition == 'E'){
//            gameOver = true;
//        }
//
//        verifySpecialCells(cellHero);
//
//        columnHero+=2;
//
//        if(nextPosition == 'C'){
//            changeDoorsState();
//        }
//
//        matrix[lineHero][columnHero] = 'H';
//        cellHero.setPosition(lineHero,columnHero);
//
//        movements.add(convertMatrixToString(matrix));
//
//        moveEnemies(movements);
//
//        return movements;
    }

    public List<String> moveDown() {
        List<String> moves = new ArrayList<>();
        moves.add(((Hero) cellHero).moveDownSuper(this, 'H'));
        moveEnemies(moves);
        return moves;
//        List<String> movements = new ArrayList<>();
//
//        int lineHero = cellHero.getLine();
//        int columnHero = cellHero.getColumn();
//
//        char nextPosition = matrix[lineHero+2][columnHero];
//        if(nextPosition == 'A' || nextPosition == 'M' || nextPosition == 'V' || nextPosition == 'E'){
//            gameOver = true;
//        }
//
//        verifySpecialCells(cellHero);
//
//        lineHero+=2;
//
//        if(nextPosition == 'C'){
//            changeDoorsState();
//        }
//
//        matrix[lineHero][columnHero] = 'H';
//        cellHero.setPosition(lineHero,columnHero);
//
//        movements.add(convertMatrixToString(matrix));
//        moveEnemies(movements);
//
//        return movements;
    }

    public List<String> moveLeft() {
        List<String> moves = new ArrayList<>();
        moves.add(((Hero) cellHero).moveLeftSuper(this, 'H'));
        moveEnemies(moves);
        return moves;
//        List<String> movements = new ArrayList<>();
//
//        int lineHero = cellHero.getLine();
//        int columnHero = cellHero.getColumn();
//
//        char nextPosition = matrix[lineHero][columnHero-2];
//        if(nextPosition == 'A' || nextPosition == 'M' || nextPosition == 'V' || nextPosition == 'E'){
//            gameOver = true;
//        }
//
//        verifySpecialCells(cellHero);
//
//        columnHero-=2;
//
//        if(nextPosition == 'C'){
//            changeDoorsState();
//        }
//        matrix[lineHero][columnHero] = 'H';
//        cellHero.setPosition(lineHero,columnHero);
//        movements.add(convertMatrixToString(matrix));
//        moveEnemies(movements);
//
//        return movements;
    }

    public List<String> moveStandStill(){
        List<String> movements = new ArrayList<>();
        moveEnemies(movements);
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
//        for (int i = 0; i < horizontalDoors; i++) {
//            int lineHorizontalDoors = cellHorizontalDoors[i].getLine();
//            int columnHorizontalDoors = cellHorizontalDoors[i].getColumn();
//
//            if (matrix[lineHorizontalDoors][columnHorizontalDoors]=='=') {
//                matrix[lineHorizontalDoors][columnHorizontalDoors] = '_';
//            }else {
//                matrix[lineHorizontalDoors][columnHorizontalDoors] = '=';
//            }
//        }
//
//        for (int i = 0; i < verticalDoors; i++) {
//            int lineVerticalDoors = cellVerticalDoors[i].getLine();
//            int columnVerticalDoors = cellVerticalDoors[i].getColumn();
//
//            if(matrix[lineVerticalDoors][columnVerticalDoors]=='"'){
//                matrix[lineVerticalDoors][columnVerticalDoors]=')';
//            }else {
//                matrix[lineVerticalDoors][columnVerticalDoors]='"';
//            }
//        }
    }

//    public boolean canMoveRight(int lineEntity, int columnEntity){
//
//        //verificar se não sai fora dos limites (line < 12 && line > 0 && column < 12 && column > 0)
//        if(columnEntity == matrix.length - 2){
//            return false;
//        }
//
//        char matrixPosition = matrix[lineEntity][columnEntity+1];
//        //verificar se tem parede (| ou -), porta fechada (" ou =)
//        return  matrixPosition != '|' && matrixPosition != '-' &&
//                matrixPosition != '=' && matrixPosition != '"';
//    }

    public boolean canMoveRightHero(){
//        return !gameOver && canMoveRight(cellHero.getLine(), cellHero.getColumn());
        return !gameOver && ((Hero)cellHero).canMoveRight(this);
    }

//    public boolean canMoveLeft(int lineEntity, int columnEntity){
//        //moves off limits?
//        if(columnEntity == 1){
//            return false;
//        }
//
//        //has something blocking entity's path?
//        char matrixPosition = matrix[lineEntity][columnEntity-1];
//        //verificar se tem parede (| ou -), porta fechada (" ou =)
//        return  matrixPosition != '|' && matrixPosition != '-' &&
//                matrixPosition != '=' && matrixPosition != '"';
//    }

    public boolean canMoveLeftHero(){
//        return !gameOver && canMoveLeft(cellHero.getLine(), cellHero.getColumn());
        return !gameOver && ((Hero)cellHero).canMoveLeft(this);
    }

//    public boolean canMoveUp(int lineEntity, int columnEntity){
//        //moves off limits?
//        if(lineEntity == 1){
//            return false;
//        }
//
//        char matrixPosition = matrix[lineEntity-1][columnEntity];
//        //verificar se tem parede (| ou -), porta fechada (" ou =)
//        return  matrixPosition != '|' && matrixPosition != '-' &&
//                matrixPosition != '=' && matrixPosition != '"';
//    }

    public boolean canMoveUpHero(){
//        return !gameOver && canMoveUp(cellHero.getLine(), cellHero.getColumn());
        return !gameOver && ((Hero)cellHero).canMoveUp(this);
    }

//    public boolean canMoveDown(int lineEntity, int columnEntity){
//        //moves off limits?
//        if(lineEntity == matrix.length - 2){
//            return false;
//        }
//
//        char matrixPosition = matrix[lineEntity+1][columnEntity];
//        //verificar se tem parede (| ou -), porta fechada (" ou =)
//        return  matrixPosition != '|' && matrixPosition != '-' &&
//                matrixPosition != '=' && matrixPosition != '"';
//    }

    public boolean canMoveDownHero(){
//        return !gameOver && canMoveDown(cellHero.getLine(), cellHero.getColumn());
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

//        for (int i = 0; i < whiteMummies; i++) {
//            distance += Math.abs(cellHero.getLine() - cellWhiteMummies[i].getLine()) + Math.abs(cellHero.getColumn() - cellWhiteMummies[i].getColumn());
//        }
//        for (int i = 0; i < redMummies; i++) {
//            distance += Math.abs(cellHero.getLine() - cellRedMummies[i].getLine()) + Math.abs(cellHero.getColumn() - cellRedMummies[i].getColumn());
//        }
//        for (int i = 0; i < scorpions; i++) {
//            distance += Math.abs(cellHero.getLine() - cellScorpions[i].getLine()) + Math.abs(cellHero.getColumn() - cellScorpions[i].getColumn());
//        }

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
//        for (int i = 0; i < whiteMummies; i++) {
//            if(matrix[cellWhiteMummies[i].getLine()][cellWhiteMummies[i].getColumn()-1] == '|' ||
//                    matrix[cellWhiteMummies[i].getLine()][cellWhiteMummies[i].getColumn()+1] == '|' ||
//                    matrix[cellWhiteMummies[i].getLine()-1][cellWhiteMummies[i].getColumn()] == '-' ||
//                    matrix[cellWhiteMummies[i].getLine()+1][cellWhiteMummies[i].getColumn()] == '-' ){
//                walls++;
//            }
//        }
//        for (int i = 0; i < scorpions; i++) {
//            if(matrix[cellScorpions[i].getLine()][cellScorpions[i].getColumn()-1] == '|' ||
//                    matrix[cellScorpions[i].getLine()][cellScorpions[i].getColumn()+1] == '|' ||
//                    matrix[cellScorpions[i].getLine()-1][cellScorpions[i].getColumn()] == '-' ||
//                    matrix[cellScorpions[i].getLine()+1][cellScorpions[i].getColumn()] == '-' ){
//                walls++;
//            }
//        }
//        for (int i = 0; i < redMummies; i++) {
//            if(matrix[cellRedMummies[i].getLine()][cellRedMummies[i].getColumn()-1] == '|' ||
//                    matrix[cellRedMummies[i].getLine()][cellRedMummies[i].getColumn()+1] == '|' ||
//                    matrix[cellRedMummies[i].getLine()-1][cellRedMummies[i].getColumn()] == '-' ||
//                    matrix[cellRedMummies[i].getLine()+1][cellRedMummies[i].getColumn()] == '-' ){
//                walls++;
//            }
//        }
        return 1/walls;
    }

    public double computeNumberEnemies() {
//        int enemies = scorpions + redMummies + whiteMummies;
//        if(enemies == 0){
//            return 0;
//        }
//        return 1/enemies;
        return cellEnemies.size();
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
//        return new MummyMazeState(this.matrix, cellKey, cellTraps);
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

//    public void resetEnemies() {
//        scorpions = 0;
//        whiteMummies = 0;
//        redMummies = 0;
//        horizontalDoors = 0;
//        verticalDoors = 0;
//        traps = 0;
//    }

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
//        result = 31 * result + Arrays.hashCode(cellEnemies);
//        result = 31 * result + Objects.hash(whiteMummies);
//        result = 31 * result + Objects.hash(redMummies);
//        result = 31 * result + Objects.hash(scorpions);
        return result;
    }
}
