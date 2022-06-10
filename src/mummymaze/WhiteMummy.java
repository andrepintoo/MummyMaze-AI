package mummymaze;

import java.util.ArrayList;
import java.util.List;

public class WhiteMummy extends Mummy{

    public WhiteMummy(int line, int column, char symbol) {
        super(line, column, symbol);
    }

    public WhiteMummy(WhiteMummy m){
        super(m);
    }

    @Override
    public List<String> moveEnemy(MummyMazeState state) {
        List<String> movements = new ArrayList<>();
        int lineHero = state.getCellHero().getLine();
        int columnHero = state.getCellHero().getColumn();
        for (int n = 0; n < 2; n++) {
            if(state.isGameOver() || state.hasKilledHero(line, column)){
                return movements;
            }
            if(column != columnHero){
                if(columnHero > column){
                    if(canMoveRight(state)){
                        movements.add(moveRightSuper(state, 'M'));
                        state.hasKilledHero(line, column);
                        continue;
                    }
                }else{
                    if(canMoveLeft(state)){
                        movements.add(moveLeftSuper(state, 'M'));
                        state.hasKilledHero(line, column);
                        continue;
                    }
                }
            }
            if(line != lineHero){
                if(lineHero > line){
                    if(canMoveDown(state)){
                        movements.add(moveDownSuper(state,'M'));
                        state.hasKilledHero(line, column);
                    }
                }else{
                    if(canMoveUp(state)){
                        movements.add(moveUpSuper(state,'M'));
                        state.hasKilledHero(line, column);
                    }
                }
            }
            state.hasKilledHero(line, column);
        }
        return movements;
    }

    @Override
    protected Object clone() {
        return new WhiteMummy(this);
    }
}
