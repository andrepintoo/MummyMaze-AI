package mummymaze;

import java.util.ArrayList;
import java.util.List;

public class Scorpion extends Enemy{

    public Scorpion(int line, int column, char symbol) {
        super(line, column, symbol);
    }

    public Scorpion(Scorpion s){
        super(s);
    }

    @Override
    public List<String> moveEnemy(MummyMazeState state) { //meter isto no Enemy e enviar o caracter para fazer updateSymbol
        List<String> movements = new ArrayList<>();
        if(state.isGameOver() || state.hasKilledHero(line, column)){
            return null;
        }
        int lineHero = state.getCellHero().getLine();
        int columnHero = state.getCellHero().getColumn();
        if(column != columnHero){
            if(columnHero > column){
                if(canMoveRight(state)){
                    movements.add(moveRightSuper(state, 'E'));
                    return movements;
                }
            }else{
                if(canMoveLeft(state)){
                    movements.add(moveLeftSuper(state, 'E'));
                    return movements;
//                    super.moveLeft(state);
//                    state.updateSymbol(line,column,'E');
//                    state.killEnemy(this);
//                    state.hasKilledHero(line,column);
//                    List<String> move = new ArrayList<>();
//                    move.add(state.convertMatrixToString(state.getMatrix()));
//                    return move;
                }
            }
        }
        if(line != lineHero){
            if(lineHero > line){
                if(canMoveDown(state)){
                    movements.add(moveDownSuper(state,'E'));
                    return movements;
//                    super.moveDown(state);
//                    state.updateSymbol(line,column,'E');
//                    state.killEnemy(this);
//                    state.hasKilledHero(line,column);
//                    List<String> move = new ArrayList<>();
//                    move.add(state.convertMatrixToString(state.getMatrix()));
//                    return move;
                }
            }else{
                if(canMoveUp(state)){
                    movements.add(moveUpSuper(state,'E'));
                    return movements;
//                    super.moveUp(state);
//                    state.updateSymbol(line,column,'E');
//                    state.killEnemy(this);
//                    state.hasKilledHero(line,column);
//                    List<String> move = new ArrayList<>();
//                    move.add(state.convertMatrixToString(state.getMatrix()));
//                    return move;
                }
            }
        }
        return null;
    }

    @Override
    protected Object clone() {
        return new Scorpion(this);
    }
}
