package mummymaze;

import agent.Action;
import agent.State;
import gui.MainFrame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.Arrays.copyOf;


public class MummyMazeState extends State implements Cloneable{

    private final char[][] matrix; //posições válidas : do [1][1] ao [1][11] (na horizontal) e [1][1] ao [11][1] (na vertical)

    private Cell cellHero; //cell where the hero is
    private Cell cellExit;
    private Cell cellKey;

    private Cell[] cellWhiteMummies;
    private int whiteMummies;

    private Cell[] cellScorpions;
    private int scorpions;

    private Cell[] cellRedMummies;
    private int redMummies;

    private Cell[] cellHorizontalDoors;
    private int horizontalDoors;

    private Cell[] cellVerticalDoors;
    private int verticalDoors;

    private Cell[] cellTraps;
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
                        cellHero = new Cell(i,j);
                        break;
                    case 'S': //stores the exit position in the matrix
                        cellExit = new Cell(i,j);
                        break;
                    case 'M': //stores the white mummy's position in the matrix
                        if(whiteMummies!=0) {
                            cellWhiteMummies = copyOf(cellWhiteMummies, whiteMummies + 1);
                        }

                        cellWhiteMummies[whiteMummies] = new Cell(i, j);
                        whiteMummies++;
                        break;
                    case 'E': //stores the scorpion's position in the matrix
                        if (scorpions != 0) {
                            cellScorpions = copyOf(cellScorpions, scorpions+1);
                        }

                        cellScorpions[scorpions] = new Cell(i,j);
                        scorpions++;
                        break;

                    case 'V': //stores the red mummy's position in the matrix

                        if (redMummies != 0) {
                            cellRedMummies = copyOf(cellRedMummies, redMummies+1);
                        }

                        cellRedMummies[redMummies] = new Cell(i,j);
                        redMummies++;
                        break;
                    case '=','_':
                        if (horizontalDoors != 0) {
                            cellHorizontalDoors = copyOf(cellHorizontalDoors, horizontalDoors+1);
                        }

                        cellHorizontalDoors[horizontalDoors] = new Cell(i,j);
                        horizontalDoors++;
                        break;
                    case '"',')':
                        if (verticalDoors != 0) {
                            cellVerticalDoors = copyOf(cellVerticalDoors, verticalDoors+1);
                        }

                        cellVerticalDoors[verticalDoors] = new Cell(i,j);
                        verticalDoors++;
                        break;
                    case 'C':
                        cellKey = new Cell(i,j);
                        break;
                    case 'A':
                        if (traps != 0) {
                            cellTraps = copyOf(cellTraps, traps+1);
                        }

                        cellTraps[traps] = new Cell(i,j);
                        traps++;
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

