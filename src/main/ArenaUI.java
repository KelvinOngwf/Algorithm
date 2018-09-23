/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

//import arena.*;
import robot.*;
import javax.swing.*;
import java.awt.*;


/**
 *
 * @author Kelvin
 */
public class ArenaUI {
    
    public ArenaUI(){
        _DisplayCell[][] _mapCells = new _DisplayCell[Arena.arenaX][Arena.arenaY];
        for (int x = 0; x < Arena.arenaX; x++) {
            for (int y = 0; y < Arena.arenaY; y++) {
                _mapCells[x][y] = new _DisplayCell(x * 30, y * 30, 30);
            }
        }

        // Paint the cells with the appropriate colors.
        for (int x = 0; x < Arena.arenaX; x++) {
            for (int y = 0; y < Arena.arenaY; y++) {
                Color cellColor;

                if (inStartZone(x, y))
                    cellColor = Color.YELLOW;                
                else if (inGoalZone(x, y))
                    cellColor = Color.RED;
                else {
                    if (!c[x][y].getIsVisited())
                        cellColor = Color.GRAY;
                    else if (c[x][y].getIsObstacle())
                        cellColor = Color.BLACK;
                    else
                        cellColor = Color.WHITE;
                }

                g.setColor(cellColor);
                g.fillRect(_mapCells[x][y].cellX + 10, _mapCells[x][y].cellY, _mapCells[x][y].cellSize, _mapCells[x][y].cellSize);

            }
        }

        // Paint the robot on-screen.
        g.setColor(Color.CYAN);
        int r = robot.getRobotX();
        int c = robot.getRobotY();
        g.fillOval((c - 1) * GraphicsConstants.CELL_SIZE + GraphicsConstants.ROBOT_X_OFFSET + GraphicsConstants.MAP_X_OFFSET, GraphicsConstants.MAP_H - (r * GraphicsConstants.CELL_SIZE + GraphicsConstants.ROBOT_Y_OFFSET), GraphicsConstants.ROBOT_W, GraphicsConstants.ROBOT_H);

        // Paint the robot's direction indicator on-screen.
        g.setColor(GraphicsConstants.C_ROBOT_DIR);
        RobotConstants.DIRECTION d = bot.getRobotCurDir();
        switch (d) {
            case NORTH:
                g.fillOval(c * GraphicsConstants.CELL_SIZE + 10 + GraphicsConstants.MAP_X_OFFSET, GraphicsConstants.MAP_H - r * GraphicsConstants.CELL_SIZE - 15, GraphicsConstants.ROBOT_DIR_W, GraphicsConstants.ROBOT_DIR_H);
                break;
            case EAST:
                g.fillOval(c * GraphicsConstants.CELL_SIZE + 35 + GraphicsConstants.MAP_X_OFFSET, GraphicsConstants.MAP_H - r * GraphicsConstants.CELL_SIZE + 10, GraphicsConstants.ROBOT_DIR_W, GraphicsConstants.ROBOT_DIR_H);
                break;
            case SOUTH:
                g.fillOval(c * GraphicsConstants.CELL_SIZE + 10 + GraphicsConstants.MAP_X_OFFSET, GraphicsConstants.MAP_H - r * GraphicsConstants.CELL_SIZE + 35, GraphicsConstants.ROBOT_DIR_W, GraphicsConstants.ROBOT_DIR_H);
                break;
            case WEST:
                g.fillOval(c * GraphicsConstants.CELL_SIZE - 15 + GraphicsConstants.MAP_X_OFFSET, GraphicsConstants.MAP_H - r * GraphicsConstants.CELL_SIZE + 10, GraphicsConstants.ROBOT_DIR_W, GraphicsConstants.ROBOT_DIR_H);
                break;
        }
    }
    private class _DisplayCell {
        public final int cellX;
        public final int cellY;
        public final int cellSize;

        public _DisplayCell(int borderX, int borderY, int borderSize) {
            this.cellX = borderX + GraphicsConstants.CELL_LINE_WEIGHT;
            this.cellY = GraphicsConstants.MAP_H - (borderY - GraphicsConstants.CELL_LINE_WEIGHT);
            this.cellSize = borderSize - (GraphicsConstants.CELL_LINE_WEIGHT * 2);
        }
    }
    
}
