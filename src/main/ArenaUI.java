/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import algo.ExplorationAlgorithm;
import machine.Machine;
import arena.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import machine.MachineConfig.*;;
import static main.MapDescriptor.*;


/**
 *
 * @author Kelvin
 */
public class ArenaUI {

    private static JFrame appFrame = null;        // application JFrame   
    private static JPanel panel = null;
    private static Arena realMap = null;              // real map
    private static Arena exploredMap = null;          // exploration map
    private static int timeLimit = 3600;            // time limit
    private static int coverageLimit = 300;         // coverage limit
    //private final CommMgr comm = CommMgr.getCommMgr();
    private static final boolean simulationRun = true;
    private static Arena _arena;
    private static Machine _machine;
    private static JPanel drawingPanel = new JPanel(new GridLayout(21, 15));
    private static JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private static JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private static JPanel container = new JPanel(new BorderLayout());
    private static final CommMgr comm = CommMgr.getCommMgr();
    //private static ArenaCls as;

    public static void main(String[] args) {
        if (!simulationRun) comm.openConnection();
        _arena = new Arena(_machine);
        exploredMap= new Arena(_machine);
        realMap = new Arena(_machine);
        _machine = new Machine(_arena.getStartX(), _arena.getStartY(), FACING.EAST, simulationRun);
        //JTextField loadTF = new JTextField(15);
        //loadMapFromDisk(exploredMap,"SampleArena4");
        populateArena();
        setPaintBtn();
        paintMachine();
        
        addLabelsComponentToPane(inputPanel);
        addButtonsComponentToPane(buttonsPanel);
        
        appFrame.setVisible(true);
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    }