    private String moveScorpion(int pos){
        int lineScorpion = cellScorpions[pos].getLine();//lineScorpions[pos];
        int columnScorpion = cellScorpions[pos].getColumn();//columnScorpions[pos];

        if(hasKilledHero(lineScorpion, columnScorpion) || gameOver){
            return null;
        }
        // Colunas primeiro
        int columnHero = cellHero.getColumn();
        if (columnScorpion != columnHero) {
            if (columnHero > columnScorpion) {
                //Right
                if (canMoveRight(lineScorpion, columnScorpion)) {
                    for (int i=0; i<traps;i++){
                        if(cellTraps[i].equals(cellScorpions[pos])){
                            matrix[lineScorpion][columnScorpion] = 'A';
                        }
                    }
                    if(cellKey!=null && cellKey.equals(cellScorpions[pos])){
                        matrix[lineScorpion][columnScorpion] = 'C';
                    }else if(matrix[lineScorpion][columnScorpion] != 'A'){
                        matrix[lineScorpion][columnScorpion] = '.';
                    }

                    // Analyse if there's an enemy to kill
                    char nextPosition = matrix[lineScorpion][columnScorpion+=2];
                    pos = killEnemy(lineScorpion, columnScorpion, nextPosition,'E', pos);

                    matrix[lineScorpion][columnScorpion] = 'E';

                    cellScorpions[pos].setPosition(lineScorpion,columnScorpion);

                    return convertMatrixToString(matrix);
                }
            } else {
                //Left
                if (canMoveLeft(lineScorpion, columnScorpion)) {
                    for (int i=0; i<traps;i++){
                        if(cellTraps[i].equals(cellScorpions[pos])){
                            matrix[lineScorpion][columnScorpion] = 'A';
                        }
                    }
                    if(cellKey!=null && cellKey.equals(cellScorpions[pos])){
                        matrix[lineScorpion][columnScorpion] = 'C';
                    }else if(matrix[lineScorpion][columnScorpion] != 'A'){
                        matrix[lineScorpion][columnScorpion] = '.';
                    }

                    // Analyse if there's an enemy to kill
                    char nextPosition = matrix[lineScorpion][columnScorpion-=2];
                    pos = killEnemy(lineScorpion, columnScorpion, nextPosition,'E', pos);

                    matrix[lineScorpion][columnScorpion] = 'E';

                    cellScorpions[pos].setPosition(lineScorpion,columnScorpion);

                    return convertMatrixToString(matrix);
                }
            }

        }
        int lineHero = cellHero.getLine();
        if (lineHero != lineScorpion) {
            if (lineHero > lineScorpion) {
                //Down
                if (canMoveDown(lineScorpion, columnScorpion)) {
                    for (int i=0; i<traps;i++){
                        if(cellTraps[i].equals(cellScorpions[pos])){
                            matrix[lineScorpion][columnScorpion] = 'A';
                        }
                    }
                    if(cellKey!=null && cellKey.equals(cellScorpions[pos])){
                        matrix[lineScorpion][columnScorpion] = 'C';
                    }else if(matrix[lineScorpion][columnScorpion] != 'A'){
                        matrix[lineScorpion][columnScorpion] = '.';
                    }

                    // Analyse if there's an enemy to kill
                    char nextPosition = matrix[lineScorpion+=2][columnScorpion];

                    pos = killEnemy(lineScorpion, columnScorpion, nextPosition,'E', pos);

                    matrix[lineScorpion][columnScorpion] = 'E';

                    cellScorpions[pos].setPosition(lineScorpion,columnScorpion);

                    return convertMatrixToString(matrix);
                }
            } else {
                //Up
                if (canMoveUp(lineScorpion, columnScorpion)) {
                    for (int i=0; i<traps;i++){
                        if(cellTraps[i].equals(cellScorpions[pos])){
                            matrix[lineScorpion][columnScorpion] = 'A';
                        }
                    }
                    if(cellKey!=null && cellKey.equals(cellScorpions[pos])){
                        matrix[lineScorpion][columnScorpion] = 'C';
                    }else if(matrix[lineScorpion][columnScorpion] != 'A'){
                        matrix[lineScorpion][columnScorpion] = '.';
                    }

                    // Analyse if there's an enemy to kill
                    char nextPosition = matrix[lineScorpion-=2][columnScorpion];

                    pos = killEnemy(lineScorpion, columnScorpion, nextPosition,'E', pos);

                    matrix[lineScorpion][columnScorpion] = 'E';

                    cellScorpions[pos].setPosition(lineScorpion,columnScorpion);

                    return convertMatrixToString(matrix);
                }
            }

        }

        return null;
    }

    private int killEnemy(int lineEnemy, int columnEnemy, char nextPosition, char typeEnemy, int pos) {
        int newIndex = pos;
        switch(nextPosition){
            case 'M':
                Cell[] newWhiteMummies;
                if(whiteMummies > 1){
                   newWhiteMummies = new Cell[whiteMummies-1];
                }else{
                    newWhiteMummies = new Cell[1];
                }

                for (int i=0,j=0; i<whiteMummies; i++,j++){
                    if(pos==i){
                        j--;
                        continue;
                    }

                    int line = cellWhiteMummies[i].getLine();
                    int column = cellWhiteMummies[i].getColumn();
                    newWhiteMummies[j] = new Cell(line, column);
                    if(line == lineEnemy && column == columnEnemy && typeEnemy=='M'){
                        newIndex=j;
                    }
                }

                whiteMummies--;
                cellWhiteMummies = copyOf(newWhiteMummies, whiteMummies);
                break;
            case 'V':
                Cell[] newRedMummies;
                if(redMummies > 1){
                    newRedMummies = new Cell[redMummies-1];
                }else{
                    newRedMummies = new Cell[1];
                }

                for (int i=0,j=0; i<redMummies; i++,j++){
                    if(pos==i){
                        j--;
                        continue;
                    }

                    int line = cellRedMummies[i].getLine();
                    int column = cellRedMummies[i].getColumn();
                    newRedMummies[j] = new Cell(line,column);
                    if(line == lineEnemy && column == columnEnemy && typeEnemy=='V'){
                        newIndex = j;
                    }

                }
                redMummies--;
                cellRedMummies = copyOf(newRedMummies, redMummies);
                break;
            case 'E':
                Cell[] newScorpions;
                if(scorpions > 1){
                     newScorpions = new Cell[scorpions-1];
                }else {
                    newScorpions = new Cell[1];
                }

                for (int i=0,j=0; i<scorpions; i++,j++){
                    if(pos==i){
                        j--;
                        continue;
                    }

                    int line = cellScorpions[i].getLine();
                    int column = cellScorpions[i].getColumn();
                    newScorpions[j] = new Cell(line, column);
                    if(line == lineEnemy && column == columnEnemy && typeEnemy=='E'){
                        newIndex = j;
                    }

                }
                scorpions--;
                cellScorpions = copyOf(newScorpions, scorpions);
                break;
        }
        return newIndex;
    }


