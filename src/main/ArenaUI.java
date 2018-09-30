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
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
    private static JPanel buttonsPanel = new JPanel(new FlowLayout());
    private static JPanel container = new JPanel(new BorderLayout());
    //private static ArenaCls as;

    public static void main(String[] args) {
        _arena = new Arena(_machine);
        exploredMap= new Arena(_machine);
        realMap = new Arena(_machine);
        _machine = new Machine(_arena.getStartX(), _arena.getStartY(), "E", simulationRun);

        populateArena();
        paintMachine();

        class Exploration extends SwingWorker<Integer, String> {
            protected Integer doInBackground() throws Exception {
                int x, y;

                x = _arena.getStartX();
                y = _arena.getStartY();

                _machine.setMachine(x, y);

                ExplorationAlgorithm exploration;
                exploration = new ExplorationAlgorithm(exploredMap, realMap, _machine, coverageLimit, timeLimit);

                exploration.startExploration();
                //generateMapDescriptor(exploredMap);

                return 111;
            }
        }

        JButton _appBtn = new JButton("Edit Arena");
        _appBtn.setName("EditBtn");
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
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                String command = actionEvent.getActionCommand();
                System.out.println("Selected: " + command);
            }
        };
        buttonsPanel.add(_appBtn);
        _appBtn = new JButton("Exploration");
        _appBtn.setName("explorationBtn");
        _appBtn.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                new Exploration().execute();
            }
        });
        buttonsPanel.add(_appBtn);
        _appBtn = new JButton("Fastest Path");
        _appBtn.setName("FastestPathBtn");
        _appBtn.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                //new FastestPath().execute();
            }
        });
        buttonsPanel.add(_appBtn);

    }

    public static void populateArena() {

        

        appFrame = new JFrame("MDP GRP 25");

        appFrame.setResizable(false);
        JLabel label = null;
        for (int j = 0; j < 16; j++) {
            if (j == 0) {
                label = new JLabel(" ");
                drawingPanel.add(label);
            } else {
                label = new JLabel("  " + (j - 1));
                drawingPanel.add(label);
            }
        }
        for (int i = 0; i < 20; i++) {
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
                        } else if (_appBtn.getBackground() == ArenaUIConfig.obstacleColor) {
                            _appBtn.setBackground(ArenaUIConfig.freeSpaceColor);
                            exploredMap.placeObstacle(cellX, cellY, false);
                        }
                        setPaintBtn();
                    }
                });
                drawingPanel.add(_appBtn);
            }
        }
        container.add(drawingPanel, BorderLayout.LINE_START);
        container.add(buttonsPanel, BorderLayout.LINE_END);
        appFrame.add(container);
        appFrame.setSize(1024, 768);
        appFrame.setVisible(true);
        repaintBtn();
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public static void repaintBtn() {
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
                } else if (exploredMap.getCell(cellX, cellY).getIsVisited()) {
                    temp.setBackground(ArenaUIConfig.freeSpaceColor);
                } else {
                    temp.setBackground(ArenaUIConfig.unexploredColor);
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
