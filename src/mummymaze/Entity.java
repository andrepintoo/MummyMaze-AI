package mummymaze;

public abstract class Entity extends Cell{

    public Entity(int line, int column, char symbol) {
        super(line, column, symbol);
    }

    public Entity(Entity e){
        super(e);
    }

    public String moveRightSuper(MummyMazeState state, char character){
        char nextPosition = state.getPosition(line, column+2);
        if(nextPosition == 'A' || nextPosition == 'M' || nextPosition == 'V' || nextPosition == 'E'){
            state.setGameOver(true);
        }
        if(nextPosition == 'C'){
            state.changeDoorsState();
        }
        state.verifySpecialCells(this);
        column+=2;
        state.updateSymbol(line, column, character);
        return move(state);
    }

    public String moveLeftSuper(MummyMazeState state, char character){
        char nextPosition = state.getPosition(line, column-2);
        if(nextPosition == 'A' || nextPosition == 'M' || nextPosition == 'V' || nextPosition == 'E'){
            state.setGameOver(true);
        }

        if(nextPosition == 'C'){
            state.changeDoorsState();
        }

        state.verifySpecialCells(this);

        column-=2;
        state.updateSymbol(line, column, character);
        return move(state);
    }

    public String moveUpSuper(MummyMazeState state, char character){
        char nextPosition = state.getPosition(line-2, column);
        if(nextPosition == 'A' || nextPosition == 'M' || nextPosition == 'V' || nextPosition == 'E'){
            state.setGameOver(true);
        }

        if(nextPosition == 'C'){
            state.changeDoorsState();
        }

        state.verifySpecialCells(this);

        line-=2;
        state.updateSymbol(line, column, character);
        return move(state);
    }

    public String moveDownSuper(MummyMazeState state, char character){
        char nextPosition = state.getPosition(line+2, column);
        if(nextPosition == 'A' || nextPosition == 'M' || nextPosition == 'V' || nextPosition == 'E'){
            state.setGameOver(true);
        }
        if(nextPosition == 'C'){
            state.changeDoorsState();
        }
        state.verifySpecialCells(this);
        line+=2;
        state.updateSymbol(line, column, character);
        return move(state);
    }

    public abstract String move(MummyMazeState state);

    public boolean canMoveRight(MummyMazeState state){
        //verificar se não sai fora dos limites (line < 12 && line > 0 && column < 12 && column > 0)
        if(column == state.getNumColumns() - 2){
            return false;
        }
        char matrixPosition = state.getPosition(line,column+1);
        //verificar se tem parede (| ou -), porta fechada (" ou =)
        return  matrixPosition != '|' && matrixPosition != '-' &&
                matrixPosition != '=' && matrixPosition != '"';
    }

    public boolean canMoveDown(MummyMazeState state){
        if(line == state.getNumLines() - 2){
            return false;
        }
        char matrixPosition = state.getPosition(line+1,column);
        return  matrixPosition != '|' && matrixPosition != '-' &&
                matrixPosition != '=' && matrixPosition != '"';
    }

    public boolean canMoveUp(MummyMazeState state){
        if(line == 1){
            return false;
        }
        char matrixPosition = state.getPosition(line-1,column);
        return  matrixPosition != '|' && matrixPosition != '-' &&
                matrixPosition != '=' && matrixPosition != '"';
    }

    public boolean canMoveLeft(MummyMazeState state){
        if(column == 1){
            return false;
        }
        char matrixPosition = state.getPosition(line,column-1);
        return  matrixPosition != '|' && matrixPosition != '-' &&
                matrixPosition != '=' && matrixPosition != '"';
    }
}
