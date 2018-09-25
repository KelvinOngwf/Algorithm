/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import arena.*;
import robot.*;
import javax.swing.*;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Kelvin
 */
public class ArenaUI{

    private static int arenaSize = 300;
    private static int CELL_LINE_WEIGHT = 2;
    private static Color startColor = Color.BLUE;
    private static Color goalColor = Color.YELLOW;
    private static Color unexploredColor = Color.LIGHT_GRAY;
    private static Color freeSpaceColor = Color.WHITE;
    private static Color obstacleColor = Color.BLACK;
    private static Color machineColor = Color.BLACK;
    private static Color virtualWallColor = Color.PINK;
    private static Color machineFacingColor = Color.WHITE;
    private static int machineWidth = 70;
    private static int machineHeight = 70;
    private static int machineXOffset = 10;
    private static int machineYOffset = 20;
    private static int machineFacingWidth = 10;
    private static int machineFacingHeight = 10;
    private static int cellSize = 30;
    private static int arenaHeight = 600;
    private static int arenaXOffset = 120;
    private static JFrame appFrame = null;        // application JFrame
    private static JButton appBtn = null;    
    private static JPanel panel = null;
    private static Arena realMap = null;              // real map
    private static Arena exploredMap = null;          // exploration map
    private int timeLimit = 3600;            // time limit
    private int coverageLimit = 300;         // coverage limit
    //private final CommMgr comm = CommMgr.getCommMgr();
    private final boolean realRun = true;
    private static Arena _arena;
    private static Machine _machine;
    private static ArenaCls as;

    
    
    public static void main(String[] args) {
        populateArena();

    }

    public static void populateArena(){

        _arena = new Arena(_machine);
        _machine = new Machine(_arena.getStartX(),_arena.getStartY(),"E",false);
        
        as = new ArenaCls(_arena,_machine);
        
        appFrame  = new JFrame("MDP GRP 25");
        JPanel simulatorPanel = new JPanel(new GridLayout(20,15));
        JPanel realPanel = new JPanel(new GridLayout(20,15));
        JPanel container = new JPanel(new BorderLayout());
        

        for(int i=0; i<20; i++){
            for(int j=0; j<15; j++){
                appBtn = new JButton();
                appBtn.setBounds(j*5,i*5,5,5);
                simulatorPanel.add(appBtn);
            }
        }
        for(int i=0; i<20; i++){
            for(int j=0; j<15; j++){
                appBtn = new JButton();
                appBtn.setBounds(j*5,i*5,5,5);
                realPanel.add(appBtn);
            }
        }
        appFrame.add(as);
        //appFrame.add(container);
        appFrame.setSize(1024, 768);
        appFrame.setVisible(true);
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }
    
    
    private static class ArenaCls extends JPanel
    {
        private static Arena _arena;
        private  static Machine _machine;   
        public ArenaCls(Arena map,Machine machine){
            this._arena = map;
            this._machine=machine;
        }
        
        public void paintComponent(Graphics g) {
        // Create a two-dimensional array of _DisplayCell objects for rendering.
        DisplayCell[][] _mapCells = new DisplayCell[Arena.arenaX][Arena.arenaY];
        for (int mapRow = 0; mapRow < Arena.arenaX; mapRow++) {
            for (int mapCol = 0; mapCol < Arena.arenaY; mapCol++) {
                _mapCells[mapRow][mapCol] = new DisplayCell(mapCol * cellSize, mapRow * cellSize, cellSize);
            }
        }

        // Paint the cells with the appropriate colors.
        for (int mapRow = 0; mapRow < _arena.arenaX; mapRow++) {
            for (int mapCol = 0; mapCol < _arena.arenaY; mapCol++) {
                Color cellColor;

                if (_arena.startArea(mapRow, mapCol))
                    cellColor =startColor;
                else if (_arena.goalArea(mapRow, mapCol))
                    cellColor = goalColor;
                else {
                    if (!_arena.getCell(mapCol, mapCol).getIsVisited())
                        cellColor = unexploredColor;
                    else if (_arena.getCell(mapCol, mapCol).getIsObstacle())
                        cellColor = obstacleColor;
                    else if (_arena.getCell(mapCol, mapCol).getVirtualWall())
                        cellColor = virtualWallColor;
                    else
                        cellColor = freeSpaceColor;
                }

                g.setColor(cellColor);
                g.fillRect(_mapCells[mapRow][mapCol].cellX + arenaXOffset, _mapCells[mapRow][mapCol].cellY, _mapCells[mapRow][mapCol].cellSize, _mapCells[mapRow][mapCol].cellSize);

            }
        }

        // Paint the robot on-screen.
        g.setColor(machineColor);
        int x = _machine.getRobotX();
        int y = _machine.getRobotY();
        g.fillOval((y - 1) * cellSize + machineXOffset + arenaXOffset, arenaHeight - (x * cellSize + machineYOffset), machineWidth, machineHeight);

        // Paint the robot's direction indicator on-screen.
        g.setColor(machineFacingColor);
        String d = _machine.getRobotFacing();
        switch (d) {
            case "N":
                g.fillOval(y * cellSize + 10 + arenaXOffset, arenaHeight - x * cellSize - 15, machineFacingWidth, machineFacingHeight);
                break;
            case "E":
                g.fillOval(y * cellSize + 35 + arenaXOffset, arenaHeight - x * cellSize + 10, machineFacingWidth, machineFacingHeight);
                break;
            case "S":
                g.fillOval(y * cellSize + 10 + arenaXOffset, arenaHeight - x * cellSize + 35, machineFacingWidth, machineFacingHeight);
                break;
            case "W":
                g.fillOval(y * cellSize - 15 + arenaXOffset, arenaHeight - x * cellSize + 10, machineFacingWidth, machineFacingHeight);
                break;
        }
    }

    private static class DisplayCell {
        public final int cellX;
        public final int cellY;
        public final int cellSize;

        public DisplayCell(int borderX, int borderY, int borderSize) {
            this.cellX = borderX + CELL_LINE_WEIGHT;
            this.cellY = borderY - CELL_LINE_WEIGHT;
            this.cellSize = borderSize - (CELL_LINE_WEIGHT * 2);
        }
    }
    }

}

