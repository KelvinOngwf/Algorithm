/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import arena.*;
import robot.*;
import javax.swing.*;
import java.awt.*;


/**
 *
 * @author Kelvin
 */
public class ArenaUI {
    private int arenaSize = 300;
    private int CELL_LINE_WEIGHT = 2;

    private Color startColor = Color.BLUE;
    private Color goalColor = Color.YELLOW;
    private Color unexploredColor = Color.LIGHT_GRAY;
    private Color freeSpaceColor = Color.WHITE;
    private Color obstacleColor = Color.BLACK;

    private Color machineColor = Color.BLACK;
    private Color machineFacingColor = Color.WHITE;

    private int machineWidth = 70;
    private int machineHeight = 70;

    private int machineXOffset = 10;
    private int machineYOffset = 20;

    private int machineFacingWidth = 10;
    private int machineFacingHeight = 10;

    private int cellSize = 30;

    private int arenaHeight = 600;
    private int arenaXOffset = 120;
    
    
    
    
    public void paintComponent(Graphics g) {
        // Create a two-dimensional array of _DisplayCell objects for rendering.
        DisplayCell[][] mapCells = new DisplayCell[Arena.arenaX][Arena.arenaY];
        for (int x = 0; x < Arena.arenaX; x++) {
            for (int y = 0; y < Arena.arenaY; y++) {
                mapCells[x][y] = new DisplayCell(y * cellSize, x * cellSize, cellSize);
            }
        }

        // Paint the cells with the appropriate colors.
        for (int x = 0; x < Arena.arenaX; x++) {
            for (int y = 0; y < Arena.arenaY; y++) {
                Color cellColor;

                if (inStartZone(x, y))
                    cellColor = startColor;
                else if (inGoalZone(x, y))
                    cellColor = goalColor;
                else {
                    if (!c[x][y].getIsVisited())
                        cellColor = unexploredColor;
                    else if (grid[x][y].getIsObstacle())
                        cellColor = obstacleColor;
                    else
                        cellColor = freeSpaceColor;
                }

                g.setColor(cellColor);
                g.fillRect(mapCells[x][y].cellX + arenaXOffset, mapCells[x][y].cellY, mapCells[x][y].cellSize, mapCells[x][y].cellSize);

            }
        }

        // Paint the robot on-screen.
        g.setColor(machineColor);
        int r = bot.getRobotPosRow();
        int c = bot.getRobotPosCol();
        g.fillOval((c - 1) * cellSize + machineXOffset + arenaXOffset, arenaHeight - (r * cellSize + machineYOffset), machineWidth, machineHeight);

        // Paint the robot's direction indicator on-screen.
        g.setColor(machineFacingColor);
        private String machineFacing = Machine.getRobotFacing();
        switch (d) {
            case "N":
                g.fillOval(c * cellSize + 10 + arenaXOffset, arenaHeight - r * cellSize - 15, machineFacingWidth, machineFacingHeight);
                break;
            case "E":
                g.fillOval(c * cellSize + 35 + arenaXOffset, arenaHeight - r * cellSize + 10, machineFacingWidth, machineFacingHeight);
                break;
            case "S":
                g.fillOval(c * cellSize + 10 + arenaXOffset, arenaHeight - r * cellSize + 35, machineFacingWidth, machineFacingHeight);
                break;
            case "W":
                g.fillOval(c * cellSize - 15 + arenaXOffset, arenaHeight - r * cellSize + 10, machineFacingWidth, machineFacingHeight);
                break;
        }
    }

    private class DisplayCell {
        public final int cellX;
        public final int cellY;
        public final int cellSize;

        public DisplayCell(int borderX, int borderY, int borderSize) {
            cellX = borderX + CELL_LINE_WEIGHT;
            cellY = arenaHeight - (borderY - CELL_LINE_WEIGHT);
            cellSize = borderSize - (CELL_LINE_WEIGHT * 2);
        }
    }
    
}
