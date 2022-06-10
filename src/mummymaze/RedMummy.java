package mummymaze;

import java.util.ArrayList;
import java.util.List;

public class RedMummy extends Mummy{

    public RedMummy(int line, int column, char symbol) {
        super(line, column, symbol);
    }

    public RedMummy(RedMummy m){
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
            if(line != lineHero){
                if(lineHero > line){
                    if(canMoveDown(state)){
                        movements.add(moveDownSuper(state,'V'));
                        state.hasKilledHero(line, column);
                    }
                }else{
                    if(canMoveUp(state)){
                        movements.add(moveUpSuper(state,'V'));
                        state.hasKilledHero(line, column);
                    }
                }
            }
            if(column != columnHero){
                if(columnHero > column){
                    if(canMoveRight(state)){
                        movements.add(moveRightSuper(state, 'V'));
                        state.hasKilledHero(line, column);
                        continue;
                    }
                }else{
                    if(canMoveLeft(state)){
                        movements.add(moveLeftSuper(state, 'V'));
                        state.hasKilledHero(line, column);
                        continue;
                    }
                }
            }
            state.hasKilledHero(line, column);
        }
        return movements;
    }

    @Override
    protected Object clone() {
        return new RedMummy(this);
    }
}
