package mummymaze;

public abstract class Mummy extends Enemy{

    public Mummy(int line, int column) {
        super(line, column);
    }

    public Mummy(Mummy m){
        super(m);
    }

}
