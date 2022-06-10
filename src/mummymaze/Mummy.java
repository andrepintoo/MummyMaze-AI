package mummymaze;

public abstract class Mummy extends Enemy{

    public Mummy(int line, int column, char symbol) {
        super(line, column, symbol);
    }

    public Mummy(Mummy m){
        super(m);
    }

}
