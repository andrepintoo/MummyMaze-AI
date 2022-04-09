package eightpuzzle;

import agent.Action;

public class ActionUp extends Action<EightPuzzleState>{

    public ActionUp(){
        super(1);
    }

    @Override
    public void execute(EightPuzzleState state){
        state.moveUp(); //manda o estado modificar-se movendo a vazia para cima
        state.setAction(this); //define esta ação como sendo a ação que deu origem ao estado
    }

    @Override  //pergunta ao estado se a vazia se pode mover para cima
    public boolean isValid(EightPuzzleState state){
        return state.canMoveUp();
    }

}