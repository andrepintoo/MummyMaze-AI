package gui;

import agent.Heuristic;
import agent.Solution;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import mummymaze.MummyMazeAgent;
import mummymaze.MummyMazeProblem;
import mummymaze.MummyMazeState;
import searchmethods.BeamSearch;
import searchmethods.DepthLimitedSearch;
import searchmethods.InformedSearch;
import searchmethods.SearchMethod;
import showSolution.GameArea;
import showSolution.SolutionPanel;

import static java.lang.Thread.sleep;

public class MainFrame extends JFrame {
    public static boolean SHOWSOLUTION = false;

    //Instanciar o MummyMazeAgent
    MummyMazeAgent agent = new MummyMazeAgent(new MummyMazeState(new char[13][13]));

    //Ler o estado inicial de um ficheiro
    MummyMazeState initialState;
    {
        try {
            initialState = agent.readInitialStateFromFile(new File("nivel1.txt"));
            currentLevel = "nivel1";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SolutionPanel solutionPanel = new SolutionPanel();
    private GameArea gameArea = solutionPanel.getGameArea();

    private JComboBox comboBoxSearchMethods;
    private JComboBox comboBoxHeuristics;
    private JLabel labelSearchParameter = new JLabel("limit/beam size:");
    private JTextField textFieldSearchParameter = new JTextField("0", 5);
    private PuzzleTableModel puzzleTableModel;
    private JTable tablePuzzle = new JTable();
    private JButton buttonInitialState = new JButton("Read Level");
    private JButton buttonSolve = new JButton("Solve");
    private JButton buttonStop = new JButton("Stop");
    private JButton buttonShowSolution = new JButton("Show solution");
    private JButton buttonReset = new JButton("Reset to inital state");
    private JTextArea textArea;

    private String currentLevel;
    private String currentAlgorithm;

    public MainFrame() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    private void jbInit() throws Exception {

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("MummyMaze Puzzle");

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(gameArea,BorderLayout.CENTER);
        JPanel contentPane = (JPanel) this.getContentPane();
//        JPanel contentPane = (JPanel) solutionPanel.getContentPane();
        contentPane.setLayout(new BorderLayout());

        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(buttonInitialState);
        buttonInitialState.addActionListener(new ButtonInitialState_ActionAdapter(this));
        panelButtons.add(buttonSolve);
        buttonSolve.addActionListener(new ButtonSolve_ActionAdapter(this));
        panelButtons.add(buttonStop);
        buttonStop.setEnabled(false);
        buttonStop.addActionListener(new ButtonStop_ActionAdapter(this));
        panelButtons.add(buttonShowSolution);
        buttonShowSolution.setEnabled(false);
        buttonShowSolution.addActionListener(new ButtonShowSolution_ActionAdapter(this));
        panelButtons.add(buttonReset);
        buttonReset.setEnabled(false);
        buttonReset.addActionListener(new ButtonReset_ActionAdapter(this));

        JPanel panelSearchMethods = new JPanel(new FlowLayout());
        comboBoxSearchMethods = new JComboBox(agent.getSearchMethodsArray());
        panelSearchMethods.add(comboBoxSearchMethods);
        comboBoxSearchMethods.addActionListener(new ComboBoxSearchMethods_ActionAdapter(this));
        panelSearchMethods.add(labelSearchParameter);
        labelSearchParameter.setEnabled(false);
        panelSearchMethods.add(textFieldSearchParameter);
        textFieldSearchParameter.setEnabled(false);
        textFieldSearchParameter.setHorizontalAlignment(JTextField.RIGHT);
        textFieldSearchParameter.addKeyListener(new TextFieldSearchParameter_KeyAdapter(this));
        comboBoxHeuristics = new JComboBox(agent.getHeuristicsArray());
        panelSearchMethods.add(comboBoxHeuristics);
        comboBoxHeuristics.setEnabled(true);
        comboBoxHeuristics.addActionListener(new ComboBoxHeuristics_ActionAdapter(this));

        //Jpanel = gameArea
//        JPanel puzzlePanel = new JPanel(new FlowLayout());
        JPanel puzzlePanel = new JPanel(new FlowLayout());
//        SolutionPanel puzzlePanel = new SolutionPanel();
//        GameArea gameArea = new GameArea();
        puzzlePanel.add(gameArea);
        setState(initialState.getStateString());
//        showState(initialState.getStateString());
//        puzzlePanel.add(tablePuzzle);

        // caixa de texto branca - estatísticas
        textArea = new JTextArea(14, 30);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setEditable(false);
        puzzlePanel.add(scrollPane);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panelButtons, BorderLayout.NORTH);
        mainPanel.add(panelSearchMethods, BorderLayout.CENTER);
        mainPanel.add(puzzlePanel, BorderLayout.SOUTH);
        contentPane.add(mainPanel);

        configureTabel(tablePuzzle);

        pack();
    }

    private void configureTabel(JTable table) {
        puzzleTableModel = new PuzzleTableModel(agent.getEnvironment());
        tablePuzzle.setModel(puzzleTableModel);
        table.setDefaultRenderer(Object.class, new PuzzleTileCellRenderer());

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(Properties.CELL_WIDTH);
        }
        table.setRowHeight(Properties.CELL_HEIGHT);
        //table.setBorder(BorderFactory.createLineBorder(Color.black));
    }

