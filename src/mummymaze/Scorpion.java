package mummymaze;

import java.util.LinkedList;

public class Scorpion extends Enemy{

    public Scorpion(int line, int column) {
        super(line, column);
    }

    public Scorpion(Scorpion s){
        super(s);
    }

    @Override
    public LinkedList<String> moveEnemy(MummyMazeState state) {
        LinkedList<String> movements = new LinkedList<>();
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
                }
            }
        }
        if(line != lineHero){
            if(lineHero > line){
                if(canMoveDown(state)){
                    movements.add(moveDownSuper(state,'E'));
                    return movements;
                }
            }else{
                if(canMoveUp(state)){
                    movements.add(moveUpSuper(state,'E'));
                    return movements;
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
