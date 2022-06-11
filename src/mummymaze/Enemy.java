package mummymaze;

import java.util.LinkedList;

public abstract class Enemy extends Entity{

    public Enemy(int line, int column) {
        super(line, column);
    }

    public Enemy(Enemy e){
        super(e);
    }

    public abstract LinkedList<String> moveEnemy(MummyMazeState state);

    @Override
    public String move(MummyMazeState state) {
        state.killEnemy(this);
        state.hasKilledHero(line,column);
        return state.convertMatrixToString(state.getMatrix());
    }

    @Override
    protected Object clone() {
        return super.clone();
    }
}
