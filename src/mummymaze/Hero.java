package mummymaze;


public class Hero extends Entity{

    public Hero(int line, int column, char symbol) {
        super(line, column, symbol);
    }

    public Hero(Hero h){
        super(h);
    }

    @Override
    public String move(MummyMazeState state) {
//        super.moveRightSuper(state, 'H');
//        List<String> movements = new ArrayList<>();
//        movements.add(state.convertMatrixToString(state.getMatrix()));
        return state.convertMatrixToString(state.getMatrix());
    }
//    @Override
//    public List<String> moveRight(MummyMazeState state) {
//        super.moveRightSuper(state, 'H');
//        List<String> movements = new ArrayList<>();
//        movements.add(state.convertMatrixToString(state.getMatrix()));
//        return movements;
//    }


    @Override
    protected Object clone() {
        return new Hero(this);
    }
}
