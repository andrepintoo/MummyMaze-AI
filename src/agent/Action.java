package agent;


public abstract class Action <S extends State>{
    //uma ação tem um state. Vai ser o estado do problema especifico em que se está a trabalhar

    private final double cost; //custo que envolveu a execução da ação

    public Action(double cost){
        this.cost = cost;
    } //no eigthPuzzle, à partida o custo é igual para todos

    public abstract void execute(S State); //depende do problema, então é method abstract

    public abstract boolean isValid(S State); //depende do problema, então é method abstract

    public double getCost(){
        return cost;
    }
}
