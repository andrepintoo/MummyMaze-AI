package showSolution;

import agent.Action;
import agent.Solution;
import gui.*;
import mummymaze.HeuristicExitDistance;
import mummymaze.MummyMazeAgent;
import mummymaze.MummyMazeProblem;
import mummymaze.MummyMazeState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SolutionPanel extends JFrame{

	private showSolution.GameArea gameArea;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		MummyMazeState newState = new MummyMazeState(new char[13][13]);
//		MummyMazeAgent agent = new MummyMazeAgent(newState);
		MummyMazeAgent agent = new MummyMazeAgent(new MummyMazeAgent(new MummyMazeState(new char[13][13])).setInitialStateFromFile(new File("nivel1.txt")));
		List<String> movements = new ArrayList<>();

		MummyMazeProblem problem = new MummyMazeProblem(agent.getEnvironment());
		Solution solution = agent.solveProblem(problem);


		for (Action action: solution.getActions()) {
			action.execute(agent.getEnvironment());
			List<String> actionMovements = action.getMovements();
			for (String movement: actionMovements) {
				movements.add(movement);
			}
		}

		showSolution(movements,solution.getCost());


	}
	
	public SolutionPanel(){
		super("Mummy Maze");
		gameArea = new GameArea();
		getContentPane().setLayout(new BorderLayout());
        getContentPane().add(gameArea,BorderLayout.CENTER);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                // Exit the application
                System.exit(0);
            }
        });
	}
	
	public static void showSolution(final List<String> states, final double solutionCost){
		final SolutionPanel p = new SolutionPanel();
		p.setVisible(true);
		p.pack();		
		Thread t = new Thread(){
            public void run(){
            	p.setSolutionCost(solutionCost);
            	for(String s : states)  {
                	p.setState(s);
                	try {
						sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            }
        };
        t.start();
	}

	public static void showState(final String state){
		final SolutionPanel p = new SolutionPanel();
		p.setVisible(true);
		p.pack();
		Thread t = new Thread(){
			public void run(){
				p.setState(state);
				p.setShowSolutionCost(false);
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		t.start();
	}
	
	private void setState(String state){
		gameArea.setState(state);
	}

	public void setShowSolutionCost(boolean showSolutionCost) {
		gameArea.setShowSolutionCost(showSolutionCost);
	}

	private void setSolutionCost(double solutionCost){
		gameArea.setSolutionCost(solutionCost);
	}

	public GameArea getGameArea() {
		return gameArea;
	}

}
