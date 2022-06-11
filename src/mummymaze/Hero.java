package mummymaze;


public class Hero extends Entity{

    public Hero(int line, int column) {
        super(line, column);
    }

    public Hero(Hero h){
        super(h);
    }

    @Override
    public String move(MummyMazeState state) {
        return state.convertMatrixToString(state.getMatrix());
    }

    @Override
    protected Object clone() {
        return new Hero(this);
    }
}