    private void moveWhiteMummy(int pos, List<String> movements) {
        int lineMummy = cellWhiteMummies[pos].getLine();
        int columnMummy = cellWhiteMummies[pos].getColumn();

        int columnHero = cellHero.getColumn();
        int lineHero = cellHero.getLine();

        for (int n = 0; n < 2; n++) {
            if (hasKilledHero(lineMummy, columnMummy) || gameOver) {
                return;
            }

            // Colunas primeiro
            if (columnMummy != columnHero) {
                if (columnHero > columnMummy) {
                    //Right
                    if (canMoveRight(lineMummy, columnMummy)) {
                        for (int i=0; i<traps;i++){
                            if(cellTraps[i].equals(cellWhiteMummies[pos])){
                                matrix[lineMummy][columnMummy] = 'A';
                            }
                        }
                        if(cellKey!=null && cellKey.equals(cellWhiteMummies[pos])){
                            matrix[lineMummy][columnMummy] = 'C';
                        }else if(matrix[lineMummy][columnMummy] != 'A'){
                            matrix[lineMummy][columnMummy] = '.';
                        }

                        // Analyse if there's an enemy to kill
                        char nextPosition = matrix[lineMummy][columnMummy+=2];

                        pos = killEnemy(lineMummy, columnMummy, nextPosition,'M', pos);

                        matrix[lineMummy][columnMummy] = 'M';

                        cellWhiteMummies[pos].setPosition(lineMummy,columnMummy);

                        movements.add(convertMatrixToString(matrix));
                        continue;
                    }
                } else {
                    //Left
                    if (canMoveLeft(lineMummy, columnMummy)) {
                        for (int i=0; i<traps;i++){
                            if(cellTraps[i].equals(cellWhiteMummies[pos])){
                                matrix[lineMummy][columnMummy] = 'A';
                            }
                        }
                        if(cellKey!=null && cellKey.equals(cellWhiteMummies[pos])){
                            matrix[lineMummy][columnMummy] = 'C';
                        }else if(matrix[lineMummy][columnMummy] != 'A'){
                            matrix[lineMummy][columnMummy] = '.';
                        }

                        // Analyse if there's an enemy to kill
                        char nextPosition = matrix[lineMummy][columnMummy-=2];

                        pos = killEnemy(lineMummy, columnMummy, nextPosition,'M', pos);

                        matrix[lineMummy][columnMummy] = 'M';

                        cellWhiteMummies[pos].setPosition(lineMummy,columnMummy);

                        movements.add(convertMatrixToString(matrix));
                        continue;
                    }
                }
            }

            if (lineHero != lineMummy) {
                if (lineHero > lineMummy) {
                    //Down
                    if (canMoveDown(lineMummy, columnMummy)) {
                        for (int i=0; i<traps;i++){
                            if(cellTraps[i].equals(cellWhiteMummies[pos])){
                                matrix[lineMummy][columnMummy] = 'A';
                            }
                        }
                        if(cellKey!=null && cellKey.equals(cellWhiteMummies[pos])){
                            matrix[lineMummy][columnMummy] = 'C';
                        }else if(matrix[lineMummy][columnMummy] != 'A'){
                            matrix[lineMummy][columnMummy] = '.';
                        }

                        // Analyse if there's an enemy to kill
                        char nextPosition = matrix[lineMummy+=2][columnMummy];
                        pos = killEnemy(lineMummy, columnMummy, nextPosition,'M', pos);

                        matrix[lineMummy][columnMummy] = 'M';

                        cellWhiteMummies[pos].setPosition(lineMummy,columnMummy);

                        movements.add(convertMatrixToString(matrix));
                    }
                } else {
                    //Up
                    if (canMoveUp(lineMummy, columnMummy)) {
                        for (int i=0; i<traps;i++){
                            if(cellTraps[i].equals(cellWhiteMummies[pos])){
                                matrix[lineMummy][columnMummy] = 'A';
                            }
                        }
                        if(cellKey!=null && cellKey.equals(cellWhiteMummies[pos])){
                            matrix[lineMummy][columnMummy] = 'C';
                        }else if(matrix[lineMummy][columnMummy] != 'A'){
                            matrix[lineMummy][columnMummy] = '.';
                        }

                        // Analyse if there's an enemy to kill
                        char nextPosition = matrix[lineMummy-=2][columnMummy];

                        pos = killEnemy(lineMummy, columnMummy, nextPosition,'M', pos);

                        matrix[lineMummy][columnMummy] = 'M';

                        cellWhiteMummies[pos].setPosition(lineMummy,columnMummy);

                        movements.add(convertMatrixToString(matrix));
                    }
                }
            }

            hasKilledHero(lineMummy, columnMummy);
        }
    }

