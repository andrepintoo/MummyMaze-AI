package agent;


import java.util.ArrayList;
import java.util.List;

public abstract class Action <S extends State> implements Cloneable{
    //uma ação tem um state. Vai ser o estado do problema especifico em que se está a trabalhar

    protected List<String> movements;
    private final double cost; //custo que envolveu a execução da ação

    public Action(double cost){
        this.cost = cost;
    } //no eigthPuzzle, à partida o custo é igual para todos

    public abstract void execute(S State); //depende do problema, então é method abstract

    public abstract boolean isValid(S State); //depende do problema, então é method abstract

    public double getCost(){
        return cost;
    }

    public List<String> getMovements() {
        return movements; //new ArrayList<>(movements);
    }

    public void resetMovements(){
        this.movements.clear();
    }

    protected void addMovement(List<String> movements){
        this.movements.addAll(movements);
//        for (String movement: movements) {
//            this.movements.add(movement);
//        }
    }
}
