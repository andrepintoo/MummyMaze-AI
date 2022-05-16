package mummymaze;

import agent.Action;
import agent.State;
import eightpuzzle.EightPuzzleState;
import gui.MainFrame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class MummyMazeState extends State implements Cloneable{

    private final char[][] matrix; //posições válidas : do [1][1] ao [1][11] (na horizontal) e [1][1] ao [11][1] (na vertical)
    private int columnExit;
    private int lineExit;
    private int lineKey;
    private int columnKey;
//    private int lineHero; //variables to store where the hero is
//    private int columnHero;
    private Cell cellHero = new Cell(); //cell where the hero is

    //Falta colocar a Cell das armadilhas

//    private int[] lineWhiteMummies;
//    private int[] columnWhiteMummies;
    private Cell[] cellWhiteMummies;
    private int whiteMummies;

    //    private int[] lineScorpions;
//    private int[] columnScorpions;
    private Cell[] cellScorpions;
    private int scorpions;

    private int[] lineRedMummies;
    private int[] columnRedMummies;
    private int redMummies;

    private int[] lineHorizontalDoors;
    private int[] columnHorizontalDoors;
    private int horizontalDoors;

    private int[] lineVerticalDoors;
    private int[] columnVerticalDoors;
    private int verticalDoors;

    private int[] lineTraps;
    private int[] columnTraps;
    private int traps;

    private boolean gameOver = false;

    public MummyMazeState(char[][] matrix) { //será [13][13]
        this.matrix = new char[matrix.length][matrix.length];
        resetEnemies();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                this.matrix[i][j] = matrix[i][j];

                switch (matrix[i][j]) {
                    case 'H'://stores the hero's position in the matrix
                        cellHero.setPosition(i,j);
                        break;
                    case 'S': //stores the exit position in the matrix
                        lineExit = i;
                        columnExit = j;
                        break;
                    case 'M': //stores the white mummy's position in the matrix
                        if (whiteMummies != 0) {
                            cellWhiteMummies = Arrays.copyOf(cellWhiteMummies, whiteMummies+1);
                            cellWhiteMummies[whiteMummies] = new Cell();
                        }
                        cellWhiteMummies[whiteMummies].setPosition(i,j);

                        whiteMummies++;
                        break;
                    case 'E': //stores the scorpion's position in the matrix
                        if (scorpions != 0) {
                            cellScorpions = Arrays.copyOf(cellScorpions, scorpions+1);
//                        lineScorpions = Arrays.copyOf(lineScorpions, scorpions + 1);
//                        columnScorpions = Arrays.copyOf(columnScorpions, scorpions + 1);
                            cellScorpions[scorpions] = new Cell();
                        }

                        cellScorpions[scorpions].setPosition(i,j);
                        scorpions++;
                        break;

                    case 'V': //stores the red mummy's position in the matrix
                        if (redMummies != 0) {
                            lineRedMummies = Arrays.copyOf(lineRedMummies, redMummies + 1);
                            columnRedMummies = Arrays.copyOf(columnRedMummies, redMummies + 1);
                        }

                        lineRedMummies[redMummies] = i;
                        columnRedMummies[redMummies] = j;

                        redMummies++;
                        break;
                    case '=','_':
                        if (horizontalDoors != 0) {
                            lineHorizontalDoors = Arrays.copyOf(lineHorizontalDoors, horizontalDoors + 1);
                            columnHorizontalDoors = Arrays.copyOf(columnHorizontalDoors, horizontalDoors + 1);
                        }

                        lineHorizontalDoors[horizontalDoors] = i;
                        columnHorizontalDoors[horizontalDoors] = j;

                        horizontalDoors++;
                        break;
                    case '"',')':
                        if (verticalDoors != 0) {
                            lineVerticalDoors = Arrays.copyOf(lineVerticalDoors, verticalDoors + 1);
                            columnVerticalDoors = Arrays.copyOf(columnVerticalDoors, verticalDoors + 1);
                        }

                        lineVerticalDoors[verticalDoors] = i;
                        columnVerticalDoors[verticalDoors] = j;

                        verticalDoors++;
                        break;
                    case 'C':
                        lineKey = i;
                        columnKey = j;
                        break;
                    case 'A':
                        if (traps != 0) {
                            lineTraps = Arrays.copyOf(lineTraps, traps + 1);
                            columnTraps = Arrays.copyOf(columnTraps, traps + 1);
                        }

                        lineTraps[traps] = i;
                        columnTraps[traps] = j;

                        traps++;
                        break;

                }
            }
        }
    }

    @Override
    public void executeAction(Action action) {
        //action.resetMovements();
        action.execute(this);
        if (MainFrame.SHOWSOLUTION) {
            firePuzzleChanged(); //para atualizar a interface gráfica
        }
    }


    public boolean isGameOver() {
        return gameOver;
    }

    private void moveScorpion(int pos, List<String> movements){
        int lineScorpion = cellScorpions[pos].getLine();//lineScorpions[pos];
        int columnScorpion = cellScorpions[pos].getColumn();//columnScorpions[pos];

        if(hasKilledHero(lineScorpion, columnScorpion) || gameOver){
            return;
        }
        // Colunas primeiro
        if (columnScorpion != cellHero.getColumn()) {
            if (cellHero.getColumn() > columnScorpion) {
                //Right
                if (canMoveRight(lineScorpion, columnScorpion)) {
                    matrix[lineScorpion][columnScorpion] = '.';
                    if(cellScorpions[pos].hasSteppedOnTrap()){
                        matrix[lineScorpion][columnScorpion] = 'A';
                        cellScorpions[pos].leftTrapPosition();
                    }else if(cellScorpions[pos].hasSteppedOnKey()){
                        matrix[lineScorpion][columnScorpion] = 'C';
                        cellScorpions[pos].leftKeyPosition();
                    }
                    char nextPosition = matrix[lineScorpion][columnScorpion+=2];
                    killEnemy(lineScorpion, columnScorpion, nextPosition);
                    matrix[lineScorpion][columnScorpion] = 'E';

                    movements.add(convertMatrixToString(matrix));
                    if(scorpions>1) {
                        if(nextPosition == 'A'){
                            cellScorpions[pos].setPositionSteppedOnTrap(lineScorpion, columnScorpion);
                        }else if(nextPosition == 'C'){
                            cellScorpions[pos].setPositionSteppedOnKey(lineScorpion, columnScorpion);
                        }else{
                            cellScorpions[pos].setColumn(columnScorpion);
                        }
                    }else{
                        if(nextPosition == 'A'){
                            cellScorpions[0].setPositionSteppedOnTrap(lineScorpion, columnScorpion);
                        }else if(nextPosition == 'C'){
                            cellScorpions[0].setPositionSteppedOnKey(lineScorpion, columnScorpion);
                        }else{
                            cellScorpions[0].setColumn(columnScorpion);
                        }
                    }
                    return;
                }
            } else {
                //Left
                if (canMoveLeft(lineScorpion, columnScorpion)) {
                    matrix[lineScorpion][columnScorpion] = '.';
                    if(cellScorpions[pos].hasSteppedOnTrap()){
                        matrix[lineScorpion][columnScorpion] = 'A';
                        cellScorpions[pos].leftTrapPosition();
                    }else if(cellScorpions[pos].hasSteppedOnKey()){
                        matrix[lineScorpion][columnScorpion] = 'C';
                        cellScorpions[pos].leftKeyPosition();
                    }
                    char nextPosition = matrix[lineScorpion][columnScorpion-=2];
                    killEnemy(lineScorpion, columnScorpion, nextPosition);
                    matrix[lineScorpion][columnScorpion] = 'E';

                    movements.add(convertMatrixToString(matrix));
                    if(scorpions>1) {
                        if(nextPosition == 'A'){
                            cellScorpions[pos].setPositionSteppedOnTrap(lineScorpion, columnScorpion);
                        }else if(nextPosition == 'C'){
                            cellScorpions[pos].setPositionSteppedOnKey(lineScorpion, columnScorpion);
                        }else{
                            cellScorpions[pos].setColumn(columnScorpion);
                        }
                    }else{
                        if(nextPosition == 'A'){
                            cellScorpions[0].setPositionSteppedOnTrap(lineScorpion, columnScorpion);
                        }else if(nextPosition == 'C'){
                            cellScorpions[0].setPositionSteppedOnKey(lineScorpion, columnScorpion);
                        }else{
                            cellScorpions[0].setColumn(columnScorpion);
                        }
                    }
                    return;
                }
            }

        }
        if (cellHero.getLine() != lineScorpion) {
            if (cellHero.getLine() > lineScorpion) {
                //Down
                if (canMoveDown(lineScorpion, columnScorpion)) {
                    matrix[lineScorpion][columnScorpion] = '.';
                    if(cellScorpions[pos].hasSteppedOnTrap()){
                        matrix[lineScorpion][columnScorpion] = 'A';
                        cellScorpions[pos].leftTrapPosition();
                    }else if(cellScorpions[pos].hasSteppedOnKey()){
                        matrix[lineScorpion][columnScorpion] = 'C';
                        cellScorpions[pos].leftKeyPosition();
                    }
                    char nextPosition = matrix[lineScorpion+=2][columnScorpion];
                    killEnemy(lineScorpion, columnScorpion, nextPosition);
                    matrix[lineScorpion][columnScorpion] = 'E';

                    movements.add(convertMatrixToString(matrix));

                    if(scorpions>1) {
                        if(nextPosition == 'A'){
                            cellScorpions[pos].setPositionSteppedOnTrap(lineScorpion, columnScorpion);
                        }else if(nextPosition == 'C'){
                            cellScorpions[pos].setPositionSteppedOnKey(lineScorpion, columnScorpion);
                        }else{
                            cellScorpions[pos].setLine(lineScorpion);
                        }
                    }else{
                        if(nextPosition == 'A'){
                            cellScorpions[0].setPositionSteppedOnTrap(lineScorpion, columnScorpion);
                        }else if(nextPosition == 'C'){
                            cellScorpions[0].setPositionSteppedOnKey(lineScorpion, columnScorpion);
                        }else{
                            cellScorpions[0].setLine(lineScorpion);
                        }
                    }
                }
            } else {
                //Up
                if (canMoveUp(lineScorpion, columnScorpion)) {
                    matrix[lineScorpion][columnScorpion] = '.';
                    if(cellScorpions[pos].hasSteppedOnTrap()){
                        matrix[lineScorpion][columnScorpion] = 'A';
                        cellScorpions[pos].leftTrapPosition();
                    }else if(cellScorpions[pos].hasSteppedOnKey()){
                        matrix[lineScorpion][columnScorpion] = 'C';
                        cellScorpions[pos].leftKeyPosition();
                    }
                    char nextPosition = matrix[lineScorpion-=2][columnScorpion];
                    killEnemy(lineScorpion, columnScorpion, nextPosition);
                    matrix[lineScorpion][columnScorpion] = 'E';

                    movements.add(convertMatrixToString(matrix));
                    if(scorpions>1) {
                        if(nextPosition == 'A'){
                            cellScorpions[pos].setPositionSteppedOnTrap(lineScorpion, columnScorpion);
                        }else if(nextPosition == 'C'){
                            cellScorpions[pos].setPositionSteppedOnKey(lineScorpion, columnScorpion);
                        }else{
                            cellScorpions[pos].setLine(lineScorpion);
                        }
                    }else{
                        if(nextPosition == 'A'){
                            cellScorpions[0].setPositionSteppedOnTrap(lineScorpion, columnScorpion);
                        }else if(nextPosition == 'C'){
                            cellScorpions[0].setPositionSteppedOnKey(lineScorpion, columnScorpion);
                        }else{
                            cellScorpions[0].setLine(lineScorpion);
                        }
                    }
                }
            }

        }
    }

    private void killEnemy(int lineScorpion, int columnScorpion, char nextPosition) {
        switch(nextPosition){
            case 'M':
                Cell[] newWhiteMummies;
                if(whiteMummies > 1){
                   newWhiteMummies = new Cell[whiteMummies-1];
                }else{
                    newWhiteMummies = new Cell[1];
                }

                for (int i=0,j=0; i<whiteMummies; i++,j++){
                    newWhiteMummies[j] = new Cell();
                    int line = cellWhiteMummies[i].getLine();
                    int column = cellWhiteMummies[i].getColumn();
                    if(line == lineScorpion && column == columnScorpion){
                        j--;
                        continue;
                    }
                    newWhiteMummies[j].setPosition(line, column);
                }
                whiteMummies--;
                cellWhiteMummies = Arrays.copyOf(newWhiteMummies, whiteMummies);
                break;
            case 'V':
                int[] newLineRedMummies = new int[redMummies-1];
                int[] newColumnRedMummies = new int[redMummies-1];
                for (int i=0,j=0; i<redMummies; i++,j++){
                    if(lineRedMummies[i]== lineScorpion && columnRedMummies[i]== columnScorpion){
                        j--;
                        continue;
                    }
                    newLineRedMummies[j] = lineRedMummies[i];
                    newColumnRedMummies[j] = columnRedMummies[i];
                }
                redMummies--;

                lineRedMummies = Arrays.copyOf(newLineRedMummies, redMummies);
                columnRedMummies = Arrays.copyOf(newColumnRedMummies, redMummies);
                break;
            case 'E':
//                int[] newLineScorpions = new int[scorpions-1];
//                int[] newColumnScorpions = new int[scorpions-1];
                Cell[] newScorpions;
                if(scorpions > 1){
                     newScorpions = new Cell[scorpions-1];
                }else {
                    newScorpions = new Cell[1];
                }

                for (int i=0,j=0; i<scorpions; i++,j++){
                    newScorpions[j] = new Cell();
                    int line = cellScorpions[i].getLine();
                    int column = cellScorpions[i].getColumn();
//                    if(lineScorpions[i]== lineScorpion && columnScorpions[i]== columnScorpion){
                    if(line == lineScorpion && column == columnScorpion){
                        j--;
                        continue;
                    }
                    newScorpions[j].setPosition(line, column);
//                    newLineScorpions[j] = line;
//                    newColumnScorpions[j] = column;
                }
                scorpions--;
                cellScorpions = Arrays.copyOf(newScorpions, scorpions);
//                lineScorpions = Arrays.copyOf(newLineScorpions, scorpions);
//                columnScorpions = Arrays.copyOf(newColumnScorpions, scorpions);
                break;
        }
    }

    private void moveWhiteMummy(int pos, List<String> movements) {
        int lineMummy = cellWhiteMummies[pos].getLine();
        int columnMummy = cellWhiteMummies[pos].getColumn();

        for (int i = 0; i < 2; i++) {
            if (hasKilledHero(lineMummy, columnMummy) || gameOver) {
                return;
            }
//            if((lineWhiteMummies[pos]==3 && columnWhiteMummies[pos]==1) || (lineWhiteMummies[pos]==9 && columnWhiteMummies[pos]==4)){ //PARA O NIVEL 5, a mumia vai ter que estar numa dessas posições
//                System.out.println("debug");
//            }

            // Colunas primeiro
            if (columnMummy != cellHero.getColumn()) {
                if (cellHero.getColumn() > columnMummy) {
                    //Right
                    if (canMoveRight(lineMummy, columnMummy)) {
                        matrix[lineMummy][columnMummy] = '.';
                        if (cellWhiteMummies[pos].hasSteppedOnTrap()) {
                            matrix[lineMummy][columnMummy] = 'A';
                            cellWhiteMummies[pos].leftTrapPosition();
                        } else if (cellWhiteMummies[pos].hasSteppedOnKey()) {
                            matrix[lineMummy][columnMummy] = 'C';
                            cellWhiteMummies[pos].leftKeyPosition();
                        }
                        char nextPosition = matrix[lineMummy][columnMummy += 2];

                        killEnemy(lineMummy, columnMummy, nextPosition);
                        matrix[lineMummy][columnMummy] = 'M';

                        movements.add(convertMatrixToString(matrix));
                        if (whiteMummies > 1) {
                            if (nextPosition == 'A') {
                                cellWhiteMummies[pos].setPositionSteppedOnTrap(lineMummy, columnMummy);
                            } else if(nextPosition == 'C'){
                                cellWhiteMummies[pos].setPositionSteppedOnKey(lineMummy, columnMummy);
                            }else{
                                cellWhiteMummies[pos].setColumn(columnMummy);
                            }

                        } else {
                            if (nextPosition == 'A') {
                                cellWhiteMummies[0].setPositionSteppedOnTrap(lineMummy, columnMummy);
                            } else if(nextPosition == 'C'){
                                cellWhiteMummies[0].setPositionSteppedOnKey(lineMummy, columnMummy);
                            }else{
                                cellWhiteMummies[0].setColumn(columnMummy);
                            }
                        }
                        continue;
                    }
                } else {
                    //Left
                    if (canMoveLeft(lineMummy, columnMummy)) {
                        matrix[lineMummy][columnMummy] = '.';
                        if (cellWhiteMummies[pos].hasSteppedOnTrap()) {
                            matrix[lineMummy][columnMummy] = 'A';
                            cellWhiteMummies[pos].leftTrapPosition();
                        } else if (cellWhiteMummies[pos].hasSteppedOnKey()) {
                            matrix[lineMummy][columnMummy] = 'C';
                            cellWhiteMummies[pos].leftKeyPosition();
                        }
                        char nextPosition = matrix[lineMummy][columnMummy -= 2];

                        killEnemy(lineMummy, columnMummy, nextPosition);
                        matrix[lineMummy][columnMummy] = 'M';

                        movements.add(convertMatrixToString(matrix));
                        if (whiteMummies > 1) {
                            if (nextPosition == 'A') {
                                cellWhiteMummies[pos].setPositionSteppedOnTrap(lineMummy, columnMummy);
                            } else if(nextPosition == 'C') {
                                cellWhiteMummies[pos].setPositionSteppedOnKey(lineMummy, columnMummy);
                            }else{
                                cellWhiteMummies[pos].setColumn(columnMummy);
                            }
                        } else {
                            if (nextPosition == 'A') {
                                cellWhiteMummies[0].setPositionSteppedOnTrap(lineMummy, columnMummy);
                            } else if(nextPosition == 'C') {
                                cellWhiteMummies[pos].setPositionSteppedOnKey(lineMummy, columnMummy);
                            }else {
                                cellWhiteMummies[0].setColumn(columnMummy);
                            }
                        }
                        continue;
                    }
                }
            }

            if (cellHero.getLine() != lineMummy) {
                if (cellHero.getLine() > lineMummy) {
                    //Down
                    if (canMoveDown(lineMummy, columnMummy)) {
                        matrix[lineMummy][columnMummy] = '.';
                        if (cellWhiteMummies[pos].hasSteppedOnTrap()) {
                            matrix[lineMummy][columnMummy] = 'A';
                            cellWhiteMummies[pos].leftTrapPosition();
                        } else if (cellWhiteMummies[pos].hasSteppedOnKey()) {
                            matrix[lineMummy][columnMummy] = 'C';
                            cellWhiteMummies[pos].leftKeyPosition();
                        }
                        char nextPosition = matrix[lineMummy += 2][columnMummy];
                        killEnemy(lineMummy, columnMummy, nextPosition);
                        matrix[lineMummy][columnMummy] = 'M';

                        movements.add(convertMatrixToString(matrix));
                        if (whiteMummies > 1) {
                            if (nextPosition == 'A') {
                                cellWhiteMummies[pos].setPositionSteppedOnTrap(lineMummy, columnMummy);
                            }else if(nextPosition == 'C') {
                                cellWhiteMummies[pos].setPositionSteppedOnKey(lineMummy, columnMummy);
                            }else{
                                cellWhiteMummies[pos].setLine(lineMummy);
                            }
                        } else {
                            if (nextPosition == 'A') {
                                cellWhiteMummies[0].setPositionSteppedOnTrap(lineMummy, columnMummy);
                            }else if(nextPosition == 'C') {
                                cellWhiteMummies[0].setPositionSteppedOnKey(lineMummy, columnMummy);
                            }else{
                                cellWhiteMummies[0].setLine(lineMummy);
                            }
                        }
                    }
                } else {
                    //Up
                    if (canMoveUp(lineMummy, columnMummy)) {
                        matrix[lineMummy][columnMummy] = '.';
                        if (cellWhiteMummies[pos].hasSteppedOnTrap()) {
                            matrix[lineMummy][columnMummy] = 'A';
                            cellWhiteMummies[pos].leftTrapPosition();
                        } else if (cellWhiteMummies[pos].hasSteppedOnKey()) {
                            matrix[lineMummy][columnMummy] = 'C';
                            cellWhiteMummies[pos].leftKeyPosition();
                        }
                        char nextPosition = matrix[lineMummy -= 2][columnMummy];
                        killEnemy(lineMummy, columnMummy, nextPosition);
                        matrix[lineMummy][columnMummy] = 'M';

                        movements.add(convertMatrixToString(matrix));
                        if (whiteMummies > 1) {
                            if (nextPosition == 'A') {
                                cellWhiteMummies[pos].setPositionSteppedOnTrap(lineMummy, columnMummy);
                            } else if(nextPosition == 'C') {
                                cellWhiteMummies[pos].setPositionSteppedOnKey(lineMummy, columnMummy);
                            } else {
                                cellWhiteMummies[pos].setLine(lineMummy);
                            }
                        } else {
                            if (nextPosition == 'A') {
                                cellWhiteMummies[0].setPositionSteppedOnTrap(lineMummy, columnMummy);
                            } else if(nextPosition == 'C') {
                                cellWhiteMummies[0].setPositionSteppedOnKey(lineMummy, columnMummy);
                            } else {
                                cellWhiteMummies[0].setLine(lineMummy);
                            }
                        }
                    }
                }
            }
            hasKilledHero(lineMummy, columnMummy);
        }
    }

    private void moveRedMummy(int pos, List<String> movements) {
        int lineMummy = lineRedMummies[pos];
        int columnMummy = columnRedMummies[pos];

        for (int i = 0; i < 2; i++) {
            if(hasKilledHero(lineMummy, columnMummy) || gameOver){
                break;
            }
            //linhas primeiro
            if (cellHero.getLine() != lineMummy) {
                if (cellHero.getLine() > lineMummy) {
                    //Down
                    if (canMoveDown(lineMummy, columnMummy)) {
                        matrix[lineMummy][columnMummy] = '.';
                        char nextPosition = matrix[lineMummy+=2][columnMummy];
                        killEnemy(lineMummy, columnMummy, nextPosition);
                        matrix[lineMummy][columnMummy] = 'V';

                        movements.add(convertMatrixToString(matrix));
                        if(redMummies>1) {
                            lineRedMummies[pos] = lineMummy;
                        }else{
                            lineRedMummies[0] = lineMummy;
                        }
//                        hasKilledHero(lineMummy, columnMummy);
                        continue;
                    }
                } else {
                    //Up
                    if (canMoveUp(lineMummy, columnMummy)) {
                        matrix[lineMummy][columnMummy] = '.';
                        char nextPosition = matrix[lineMummy-=2][columnMummy];
                        killEnemy(lineMummy, columnMummy, nextPosition);
                        matrix[lineMummy][columnMummy] = 'V';

                        movements.add(convertMatrixToString(matrix));
                        if(redMummies>1) {
                            lineRedMummies[pos] = lineMummy;
                        }else{
                            lineRedMummies[0] = lineMummy;
                        }
//                        hasKilledHero(lineMummy, columnMummy);
                        continue;
                    }
                }
            }

            hasKilledHero(lineMummy, columnMummy);

            if (columnMummy != cellHero.getColumn()) {
                if (cellHero.getColumn() > columnMummy) {
                    //Right
                    if (canMoveRight(lineMummy, columnMummy)) {
                        matrix[lineMummy][columnMummy] = '.';
                        char nextPosition = matrix[lineMummy][columnMummy+=2];
                        killEnemy(lineMummy, columnMummy, nextPosition);
                        matrix[lineMummy][columnMummy] = 'V';

                        movements.add(convertMatrixToString(matrix));
                        if(redMummies>1) {
                            columnRedMummies[pos] = columnMummy;
                        }else{
                            columnRedMummies[0] = columnMummy;
                        }
//                        hasKilledHero(lineMummy, columnMummy);
                    }
                } else {
                    //Left
                    if (canMoveLeft(lineMummy, columnMummy)) {
                        matrix[lineMummy][columnMummy] = '.';
                        char nextPosition = matrix[lineMummy][columnMummy-=2];
                        killEnemy(lineMummy, columnMummy, nextPosition);
                        matrix[lineMummy][columnMummy] = 'V';

                        movements.add(convertMatrixToString(matrix));
                        if(redMummies>1) {
                            columnRedMummies[pos] = columnMummy;
                        }else{
                            columnRedMummies[0] = columnMummy;
                        }
//                        hasKilledHero(lineMummy, columnMummy);
                    }
                }
            }
        }
    }

    private boolean hasKilledHero(int lineEnemy, int columnEnemy) {
        if(columnEnemy == cellHero.getColumn()) {
            if(canMoveUp(lineEnemy, columnEnemy) && ((lineEnemy - 2) == cellHero.getLine())) {
                return gameOver = true;
            }
            if(canMoveDown(lineEnemy, columnEnemy) && ((lineEnemy + 2) == cellHero.getLine())) {
                return gameOver = true;
            }
            if(lineEnemy == cellHero.getLine()) {
                return gameOver = true;
            }
        }

        if(lineEnemy == cellHero.getLine()) {
            if(canMoveLeft(lineEnemy, columnEnemy) && ((columnEnemy - 2) == cellHero.getColumn())) {
                return gameOver = true;
            }
            if(canMoveRight(lineEnemy, columnEnemy) && ((columnEnemy + 2) == cellHero.getColumn())) {
                gameOver = true;
            }
        }
//        if((canMoveUp(lineEnemy, columnEnemy) && matrix[lineEnemy-2][columnEnemy] == 'H') ||
//            (canMoveDown(lineEnemy, columnEnemy) && matrix[lineEnemy+2][columnEnemy] == 'H') ||
//        if((canMoveUp(lineEnemy, columnEnemy) && matrix[lineEnemy-2][columnEnemy] == 'H')
//            (canMoveDown(lineEnemy, columnEnemy) && matrix[lineEnemy+2][columnEnemy] == 'H')
//            (canMoveLeft(lineEnemy, columnEnemy) && matrix[lineEnemy][columnEnemy-2] == 'H') ||
//            (canMoveRight(lineEnemy, columnEnemy) && matrix[lineEnemy][columnEnemy+2] == 'H')){
//            gameOver = true;
//        }
        return gameOver;
    }


    public List<String> moveUp() {
        List<String> movements = new ArrayList<>();
        // Hero's movement
        int columnHero = cellHero.getColumn();
        int lineHero = cellHero.getLine();
        char nextPosition = matrix[lineHero-2][columnHero];
        if(nextPosition == 'A' || nextPosition == 'M' || nextPosition == 'V' || nextPosition == 'E'){
            gameOver = true;
        }

        if(cellHero.hasSteppedOnKey()){
            matrix[lineHero][columnHero] = 'C';
            cellHero.leftKeyPosition();
        }else{
            matrix[lineHero][columnHero] = '.';//matrix[lineHero-=2][columnHero]; // '.'
        }
        lineHero-=2;

        if(nextPosition=='C'){
            for (int i = 0; i < horizontalDoors; i++) {
                if(matrix[lineHorizontalDoors[i]][columnHorizontalDoors[i]]=='='){
                    matrix[lineHorizontalDoors[i]][columnHorizontalDoors[i]]='_';
                }else {
                    matrix[lineHorizontalDoors[i]][columnHorizontalDoors[i]]='=';
                }
            }
            for (int i = 0; i < verticalDoors; i++) {
                if(matrix[lineVerticalDoors[i]][columnVerticalDoors[i]]=='"'){
                    matrix[lineVerticalDoors[i]][columnVerticalDoors[i]]=')';
                }else {
                    matrix[lineVerticalDoors[i]][columnVerticalDoors[i]]='"';
                }
            }
            cellHero.setPositionSteppedOnKey(lineHero, columnHero);
        }

        matrix[lineHero][columnHero] = 'H';
        cellHero.setLine(lineHero);

        movements.add(convertMatrixToString(matrix));

        int n = whiteMummies;
        for (int whiteM = 0; whiteM < n; whiteM++) {
            moveWhiteMummy(whiteM, movements);
//            hasKilledHero(lineWhiteMummies[whiteM], columnWhiteMummies[whiteM]);
            if(n>whiteMummies){ //se um mummy morreu, o numero de mummies diminui (para nao fazer o ciclo com o nº de mummies anterior)
                n = whiteMummies;
            }
        }

        //TODO - moveScorpion()
        n = scorpions;
        for (int scorpion = 0; scorpion < n; scorpion++) {
            moveScorpion(scorpion, movements);
            hasKilledHero(cellScorpions[scorpion].getLine(), cellScorpions[scorpion].getColumn());//lineScorpions[scorpion], columnScorpions[scorpion]);
            if(n>scorpions){
                n = scorpions;
            }
        }

        //TODO - redMummies()
        n = redMummies;
        for(int redM = 0; redM < n; redM++){
            moveRedMummy(redM, movements);
//            hasKilledHero(lineRedMummies[redM], columnRedMummies[redM]);
            if(n>redMummies){
                n = redMummies;
            }
        }

        return movements;
    }

    public List<String> moveRight() {
        List<String> movements = new ArrayList<>();
        // Hero's movement
        int lineHero = cellHero.getLine();
        int columnHero = cellHero.getColumn();
        char nextPosition = matrix[lineHero][columnHero+2];
        if(nextPosition == 'A' || nextPosition == 'M' || nextPosition == 'V' || nextPosition == 'E'){
            gameOver = true;
        }

        if(cellHero.hasSteppedOnKey()){
            matrix[lineHero][columnHero] = 'C';
            cellHero.leftKeyPosition();
        }else{
            matrix[lineHero][columnHero] = '.';//matrix[lineHero][columnHero+=2];
        }
        columnHero+=2;

        if(nextPosition == 'C'){
            for (int i = 0; i < horizontalDoors; i++) {
                if(matrix[lineHorizontalDoors[i]][columnHorizontalDoors[i]]=='='){
                    matrix[lineHorizontalDoors[i]][columnHorizontalDoors[i]]='_';
                }else {
                    matrix[lineHorizontalDoors[i]][columnHorizontalDoors[i]]='=';
                }
            }
            for (int i = 0; i < verticalDoors; i++) {
                if(matrix[lineVerticalDoors[i]][columnVerticalDoors[i]]=='"'){
                    matrix[lineVerticalDoors[i]][columnVerticalDoors[i]]=')';
                }else {
                    matrix[lineVerticalDoors[i]][columnVerticalDoors[i]]='"';
                }
            }
            cellHero.setPositionSteppedOnKey(lineHero, columnHero);
        }

        matrix[lineHero][columnHero] = 'H';
        cellHero.setColumn(columnHero);

        movements.add(convertMatrixToString(matrix));

        for (int whiteM = 0; whiteM < whiteMummies; whiteM++) {
            moveWhiteMummy(whiteM, movements);
//            hasKilledHero(lineWhiteMummies[whiteM], columnWhiteMummies[whiteM]);
        }

        for (int scorpion = 0; scorpion < scorpions; scorpion++) {
            moveScorpion(scorpion, movements);
            hasKilledHero(cellScorpions[scorpion].getLine(), cellScorpions[scorpion].getColumn());//lineScorpions[scorpion], columnScorpions[scorpion]);
        }

        for(int redM = 0; redM < redMummies; redM++){
            moveRedMummy(redM, movements);
//            hasKilledHero(lineRedMummies[redM], columnRedMummies[redM]);
        }

        return movements;
    }

    public List<String> moveDown() {
        List<String> movements = new ArrayList<>();
        // Hero's movement
        int lineHero = cellHero.getLine();
        int columnHero = cellHero.getColumn();
        char nextPosition = matrix[lineHero+2][columnHero];
        if(nextPosition == 'A' || nextPosition == 'M' || nextPosition == 'V' || nextPosition == 'E'){
            gameOver = true;
        }

        if(cellHero.hasSteppedOnKey()){ //se ele tiver pisado numa chave antes, então coloca na posição anterior a chave
            matrix[lineHero][columnHero] = 'C';
            cellHero.leftKeyPosition();
        }else{
            matrix[lineHero][columnHero] = '.';//matrix[lineHero+=2][columnHero];
        }
        lineHero+=2;

        if(nextPosition == 'C'){
            for (int i = 0; i < horizontalDoors; i++) {
                if(matrix[lineHorizontalDoors[i]][columnHorizontalDoors[i]]=='='){
                    matrix[lineHorizontalDoors[i]][columnHorizontalDoors[i]]='_';
                }else {
                    matrix[lineHorizontalDoors[i]][columnHorizontalDoors[i]]='=';
                }
            }
            for (int i = 0; i < verticalDoors; i++) {
                if(matrix[lineVerticalDoors[i]][columnVerticalDoors[i]]=='"'){
                    matrix[lineVerticalDoors[i]][columnVerticalDoors[i]]=')';
                }else {
                    matrix[lineVerticalDoors[i]][columnVerticalDoors[i]]='"';
                }
            }
            cellHero.setPositionSteppedOnKey(lineHero, columnHero);
        }


        matrix[lineHero][columnHero] = 'H';
        cellHero.setLine(lineHero);
        movements.add(convertMatrixToString(matrix));

        for (int whiteM = 0; whiteM < whiteMummies; whiteM++) {
            moveWhiteMummy(whiteM, movements);
//            hasKilledHero(lineWhiteMummies[whiteM], columnWhiteMummies[whiteM]);
        }

        for (int scorpion = 0; scorpion < scorpions; scorpion++) {
            moveScorpion(scorpion, movements);
            hasKilledHero(cellScorpions[scorpion].getLine(), cellScorpions[scorpion].getColumn());//lineScorpions[scorpion], columnScorpions[scorpion]);
        }

        for(int redM = 0; redM < redMummies; redM++){
            moveRedMummy(redM, movements);
//            hasKilledHero(lineRedMummies[redM], columnRedMummies[redM]);
        }

        return movements;
    }

    public List<String> moveLeft() {
        List<String> movements = new ArrayList<>();
        // Hero's movement
        int lineHero = cellHero.getLine();
        int columnHero = cellHero.getColumn();
        char nextPosition = matrix[lineHero][columnHero-2];
        if(nextPosition == 'A' || nextPosition == 'M' || nextPosition == 'V' || nextPosition == 'E'){
            gameOver = true;
        }

        if(cellHero.hasSteppedOnKey()){ //se ele tiver pisado numa chave antes, então coloca na posição anterior a chave
            matrix[lineHero][columnHero] = 'C';
            cellHero.leftKeyPosition();
        }else{
            matrix[lineHero][columnHero] = '.'; //matrix[lineHero][columnHero-=2];
        }
        columnHero-=2;

        if(nextPosition=='C'){
            for (int i = 0; i < horizontalDoors; i++) {
                if(matrix[lineHorizontalDoors[i]][columnHorizontalDoors[i]]=='='){
                    matrix[lineHorizontalDoors[i]][columnHorizontalDoors[i]]='_';
                }else {
                    matrix[lineHorizontalDoors[i]][columnHorizontalDoors[i]]='=';
                }
            }
            for (int i = 0; i < verticalDoors; i++) {
                if(matrix[lineVerticalDoors[i]][columnVerticalDoors[i]]=='"'){
                    matrix[lineVerticalDoors[i]][columnVerticalDoors[i]]=')';
                }else {
                    matrix[lineVerticalDoors[i]][columnVerticalDoors[i]]='"';
                }
            }
            cellHero.setPositionSteppedOnKey(lineHero, columnHero);
        }

        matrix[lineHero][columnHero] = 'H';
        cellHero.setColumn(columnHero);

        movements.add(convertMatrixToString(matrix));

        for (int whiteM = 0; whiteM < whiteMummies; whiteM++) {
            moveWhiteMummy(whiteM, movements);
//            hasKilledHero(lineWhiteMummies[whiteM], columnWhiteMummies[whiteM]);
        }

        //TODO - moveScorpion()
        for (int scorpion = 0; scorpion < scorpions; scorpion++) {
            moveScorpion(scorpion, movements);
            hasKilledHero(cellScorpions[scorpion].getLine(), cellScorpions[scorpion].getColumn());//lineScorpions[scorpion], columnScorpions[scorpion]);
        }

        //TODO - redMummies()
        for(int redM = 0; redM < redMummies; redM++){
            moveRedMummy(redM, movements);
//            hasKilledHero(lineRedMummies[redM], columnRedMummies[redM]);
        }

        return movements;
    }

    public List<String> moveStandStill(){
        List<String> movements = new ArrayList<>();

        for (int whiteM = 0; whiteM < whiteMummies; whiteM++) {
            moveWhiteMummy(whiteM, movements);
//            hasKilledHero(lineWhiteMummies[whiteM], columnWhiteMummies[whiteM]);
        }

        //TODO - moveScorpion()
        for (int scorpion = 0; scorpion < scorpions; scorpion++) {
            moveScorpion(scorpion, movements);
            hasKilledHero(cellScorpions[scorpion].getLine(), cellScorpions[scorpion].getColumn());//lineScorpions[scorpion], columnScorpions[scorpion]);
        }

        //TODO - redMummies()
        for(int redM = 0; redM < redMummies; redM++){
            moveRedMummy(redM, movements);
//            hasKilledHero(lineRedMummies[redM], columnRedMummies[redM]);
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
        return !gameOver && canMoveRight(cellHero.getLine(), cellHero.getColumn());
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
        return !gameOver && canMoveLeft(cellHero.getLine(), cellHero.getColumn());
    }

    public boolean canMoveUp(int lineEntity, int columnEntity){
        //moves off limits?
        if(lineEntity == 1/* < 2*/){
            return false;
        }
        int lineUp = lineEntity - 1;
        //has something blocking hero's path?
        return matrix[lineUp][columnEntity] != '|' && matrix[lineUp][columnEntity] != '-' &&
                matrix[lineUp][columnEntity] != '=' && matrix[lineUp][columnEntity] != '"';
    }

    public boolean canMoveUpHero(){
        return !gameOver && canMoveUp(cellHero.getLine(), cellHero.getColumn());
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
        return !gameOver && canMoveDown(cellHero.getLine(), cellHero.getColumn());
    }

    public double computeExitDistance() {
        return ((Math.abs(cellHero.getLine() - lineExit) + Math.abs(cellHero.getColumn() - columnExit))-1)/ (double) 2;
    }

    public String convertMatrixToString(char[][] matrix) {
        // Matriz -> String
        StringBuilder s= new StringBuilder();
//        String s1 = "";
        for (int k = 0; k < 13; k++) {
            s.append(String.valueOf(matrix[k])).append("\n");
//            s1 += String.valueOf(matrix[k])+"\n";
        }

        return s.toString();
//        return s1;
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

    public void resetEnemies() {
        scorpions = 0;
//        lineScorpions = new int[1];
//        columnScorpions = new int[1];
        cellScorpions = new Cell[1];
        cellScorpions[0] = new Cell();

        whiteMummies = 0;
//        lineWhiteMummies = new int[1];
//        columnWhiteMummies = new int[1];
        cellWhiteMummies = new Cell[1];
        cellWhiteMummies[0] = new Cell();

        lineRedMummies = new int[1];
        columnRedMummies = new int[1];
        redMummies = 0;

        lineHorizontalDoors = new int[1];
        columnHorizontalDoors = new int[1];
        horizontalDoors = 0;

        lineVerticalDoors = new int[1];
        columnVerticalDoors = new int[1];
        verticalDoors = 0;

        lineTraps = new int[1];
        columnTraps = new int[1];
        traps = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MummyMazeState that = (MummyMazeState) o;
        return  cellHero.equals(that.cellHero) && Arrays.equals(cellWhiteMummies, that.cellWhiteMummies) &&
                Arrays.equals(cellScorpions, that.cellScorpions) &&
                Arrays.equals(lineRedMummies, that.lineRedMummies) &&
                Arrays.equals(columnRedMummies, that.columnRedMummies) &&
                Arrays.deepEquals(matrix, that.matrix);
    }

    @Override
    public int hashCode() {
//        int result = Objects.hash(lineHero, columnHero);
        int result = cellHero.hashCode();
        result = 31 * result + Arrays.deepHashCode(matrix);
        result = 31 * result + Arrays.hashCode(cellWhiteMummies);
        result = 31 * result + Arrays.hashCode(cellScorpions);
        result = 31 * result + Arrays.hashCode(lineRedMummies);
        result = 31 * result + Arrays.hashCode(columnRedMummies);
        result = 31 * result + Arrays.hashCode(lineHorizontalDoors);
        result = 31 * result + Arrays.hashCode(columnHorizontalDoors);
        result = 31 * result + Arrays.hashCode(lineVerticalDoors);
        result = 31 * result + Arrays.hashCode(columnVerticalDoors);
        result = 31 * result + Objects.hash(lineKey, columnKey);
        result = 31 * result + Arrays.hashCode(lineTraps);
        result = 31 * result + Arrays.hashCode(columnTraps);
        return result;
    }
}