    private void moveRedMummy(int pos, List<String> movements) {
        int lineMummy = cellRedMummies[pos].getLine();
        int columnMummy = cellRedMummies[pos].getColumn();

        int columnHero = cellHero.getColumn();
        int lineHero = cellHero.getLine();

        for (int n = 0; n < 2; n++) {
            if(hasKilledHero(lineMummy, columnMummy) || gameOver){
                break;
            }
            //linhas primeiro
            if (lineHero != lineMummy) {
                if (lineHero > lineMummy) {
                    //Down
                    if (canMoveDown(lineMummy, columnMummy)) {
                        for (int i=0; i<traps;i++){
                            if(cellTraps[i].equals(cellRedMummies[pos])){
                                matrix[lineMummy][columnMummy] = 'A';
                            }
                        }
                        if(cellKey!=null && cellKey.equals(cellRedMummies[pos])){
                            matrix[lineMummy][columnMummy] = 'C';
                        }else if(matrix[lineMummy][columnMummy] != 'A'){
                            matrix[lineMummy][columnMummy] = '.';
                        }

                        // Analyse if there's an enemy to kill
                        char nextPosition = matrix[lineMummy+=2][columnMummy];
                        pos = killEnemy(lineMummy, columnMummy, nextPosition,'V', pos);

                        matrix[lineMummy][columnMummy] = 'V';

                        movements.add(convertMatrixToString(matrix));

                        cellRedMummies[pos].setPosition(lineMummy,columnMummy);
                        continue;
                    }
                } else {
                    //Up
                    if (canMoveUp(lineMummy, columnMummy)) {
                        for (int i=0; i<traps;i++){
                            if(cellTraps[i].equals(cellRedMummies[pos])){
                                matrix[lineMummy][columnMummy] = 'A';
                            }
                        }
                        if(cellKey!=null && cellKey.equals(cellRedMummies[pos])){
                            matrix[lineMummy][columnMummy] = 'C';
                        }else if(matrix[lineMummy][columnMummy] != 'A'){
                            matrix[lineMummy][columnMummy] = '.';
                        }

                        // Analyse if there's an enemy to kill
                        char nextPosition = matrix[lineMummy-=2][columnMummy];
                        pos = killEnemy(lineMummy, columnMummy, nextPosition,'V', pos);

                        matrix[lineMummy][columnMummy] = 'V';

                        movements.add(convertMatrixToString(matrix));
                        cellRedMummies[pos].setPosition(lineMummy,columnMummy);
                        continue;
                    }
                }
            }



            if (columnMummy != columnHero) {
                if (columnHero > columnMummy) {
                    //Right
                    if (canMoveRight(lineMummy, columnMummy)) {
                        for (int i=0; i<traps;i++){
                            if(cellTraps[i].equals(cellRedMummies[pos])){
                                matrix[lineMummy][columnMummy] = 'A';
                            }
                        }
                        if(cellKey!=null && cellKey.equals(cellRedMummies[pos])){
                            matrix[lineMummy][columnMummy] = 'C';
                        }else if(matrix[lineMummy][columnMummy] != 'A'){
                            matrix[lineMummy][columnMummy] = '.';
                        }

                        // Analyse if there's an enemy to kill
                        char nextPosition = matrix[lineMummy][columnMummy+=2];
                        pos = killEnemy(lineMummy, columnMummy, nextPosition,'V', pos);

                        matrix[lineMummy][columnMummy] = 'V';

                        movements.add(convertMatrixToString(matrix));
                        cellRedMummies[pos].setPosition(lineMummy,columnMummy);
                    }
                } else {
                    //Left
                    if (canMoveLeft(lineMummy, columnMummy)) {
                        for (int i=0; i<traps;i++){
                            if(cellTraps[i].equals(cellRedMummies[pos])){
                                matrix[lineMummy][columnMummy] = 'A';
                            }
                        }
                        if(cellKey!=null && cellKey.equals(cellRedMummies[pos])){
                            matrix[lineMummy][columnMummy] = 'C';
                        }else if(matrix[lineMummy][columnMummy] != 'A'){
                            matrix[lineMummy][columnMummy] = '.';
                        }

                        // Analyse if there's an enemy to kill
                        char nextPosition = matrix[lineMummy][columnMummy-=2];
                        pos = killEnemy(lineMummy, columnMummy, nextPosition,'V', pos);

                        matrix[lineMummy][columnMummy] = 'V';

                        movements.add(convertMatrixToString(matrix));
                        cellRedMummies[pos].setPosition(lineMummy,columnMummy);
                    }
                }
            }

            hasKilledHero(lineMummy, columnMummy);
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

        for (int i=0; i<traps;i++){
            if(cellTraps[i].equals(cellHero)){
                matrix[lineHero][columnHero] = 'A';
            }
        }
        if(cellKey!=null && cellKey.equals(cellHero)){
            matrix[lineHero][columnHero] = 'C';
        }else if(matrix[lineHero][columnHero] != 'A'){
            matrix[lineHero][columnHero] = '.';
        }

        lineHero-=2;

        if(nextPosition=='C'){
            for (int i = 0; i < horizontalDoors; i++) {
                int lineHorizontalDoors = cellHorizontalDoors[i].getLine();
                int columnHorizontalDoors = cellHorizontalDoors[i].getColumn();

                if (matrix[lineHorizontalDoors][columnHorizontalDoors]=='=') {
                    matrix[lineHorizontalDoors][columnHorizontalDoors] = '_';
                }else {
                    matrix[lineHorizontalDoors][columnHorizontalDoors] = '=';
                }
            }

            for (int i = 0; i < verticalDoors; i++) {
                int lineVerticalDoors = cellVerticalDoors[i].getLine();
                int columnVerticalDoors = cellVerticalDoors[i].getColumn();

                if(matrix[lineVerticalDoors][columnVerticalDoors]=='"'){
                    matrix[lineVerticalDoors][columnVerticalDoors]=')';
                }else {
                    matrix[lineVerticalDoors][columnVerticalDoors]='"';
                }
            }
        }

        matrix[lineHero][columnHero] = 'H';
        cellHero.setPosition(lineHero,columnHero);

        movements.add(convertMatrixToString(matrix));

        //TODO - moveWhiteMummies()
        int n = whiteMummies;
        for (int whiteM = 0; whiteM < n; whiteM++) {
            moveWhiteMummy(whiteM, movements);
            if(whiteMummies < n){ //se um mummy morreu, o numero de mummies diminui (para nao fazer o ciclo com o nº de mummies anterior)
                whiteM--;
                n--;
            }
        }

        //TODO - moveScorpion()
        n = scorpions;
        for (int scorpion = 0; scorpion < n; scorpion++) {
            String movement = moveScorpion(scorpion);
            if(movement!=null){
                movements.add(movement);
            }
            hasKilledHero(cellScorpions[scorpion].getLine(), cellScorpions[scorpion].getColumn());//lineScorpions[scorpion], columnScorpions[scorpion]);
            if(scorpions < n){
                scorpion--;
                n--;
            }
        }

        //TODO - redMummies()
        n = redMummies;
        for(int redM = 0; redM < n; redM++){
            moveRedMummy(redM, movements);
//            hasKilledHero(lineRedMummies[redM], columnRedMummies[redM]);
            if(redMummies < n){
                redM--;
                n--;
            }
        }

        return movements;
    }

    public List<String> moveRight() {
        List<String> movements = new ArrayList<>();

        int lineHero = cellHero.getLine();
        int columnHero = cellHero.getColumn();

        char nextPosition = matrix[lineHero][columnHero+2];
        if(nextPosition == 'A' || nextPosition == 'M' || nextPosition == 'V' || nextPosition == 'E'){
            gameOver = true;
        }

        for (int i=0; i<traps;i++){
            if(cellTraps[i].equals(cellHero)){
                matrix[lineHero][columnHero] = 'A';
            }
        }
        if(cellKey!=null && cellKey.equals(cellHero)){
            matrix[lineHero][columnHero] = 'C';
        }else if(matrix[lineHero][columnHero] != 'A'){
            matrix[lineHero][columnHero] = '.';
        }

        columnHero+=2;

        if(nextPosition == 'C'){
            for (int i = 0; i < horizontalDoors; i++) {
                int lineHorizontalDoors = cellHorizontalDoors[i].getLine();
                int columnHorizontalDoors = cellHorizontalDoors[i].getColumn();

                if (matrix[lineHorizontalDoors][columnHorizontalDoors]=='=') {
                    matrix[lineHorizontalDoors][columnHorizontalDoors] = '_';
                }else {
                    matrix[lineHorizontalDoors][columnHorizontalDoors] = '=';
                }
            }

            for (int i = 0; i < verticalDoors; i++) {
                int lineVerticalDoors = cellVerticalDoors[i].getLine();
                int columnVerticalDoors = cellVerticalDoors[i].getColumn();

                if(matrix[lineVerticalDoors][columnVerticalDoors]=='"'){
                    matrix[lineVerticalDoors][columnVerticalDoors]=')';
                }else {
                    matrix[lineVerticalDoors][columnVerticalDoors]='"';
                }
            }
        }

        matrix[lineHero][columnHero] = 'H';
        cellHero.setPosition(lineHero,columnHero);

        movements.add(convertMatrixToString(matrix));

        //TODO - moveWhiteMummies()
        int n = whiteMummies;
        for (int whiteM = 0; whiteM < n; whiteM++) {
            moveWhiteMummy(whiteM, movements);
            if(whiteMummies < n){ //se um mummy morreu, o numero de mummies diminui (para nao fazer o ciclo com o nº de mummies anterior)
                whiteM--;
                n--;
            }
        }

        //TODO - moveScorpion()
        n = scorpions;
        for (int scorpion = 0; scorpion < n; scorpion++) {
            String movement = moveScorpion(scorpion);
            if(movement!=null){
                movements.add(movement);
            }
            hasKilledHero(cellScorpions[scorpion].getLine(), cellScorpions[scorpion].getColumn());//lineScorpions[scorpion], columnScorpions[scorpion]);
            if(scorpions < n){
                scorpion--;
                n--;
            }
        }

        //TODO - redMummies()
        n = redMummies;
        for(int redM = 0; redM < n; redM++){
            moveRedMummy(redM, movements);
            if(redMummies < n){
                redM--;
                n--;
            }
        }

        return movements;
    }

    public List<String> moveDown() {
        List<String> movements = new ArrayList<>();

        int lineHero = cellHero.getLine();
        int columnHero = cellHero.getColumn();

        char nextPosition = matrix[lineHero+2][columnHero];
        if(nextPosition == 'A' || nextPosition == 'M' || nextPosition == 'V' || nextPosition == 'E'){
            gameOver = true;
        }

        for (int i=0; i<traps;i++){
            if(cellTraps[i].equals(cellHero)){
                matrix[lineHero][columnHero] = 'A';
            }
        }
        if(cellKey!=null && cellKey.equals(cellHero)){
            matrix[lineHero][columnHero] = 'C';
        }else if(matrix[lineHero][columnHero] != 'A'){
            matrix[lineHero][columnHero] = '.';
        }

        lineHero+=2;

        if(nextPosition == 'C'){
            for (int i = 0; i < horizontalDoors; i++) {
                int lineHorizontalDoors = cellHorizontalDoors[i].getLine();
                int columnHorizontalDoors = cellHorizontalDoors[i].getColumn();

                if (matrix[lineHorizontalDoors][columnHorizontalDoors]=='=') {
                    matrix[lineHorizontalDoors][columnHorizontalDoors] = '_';
                }else {
                    matrix[lineHorizontalDoors][columnHorizontalDoors] = '=';
                }
            }

            for (int i = 0; i < verticalDoors; i++) {
                int lineVerticalDoors = cellVerticalDoors[i].getLine();
                int columnVerticalDoors = cellVerticalDoors[i].getColumn();

                if(matrix[lineVerticalDoors][columnVerticalDoors]=='"'){
                    matrix[lineVerticalDoors][columnVerticalDoors]=')';
                }else {
                    matrix[lineVerticalDoors][columnVerticalDoors]='"';
                }
            }
        }

        matrix[lineHero][columnHero] = 'H';
        cellHero.setPosition(lineHero,columnHero);
        movements.add(convertMatrixToString(matrix));

        //TODO - moveWhiteMummies()
        int n = whiteMummies;
        for (int whiteM = 0; whiteM < n; whiteM++) {
            moveWhiteMummy(whiteM, movements);
            if(whiteMummies < n){ //se um mummy morreu, o numero de mummies diminui (para nao fazer o ciclo com o nº de mummies anterior)
                whiteM--;
                n--;
            }
        }

        //TODO - moveScorpion()
        n = scorpions;
        for (int scorpion = 0; scorpion < n; scorpion++) {
            String movement = moveScorpion(scorpion);
            if(movement!=null){
                movements.add(movement);
            }
            hasKilledHero(cellScorpions[scorpion].getLine(), cellScorpions[scorpion].getColumn());//lineScorpions[scorpion], columnScorpions[scorpion]);
            if(scorpions < n){
                scorpion--;
                n--;
            }
        }

        //TODO - redMummies()
        n = redMummies;
        for(int redM = 0; redM < n; redM++){
            moveRedMummy(redM, movements);
            if(redMummies < n){
                redM--;
                n--;
            }
        }

        return movements;
    }

    public List<String> moveLeft() {
        List<String> movements = new ArrayList<>();

        int lineHero = cellHero.getLine();
        int columnHero = cellHero.getColumn();

        char nextPosition = matrix[lineHero][columnHero-2];
        if(nextPosition == 'A' || nextPosition == 'M' || nextPosition == 'V' || nextPosition == 'E'){
            gameOver = true;
        }

        for (int i=0; i<traps;i++){
            if(cellTraps[i].equals(cellHero)){
                matrix[lineHero][columnHero] = 'A';
            }
        }
        if(cellKey!=null && cellKey.equals(cellHero)){
            matrix[lineHero][columnHero] = 'C';
        }else if(matrix[lineHero][columnHero] != 'A'){
            matrix[lineHero][columnHero] = '.';
        }

        columnHero-=2;

        if(nextPosition == 'C'){
            for (int i = 0; i < horizontalDoors; i++) {
                int lineHorizontalDoors = cellHorizontalDoors[i].getLine();
                int columnHorizontalDoors = cellHorizontalDoors[i].getColumn();

                if (matrix[lineHorizontalDoors][columnHorizontalDoors]=='=') {
                    matrix[lineHorizontalDoors][columnHorizontalDoors] = '_';
                }else {
                    matrix[lineHorizontalDoors][columnHorizontalDoors] = '=';
                }
            }

            for (int i = 0; i < verticalDoors; i++) {
                int lineVerticalDoors = cellVerticalDoors[i].getLine();
                int columnVerticalDoors = cellVerticalDoors[i].getColumn();

                if(matrix[lineVerticalDoors][columnVerticalDoors]=='"'){
                    matrix[lineVerticalDoors][columnVerticalDoors]=')';
                }else {
                    matrix[lineVerticalDoors][columnVerticalDoors]='"';
                }
            }
        }

        matrix[lineHero][columnHero] = 'H';
        cellHero.setPosition(lineHero,columnHero);
        movements.add(convertMatrixToString(matrix));

        //TODO - moveWhiteMummies()
        int n = whiteMummies;
        for (int whiteM = 0; whiteM < n; whiteM++) {
            moveWhiteMummy(whiteM, movements);
            if(whiteMummies < n){ //se um mummy morreu, o numero de mummies diminui (para nao fazer o ciclo com o nº de mummies anterior)
                whiteM--;
                n--;
            }
        }

        //TODO - moveScorpion()
        n = scorpions;
        for (int scorpion = 0; scorpion < n; scorpion++) {
            String movement = moveScorpion(scorpion);
            if(movement!=null){
                movements.add(movement);
            }
            hasKilledHero(cellScorpions[scorpion].getLine(), cellScorpions[scorpion].getColumn());//lineScorpions[scorpion], columnScorpions[scorpion]);
            if(scorpions < n){
                scorpion--;
                n--;
            }
        }

        //TODO - redMummies()
        n = redMummies;
        for(int redM = 0; redM < n; redM++){
            moveRedMummy(redM, movements);
            if(redMummies < n){
                redM--;
                n--;
            }
        }

        return movements;
    }

    public List<String> moveStandStill(){
        List<String> movements = new ArrayList<>();

        //TODO - moveWhiteMummies()
        int n = whiteMummies;
        for (int whiteM = 0; whiteM < n; whiteM++) {
            moveWhiteMummy(whiteM, movements);
            if(whiteMummies < n){ //se um mummy morreu, o numero de mummies diminui (para nao fazer o ciclo com o nº de mummies anterior)
                whiteM--;
                n--;
            }
        }

        //TODO - moveScorpion()
        n = scorpions;
        for (int scorpion = 0; scorpion < n; scorpion++) {
            String movement = moveScorpion(scorpion);
            if(movement!=null){
                movements.add(movement);
            }
            hasKilledHero(cellScorpions[scorpion].getLine(), cellScorpions[scorpion].getColumn());//lineScorpions[scorpion], columnScorpions[scorpion]);
            if(scorpions < n){
                scorpion--;
                n--;
            }
        }

        //TODO - redMummies()
        n = redMummies;
        for(int redM = 0; redM < n; redM++){
            moveRedMummy(redM, movements);
//            hasKilledHero(lineRedMummies[redM], columnRedMummies[redM]);
            if(redMummies < n){
                redM--;
                n--;
            }
        }

        return movements;
    }

    public boolean canMoveRight(int lineEntity, int columnEntity){

        //verificar se não sai fora dos limites (line < 12 && line > 0 && column < 12 && column > 0)
        if(columnEntity == matrix.length - 2){
            return false;
        }

        char matrixPosition = matrix[lineEntity][columnEntity+1];
        //verificar se tem parede (| ou -), porta fechada (" ou =)
        return  matrixPosition != '|' && matrixPosition != '-' &&
                matrixPosition != '=' && matrixPosition != '"';
    }

    public boolean canMoveRightHero(){
        return !gameOver && canMoveRight(cellHero.getLine(), cellHero.getColumn());
    }

    public boolean canMoveLeft(int lineEntity, int columnEntity){
        //moves off limits?
        if(columnEntity == 1){
            return false;
        }

        //has something blocking hero's path?
        char matrixPosition = matrix[lineEntity][columnEntity-1];
        //verificar se tem parede (| ou -), porta fechada (" ou =)
        return  matrixPosition != '|' && matrixPosition != '-' &&
                matrixPosition != '=' && matrixPosition != '"';
    }

    public boolean canMoveLeftHero(){
        return !gameOver && canMoveLeft(cellHero.getLine(), cellHero.getColumn());
    }

    public boolean canMoveUp(int lineEntity, int columnEntity){
        //moves off limits?
        if(lineEntity == 1){
            return false;
        }

        char matrixPosition = matrix[lineEntity-1][columnEntity];
        //verificar se tem parede (| ou -), porta fechada (" ou =)
        return  matrixPosition != '|' && matrixPosition != '-' &&
                matrixPosition != '=' && matrixPosition != '"';
    }

    public boolean canMoveUpHero(){
        return !gameOver && canMoveUp(cellHero.getLine(), cellHero.getColumn());
    }

    public boolean canMoveDown(int lineEntity, int columnEntity){
        //moves off limits?
        if(lineEntity == matrix.length - 2){
            return false;
        }

        char matrixPosition = matrix[lineEntity+1][columnEntity];
        //verificar se tem parede (| ou -), porta fechada (" ou =)
        return  matrixPosition != '|' && matrixPosition != '-' &&
                matrixPosition != '=' && matrixPosition != '"';
    }

    public boolean canMoveDownHero(){
        return !gameOver && canMoveDown(cellHero.getLine(), cellHero.getColumn());
    }

    public double computeExitDistance() {
        return ((Math.abs(cellHero.getLine() - cellExit.getLine()) + Math.abs(cellHero.getColumn() - cellExit.getColumn()))-1)/ (double) 2;
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

    public void resetEnemies() {
        scorpions = 0;
        cellScorpions = new Cell[1];

        whiteMummies = 0;
        cellWhiteMummies = new Cell[1];

        redMummies = 0;
        cellRedMummies = new Cell[1];

        horizontalDoors = 0;
        cellHorizontalDoors = new Cell[1];

        verticalDoors = 0;
        cellVerticalDoors = new Cell[1];

        traps = 0;
        cellTraps = new Cell[1];
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
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        MummyMazeState that = (MummyMazeState) o;
//        return  cellHero.equals(that.cellHero) && Arrays.equals(cellWhiteMummies, that.cellWhiteMummies) &&
//                Arrays.equals(cellScorpions, that.cellScorpions) &&
//                Arrays.equals(lineRedMummies, that.lineRedMummies) &&
//                Arrays.equals(columnRedMummies, that.columnRedMummies) &&
//                Arrays.deepEquals(matrix, that.matrix);
    }

    @Override
    public int hashCode() {
        return 97 * 7 + Arrays.deepHashCode(this.matrix);
////        int result = Objects.hash(lineHero, columnHero);
//        int result = cellHero.hashCode();
//        result = 31 * result + Arrays.deepHashCode(matrix);
////        result = 31 * result + cellWhiteMummies.hashCode();
////        result = 31 * result + cellScorpions.hashCode();
//        result = 31 * result + Arrays.hashCode(lineRedMummies);
//        result = 31 * result + Arrays.hashCode(columnRedMummies);
//        result = 31 * result + Arrays.hashCode(lineHorizontalDoors);
//        result = 31 * result + Arrays.hashCode(columnHorizontalDoors);
//        result = 31 * result + Arrays.hashCode(lineVerticalDoors);
//        result = 31 * result + Arrays.hashCode(columnVerticalDoors);
//        result = 31 * result + Objects.hash(lineKey, columnKey);
//        result = 31 * result + Arrays.hashCode(lineTraps);
//        result = 31 * result + Arrays.hashCode(columnTraps);
//        return result;
    }
}
