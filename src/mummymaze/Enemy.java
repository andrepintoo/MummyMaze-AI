package mummymaze;

import java.util.ArrayList;
import java.util.List;

public abstract class Enemy extends Entity{

    public Enemy(int line, int column, char symbol) {
        super(line, column, symbol);
    }

    public Enemy(Enemy e){
        super(e);
    }

    public abstract List<String> moveEnemy(MummyMazeState state);

    @Override
    public String move(MummyMazeState state) {
        state.killEnemy(this);
        state.hasKilledHero(line,column);
        return state.convertMatrixToString(state.getMatrix());
    }

//    @Override
//    public List<String> moveRight(MummyMazeState state) {
//        state.killEnemy(this);
//        state.hasKilledHero(line,column);
//        List<String> move = new ArrayList<>();
//        move.add(state.convertMatrixToString(state.getMatrix()));
//        return move;
//    }
//
//    @Override
//    public List<String> moveLeft(MummyMazeState state) {
//        state.killEnemy(this);
//        state.hasKilledHero(line,column);
//        List<String> move = new ArrayList<>();
//        move.add(state.convertMatrixToString(state.getMatrix()));
//        return move;
//    }
//
//    @Override
//    public List<String> moveDown(MummyMazeState state) {
//
//
//        return null;
//    }
//
//    @Override
//    public List<String> moveUp(MummyMazeState state) {
//        List<String> moves = super.moveUp(state);
//
//        return null;
//    }


    @Override
    protected Object clone() {
        return super.clone();
    }
}