    public static void populateArena() {
        appFrame = new JFrame("MDP GRP 25");

        appFrame.setResizable(false);
        JLabel label = null;
        
        for (int i = 19; i >=0; i--) {
            label = new JLabel("  " + i);
            drawingPanel.add(label);
            for (int j = 0; j < 15; j++) {
                JButton _appBtn = new JButton();
                _appBtn.setBounds(j * 5, i * 5, 5, 5);
                _appBtn.setToolTipText(i + ":" + j);
                String[] cellCord = _appBtn.getToolTipText().split(":");
                int cellX = Integer.parseInt(cellCord[0]);
                int cellY = Integer.parseInt(cellCord[1]);
                repaintBtn();
                _appBtn.addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        if ((_appBtn.getBackground() == ArenaUIConfig.freeSpaceColor || _appBtn.getBackground() == ArenaUIConfig.virtualWallColor) && exploredMap.checkValidCell(cellX, cellY)) {
                            _appBtn.setBackground(ArenaUIConfig.obstacleColor);
                            exploredMap.placeObstacle(cellX, cellY, true);
                            System.out.println(cellX+" "+ cellY);
                        } else if (_appBtn.getBackground() == ArenaUIConfig.obstacleColor) {
                            _appBtn.setBackground(ArenaUIConfig.freeSpaceColor);
                            exploredMap.placeObstacle(cellX, cellY, false);
                            exploredMap.defineVirtualWall();
                        }
                    }
                });
                drawingPanel.add(_appBtn);
            }
        }
        for (int j = 0; j < 16; j++) {
            if (j == 0) {
                label = new JLabel(" ");
                drawingPanel.add(label);
            } else {
                label = new JLabel("  " + (j - 1));
                drawingPanel.add(label);
            }
        }
        container.add(inputPanel, BorderLayout.PAGE_START);
        container.add(drawingPanel, BorderLayout.CENTER);
        container.add(buttonsPanel, BorderLayout.PAGE_END);
        appFrame.add(container);
        appFrame.setSize(768, 1024);
        
        repaintBtn();
    }
    public static void addLabelsComponentToPane(Container pane){
        JTextField timerInput = new JTextField();
        JTextField coverageInput = new JTextField();
        final JComponent[] limitInputs = new JComponent[]{
          new JLabel("Timer Limit (s)"), timerInput, new JLabel("Coverage Limit (%)"), coverageInput
        }; 
        
        JButton _appBtn = new JButton("Input Limits");
        _appBtn.setName("InputLimits");
        _appBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        _appBtn.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int result = JOptionPane.showConfirmDialog(null, limitInputs, "Input Limits", JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION){
                    System.out.println("Timer Limit: " + timerInput.getText() + "\n Coverage Limit: " + coverageInput.getText());
                } 
                else {
                    System.out.println("User cancelled / closed dialog");
                }
                
                if (!timerInput.getText().equals("")){
                    int newTimerLimit = Integer.parseInt(timerInput.getText());
                    timeLimit = newTimerLimit;
                } 
                
                if (!coverageInput.getText().equals("")){
                    int newCoverageLimit = Integer.parseInt(coverageInput.getText());
                    newCoverageLimit = newCoverageLimit*3;
                    coverageLimit = newCoverageLimit;
                }
                System.out.println(timeLimit);
                System.out.println(coverageLimit);   
            }
        });
        inputPanel.add(_appBtn);
    }
    
    public static void addButtonsComponentToPane(Container pane){
        
        class Exploration extends SwingWorker<Integer, String> {
            protected Integer doInBackground() throws Exception {
                int x, y;

                x = _arena.getStartX();
                y = _arena.getStartY();

                _machine.setMachine(x, y);

                ExplorationAlgorithm exploration;
                exploration = new ExplorationAlgorithm(exploredMap, realMap, _machine, coverageLimit, timeLimit);

                exploration.startExploration();
                generateMapDescriptor(exploredMap);

                return 111;
            }
            
        }

        
        pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));

        JButton _appBtn = new JButton("Edit Arena");
        _appBtn.setName("EditBtn");
        _appBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        _appBtn.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                for (Component c : drawingPanel.getComponents()) {
                    if (c instanceof JPanel) {
                        c.setEnabled(true);
                    }
                }
            }
        });
        buttonsPanel.add(_appBtn);
        
        _appBtn = new JButton("Save Arena");
        _appBtn.setName("savrBtn");
        _appBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                String command = actionEvent.getActionCommand();
                System.out.println("Selected: " + command);
            }
        };
        buttonsPanel.add(_appBtn);
        
     
        
        _appBtn = new JButton("Exploration");
        _appBtn.setName("explorationBtn");
        _appBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        _appBtn.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                new Exploration().execute();

            }
        });
        buttonsPanel.add(_appBtn);
        
        _appBtn = new JButton("Fastest Path");
        _appBtn.setName("FastestPathBtn");
        _appBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        _appBtn.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                //new FastestPath().execute();
            }
        });
        buttonsPanel.add(_appBtn);

        appFrame.setVisible(true);
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void repaintBtn() {
        for (Component c : drawingPanel.getComponents()) {
            if (c instanceof JButton) {
                String[] sBtn = ((JButton) c).getToolTipText().split(":");
                int cellX = Integer.parseInt(sBtn[0]);
                int cellY = Integer.parseInt(sBtn[1]);
                exploredMap.defineVirtualWall();

                JButton temp = (JButton) c;
                if (exploredMap.startArea(cellX, cellY)) {
                    temp.setBackground(ArenaUIConfig.startColor);
                } else if (exploredMap.goalArea(cellX, cellY)) {
                    temp.setBackground(ArenaUIConfig.goalColor);
                } else if (!exploredMap.getCell(cellX, cellY).getIsVisited()) {
                    temp.setBackground(ArenaUIConfig.unexploredColor);
                } else if(exploredMap.isObstacle(cellX, cellY)){
                    temp.setBackground(ArenaUIConfig.obstacleColor);
                }else if (exploredMap.isVirtualWall(cellX, cellY)) {
                    temp.setBackground(ArenaUIConfig.virtualWallColor);
                }else {
                    temp.setBackground(ArenaUIConfig.freeSpaceColor);
                } 
                    
                    
                
            }
        }
    }

    public static void setPaintBtn() {
        for (Component c : drawingPanel.getComponents()) {
            if (c instanceof JButton) {
                String[] sBtn = ((JButton) c).getToolTipText().split(":");
                int cellX = Integer.parseInt(sBtn[0]);
                int cellY = Integer.parseInt(sBtn[1]);

                JButton temp = (JButton) c;
                if (exploredMap.startArea(cellX, cellY)) {
                    temp.setBackground(ArenaUIConfig.startColor);
                } else if (exploredMap.goalArea(cellX, cellY)) {
                    temp.setBackground(ArenaUIConfig.goalColor);
                } else if (exploredMap.isObstacle(cellX, cellY)) {
                    temp.setBackground(ArenaUIConfig.obstacleColor);
                } else if (exploredMap.isVirtualWall(cellX, cellY)) {
                    temp.setBackground(ArenaUIConfig.virtualWallColor);
                } else if (!exploredMap.checkValidCell(cellX, cellY)) {
                    temp.setBackground(ArenaUIConfig.virtualWallColor);
                } else {
                    temp.setBackground(ArenaUIConfig.freeSpaceColor);
                }
            }
        }
    }
    public static void setAllUnexplored () {
        for (Component c : drawingPanel.getComponents()) {
            if (c instanceof JButton) {
                String[] sBtn = ((JButton) c).getToolTipText().split(":");
                int cellX = Integer.parseInt(sBtn[0]);
                int cellY = Integer.parseInt(sBtn[1]);

                JButton temp = (JButton) c;
                if (exploredMap.startArea(cellX, cellY)) {
                    temp.setBackground(ArenaUIConfig.startColor);
                } else if (exploredMap.goalArea(cellX, cellY)) {
                    temp.setBackground(ArenaUIConfig.goalColor);
                } else {
                    temp.setBackground(ArenaUIConfig.unexploredColor);
                }
            }
        }
    }
    

    public static void paintMachine() {
        for (Component c : drawingPanel.getComponents()) {
            if (c instanceof JButton) {
                String[] sBtn = ((JButton) c).getToolTipText().split(":");
                int machineX = Integer.parseInt(sBtn[0]);
                int machineY = Integer.parseInt(sBtn[1]);
                JButton temp = (JButton) c;
                if (_machine.machineSize(machineX, machineY)) {
                    temp.setBackground(ArenaUIConfig.machineColor);
                    exploredMap.getCell(machineX, machineY).setVisited(true);
                }
                if (_machine.machineFacingCell(machineX, machineY)) {
                    temp.setBackground(ArenaUIConfig.machineFacingColor);
                }

            }
        }
    }

}