    public void buttonInitialState_ActionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser(new java.io.File("."));
        try {
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                initialState = agent.readInitialStateFromFile(fc.getSelectedFile());
                puzzleTableModel.setPuzzle(initialState);
                setState(initialState.getStateString());
                buttonSolve.setEnabled(true);
                buttonShowSolution.setEnabled(false);
                buttonReset.setEnabled(false);

                currentLevel = (fc.getSelectedFile().getName()).split(".txt")[0];
            }


        } catch (IOException e1) {
            e1.printStackTrace(System.err);
        } catch (NoSuchElementException e2) {
            JOptionPane.showMessageDialog(this, "File format not valid", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void comboBoxSearchMethods_ActionPerformed(ActionEvent e) {
        int index = comboBoxSearchMethods.getSelectedIndex();
        agent.setSearchMethod((SearchMethod) comboBoxSearchMethods.getItemAt(index));
        puzzleTableModel.setPuzzle(agent.resetEnvironment());
        buttonSolve.setEnabled(true);
        buttonShowSolution.setEnabled(false);
        buttonReset.setEnabled(false);
        textArea.setText("");
        comboBoxHeuristics.setEnabled(index > 4); //Informed serch methods
        textFieldSearchParameter.setEnabled(index == 3 || index == 7); // limited depth or beam search
        labelSearchParameter.setEnabled(index == 3 || index == 7); // limited depth or beam search
    }

    public void comboBoxHeuristics_ActionPerformed(ActionEvent e) {
        int index = comboBoxHeuristics.getSelectedIndex();
        agent.setHeuristic((Heuristic) comboBoxHeuristics.getItemAt(index));
        puzzleTableModel.setPuzzle(agent.resetEnvironment());
        buttonSolve.setEnabled(true);
        buttonShowSolution.setEnabled(false);
        buttonReset.setEnabled(false);
        textArea.setText("");
    }

    public void buttonSolve_ActionPerformed(ActionEvent e) {
        SHOWSOLUTION = false;

        SwingWorker worker = new SwingWorker<Solution, Void>() {
            @Override
            public Solution doInBackground() {
                textArea.setText("");
                buttonStop.setEnabled(true);
                buttonSolve.setEnabled(false);
                try {
                    prepareSearchAlgorithm();

                    //Instanciar o problem
                    MummyMazeProblem problem = new MummyMazeProblem(initialState);

                    //executar algoitmo de procura para obter a solução
                    agent.solveProblem(problem);

                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
                return null;
            }

            @Override
            public void done() {
                if (!agent.hasBeenStopped()) {
                    textArea.setText(agent.getSearchReport());
                    if (agent.hasSolution()) {
                        File file = new File("statistics_"+currentLevel+".xls");
                        if(!file.exists()){
                            appendToTextFile("statistics_"+currentLevel+".xls", buildExperimentHeader()+"\r\n");
                        }

                        buttonShowSolution.setEnabled(true);

                        SearchMethod searchMethod = agent.getSearchMethod();
                        String heuristicString;
                        if(searchMethod instanceof InformedSearch){
                            heuristicString = agent.getHeuristic().toString();
                        }else{
                            heuristicString = "-";
                        }
                        appendToTextFile("statistics_"+currentLevel+".xls", searchMethod.toString()+"\t"+
                                heuristicString+"\t"+agent.getSolutionCost()+"\t"+
                                searchMethod.getStatistics().numExpandedNodes+"\t"+searchMethod.getStatistics().maxFrontierSize+"\t"+
                                searchMethod.getStatistics().numGeneratedNodes+"\t"+searchMethod.getStatistics().getDurationInSeconds()+"\r\n");

                    }
                }
                buttonSolve.setEnabled(true);
                buttonStop.setEnabled(false);
            }
        };

        worker.execute();
    }

    private String buildExperimentHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append("Algorithm:" + "\t");
        sb.append("Heuristic:" + "\t");
        sb.append("Solution Cost:" + "\t");
        sb.append("Expanded Nodes:" + "\t");
        sb.append("Max Frontier Size:" + "\t");
        sb.append("Generated Nodes:" + "\t");
        sb.append("Duration:" + "\t");
        return sb.toString();
    }

    public static void appendToTextFile(String fileName, String string){
        BufferedWriter w = null;
        try {
            w = new BufferedWriter(new FileWriter(fileName, true));
            w.write(string);

        } catch (Exception e) {
            System.err.println("Error: " + e);
        } finally {
            try {
                if (w != null){
                    w.close();
                }
            } catch (IOException ignore) {
            }
        }
    }

    public void buttonStop_ActionPerformed(ActionEvent e) {
        agent.stop();
        buttonShowSolution.setEnabled(false);
        buttonStop.setEnabled(false);
        buttonSolve.setEnabled(true);
    }

    public void buttonShowSolution_ActionPerformed(ActionEvent e) {
        SHOWSOLUTION = true;
        buttonShowSolution.setEnabled(false);
        buttonStop.setEnabled(false);
        buttonSolve.setEnabled(false);
        SwingWorker worker = new SwingWorker<Void, Void>() {
            @Override
            public Void doInBackground() {
                //executar a solução = obter a lista de turnos
                agent.executeSolution();

                //obter a lista de turnos correspondente à solução
                List<String> movements = agent.getMovements();
                double cost = agent.getSolutionCost();

                // mostrar a lista de turnos na interface gráfica fornecida
                Thread t = showSolution(movements, cost);
                while(t.isAlive()){
                    try {
                        sleep(300);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            public void done() {
                buttonReset.setEnabled(true);
                buttonShowSolution.setEnabled(true);
                buttonSolve.setEnabled(true);
            }
        };
        worker.execute();
    }

    public void buttonReset_ActionPerformed(ActionEvent e) {
        puzzleTableModel.setPuzzle(agent.resetEnvironment());
        setState(agent.getEnvironment().getStateString());

        buttonShowSolution.setEnabled(true);
        buttonReset.setEnabled(false);
    }

    private void prepareSearchAlgorithm() {
        if (agent.getSearchMethod() instanceof DepthLimitedSearch) {
            DepthLimitedSearch searchMethod = (DepthLimitedSearch) agent.getSearchMethod();
            searchMethod.setLimit(Integer.parseInt(textFieldSearchParameter.getText()));
        } else if (agent.getSearchMethod() instanceof BeamSearch) {
            BeamSearch searchMethod = (BeamSearch) agent.getSearchMethod();
            searchMethod.setBeamSize(Integer.parseInt(textFieldSearchParameter.getText()));
        }
    }

    public Thread showSolution(final List<String> states, final double solutionCost){

        this.setVisible(true);
        this.pack();
        Thread t = new Thread(){
            public void run(){
                setSolutionCost(solutionCost);
                for(String s : states)  {
                    setState(s);
                    try {
                        sleep(300);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
        return t;
    }

    public void showState(final String state){
        this.setVisible(true);
        this.pack();
        Thread t = new Thread(){
            public void run(){
                setState(state);
                setShowSolutionCost(false);
//                try {
//                    sleep(500);
//                } catch (InterruptedException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
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
}

class ComboBoxSearchMethods_ActionAdapter implements ActionListener {

    private final MainFrame adaptee;

    ComboBoxSearchMethods_ActionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        adaptee.comboBoxSearchMethods_ActionPerformed(e);
    }
}

class ComboBoxHeuristics_ActionAdapter implements ActionListener {

    private final MainFrame adaptee;

    ComboBoxHeuristics_ActionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        adaptee.comboBoxHeuristics_ActionPerformed(e);
    }
}

class ButtonInitialState_ActionAdapter implements ActionListener {

    private final MainFrame adaptee;

    ButtonInitialState_ActionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        adaptee.buttonInitialState_ActionPerformed(e);
    }
}

class ButtonSolve_ActionAdapter implements ActionListener {

    private final MainFrame adaptee;

    ButtonSolve_ActionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        adaptee.buttonSolve_ActionPerformed(e);
    }
}

class ButtonStop_ActionAdapter implements ActionListener {

    private final MainFrame adaptee;

    ButtonStop_ActionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        adaptee.buttonStop_ActionPerformed(e);
    }
}

class ButtonShowSolution_ActionAdapter implements ActionListener {

    private final MainFrame adaptee;

    ButtonShowSolution_ActionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        adaptee.buttonShowSolution_ActionPerformed(e);
    }
}

class ButtonReset_ActionAdapter implements ActionListener {

    private final MainFrame adaptee;

    ButtonReset_ActionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        adaptee.buttonReset_ActionPerformed(e);
    }
}

class TextFieldSearchParameter_KeyAdapter implements KeyListener {

    private final MainFrame adaptee;

    TextFieldSearchParameter_KeyAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (!Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) {
            e.consume();
        }
    }
}
