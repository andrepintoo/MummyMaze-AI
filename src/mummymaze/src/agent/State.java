package mummymaze.src.agent;

public abstract class State{ //é uma configuração do problema (é o problema numa determinada altura)
        //tem que saber qual é a ação que o originou (qual a ação que fez estar neste estado).
        //tem que se ir a cada um dos estados e perguntar "qual foi a ação que foi a tua origem?"

    /**
     * Action that generated this state.
     */
    protected Action action;

   // public State(){} //o stor apagou isto, pois não recebe nada nem faz nada -> é o construtor por omissão

    public abstract void executeAction(Action action); //recebe ação e executa. A ação a ser executada vai depender do problema (então não se pode executar na superclasse)

    public Action getAction(){
        return action;
    }

    public void setAction(Action action){
        this.action = action;
    }

    //@Override
    //public abstract int hashCode(); //o stor apagou isto

    //@Override
    //public abstract boolean equals(Object obj); //o stor apagou isto
}