package eightpuzzle;

import agent.Action;
import agent.Problem;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EightPuzzleProblem extends Problem<EightPuzzleState> {

    //TODO

    private EightPuzzleState goalState;

    public EightPuzzleProblem(EightPuzzleState initialState) { //o super tem que ser a primeira instrução
        super(initialState, new ArrayList<>(4)); // a superclasse não tem um construtor por omissão (era fazer super() )
        actions.add(new ActionUp()); //a lista actions é criada na superclasse (podia ter ali o super.actions.add() )
        actions.add(new ActionRight());
        actions.add(new ActionDown());
        actions.add(new ActionLeft());

        goalState = new EightPuzzleState(EightPuzzleState.GOAL_MATRIX);
    }

    //devolve a lista de estados sucessores (estados para onde é possivel transitar a partir
                    //daquele que é passado por argumento)
    @Override
    public List<EightPuzzleState> executeActions(EightPuzzleState state) {
        List<EightPuzzleState> listaSucessores = new ArrayList<>(4); //sei que no máximo vou ter 4 estados sucessores
        //para cada ação disponível
        //      se a ação for válida
        for (Action a: actions) { //se pode ir para cima, baixo, esq ou dir //a lista das ações está guardada na superclasse
            if(a.isValid(state)){
                //          criar um novo estado successor (igual ao original, para já)
                EightPuzzleState sucessor = (EightPuzzleState) state.clone(); //pq o clone devolve sempre um object, é preciso fazer cast
                //          executar a ação sobre o novo estado (executar sobre o estado inicial devolve uma lista com os estados disponiveis.. e assim sucessivamente)
                sucessor.executeAction(a);
                //          adiciona o novo estado à lista de sucessores
                 //já lhe diz qual é a instrução que lhe deu origem
                listaSucessores.add(sucessor);
            }
        }
        return listaSucessores; //devolver lista de estados sucessores (no máximo podem ser 4, tbm podem ser 3 ou 2)
    }

    @Override
    public boolean isGoal(EightPuzzleState state) {
        return state.equals(goalState);
    }

    @Override
    protected double computePathCost(List<Action> path) {
        return path.size(); //pq as ações do EigthPuzzelProblem têm todas custo 1. Assim evita-se estar a fazer um ciclo for para somar todos (então fez-se Override)
    }

    public EightPuzzleState getGoalState() {
        return goalState;
    }
}
