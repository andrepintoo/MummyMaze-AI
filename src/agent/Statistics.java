package agent;

import java.util.LinkedList;

public class Statistics {
    private LinkedList<SolutionWithDescription> solutions;

    public Statistics(){
        solutions = new LinkedList<>();
    }

    public void addSolution(Solution solution, String searchMethod, String statistics){
        solutions.add(new SolutionWithDescription(solution, searchMethod, statistics));
    }

    @Override
    public String toString() {
        String sb = "";
        for(SolutionWithDescription solution : solutions){
            sb += solution.toString() + "\n" + '\n';
        }
        return sb;
    }

    public String getSolutionDescription(int i){
        int j = 0;
        for (SolutionWithDescription solution : solutions) {
            if(j == i){
                return solution.toString();
            }
            j++;
        }
        return "";
    }

    public int getSize(){
        return solutions.size();
    }
}
