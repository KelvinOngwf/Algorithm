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
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author Kelvin
 */
public class ArenaUI{

    private static JFrame appFrame = null;        // application JFrame   
    private static JPanel panel = null;
    private static Arena realMap = null;              // real map
    private static Arena exploredMap = null;          // exploration map
    private static int timeLimit = 3600;            // time limit
    private static int coverageLimit = 300;         // coverage limit
    //private final CommMgr comm = CommMgr.getCommMgr();
    private final boolean realRun = true;
    private static Arena _arena;
    private static Machine _machine;
    private static JPanel  drawingPanel = new JPanel(new GridLayout(21,15));
    private static JPanel container = new JPanel(new BorderLayout());
    //private static ArenaCls as;

    
    
    public static void main(String[] args) {
        
        populateArena();
        class Exploration extends SwingWorker<Integer,String>{
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
        
        
        new Exploration().execute();
    }

    public static void populateArena(){

        _arena = new Arena(_machine);
        _machine = new Machine(_arena.getStartX(),_arena.getStartY(),"E",false);
        
        //as = new ArenaCls(_arena,_machine);
        
        appFrame  = new JFrame("MDP GRP 25");
        
        appFrame.setResizable(false);
        JLabel label =null;
        for(int j=0;j<16;j++)
        {
            if(j==0){
                label = new JLabel(" ");
                drawingPanel.add(label);
            }
            else{
             label = new JLabel("j"+(j-1));
             drawingPanel.add(label);
            }
        }
        for(int i=0; i<20; i++){
              label = new JLabel("i"+i);
             drawingPanel.add(label);
            for(int j=0; j<15; j++){
                JButton _appBtn = new JButton();
                _appBtn.setBounds(j*5,i*5,5,5);
                _appBtn.setToolTipText(i+":"+j);
                String[] cellCord = _appBtn.getToolTipText().split(":");
                int cellX = Integer.parseInt(cellCord[0]);
                int cellY = Integer.parseInt(cellCord[1]);
                repaintBtn();
                _appBtn.addMouseListener(new MouseAdapter(){
                    public void mousePressed(MouseEvent e){
                        if((_appBtn.getBackground()==ArenaUIConfig.freeSpaceColor||_appBtn.getBackground()==ArenaUIConfig.virtualWallColor)&&_arena.checkValidCell(cellX, cellY)){
                        _appBtn.setBackground(ArenaUIConfig.obstacleColor);
                        _arena.placeObstacle(cellX, cellY, true);
                        }
                        else if(_appBtn.getBackground()==ArenaUIConfig.obstacleColor){
                            _appBtn.setBackground(ArenaUIConfig.freeSpaceColor);
                        _arena.placeObstacle(cellX, cellY, false);
                        }
                        repaintBtn();
                    }
                });
                drawingPanel.add(_appBtn);
            }
        }
        container.add(drawingPanel,BorderLayout.LINE_START);
        appFrame.add(container);
        appFrame.setSize(1024, 768);
        appFrame.setVisible(true);
        repaintBtn();
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }
    public static void repaintBtn(){
        for (Component c : drawingPanel.getComponents()) {
            if (c instanceof JButton) {
                String[] sBtn = ((JButton) c).getToolTipText().split(":");
                int cellX = Integer.parseInt(sBtn[0]);
                int cellY = Integer.parseInt(sBtn[1]);

                JButton temp = (JButton) c;
                if(_arena.startArea(cellX, cellY)){
                    temp.setBackground(ArenaUIConfig.startColor);
                }
                else if( _arena.goalArea(cellX, cellY)){
                    temp.setBackground(ArenaUIConfig.goalColor);
                }
                else if(_arena.isObstacle(cellX, cellY)){
                    temp.setBackground(ArenaUIConfig.obstacleColor);
                }
                else if(_arena.isVirtualWall(cellX, cellY)){
                    temp.setBackground(ArenaUIConfig.virtualWallColor);
                }
                else if(!_arena.checkValidCell(cellX, cellY)){
                    temp.setBackground(ArenaUIConfig.virtualWallColor);
                }
                else{
                    temp.setBackground(ArenaUIConfig.freeSpaceColor);
                }
            }
        }
    }
    public static void paintMachine() {
        for (Component c : drawingPanel.getComponents()) {
            if (c instanceof JButton) {
                int machineX = _machine.getMachineX();
                int machineY = _machine.getMachineY();
                JButton temp = (JButton) c;
                if(_machine.machineSize(machineX, machineY)){
                    temp.setBackground(ArenaUIConfig.machineColor);
                }
                if(_machine.machineFacingCell(machineX, machineY)){
                    temp.setBackground(ArenaUIConfig.machineFacingColor);
                }
                
                repaintBtn();
            }
        }
    }
    
}

