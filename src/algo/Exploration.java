/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algo;

import arena.*;
import robot.*;

/**
 *
 * @author Kelvin
 */
public class Exploration {
    private Arena exploredArena;
    private Arena realArena;
    private Robot robot;
    private int exploreLimit;
    private int timeLimit;
    private int areaExplored;
    private long startTime;
    private long endTime;
    
    public Exploration(Arena exploredArena,Arena realArena, Robot robot,int exploreLimit, int timeLimit){
        this.exploredArena= exploredArena;
        this.realArena=realArena;
        this.robot=robot;
        this.exploreLimit=exploreLimit;
        this.timeLimit=timeLimit;
    }
    public void startExploration(){
        if(!robot.getSimulationBot()){
            
        }
    }
    // need rename lol
    private void explorationLoop(int x, int y){
        while(areaExplored <= exploreLimit && System.currentTimeMillis()<= endTime){
            moveAlgo();
            
            areaExplored = calculateAreaExplored();
            System.out.println("Area explored: " + areaExplored);

            if (robot.getRobotX() == x && robot.getRobotY() == y) {
                if (areaExplored >= 100) {
                    break;
                }
            }
        }
    }
    private void moveAlgo(){
        if (lookRight()) {
            robot.turnRight();
            if (lookForward()) 
                robot.moveForward();
        } else if (lookForward()) {
            robot.moveForward();
        } else if (lookLeft()) {
            robot.turnLeft();
            if (lookForward()) 
                robot.moveForward();
        } else {
            robot.turnRight();
            robot.turnRight();
        }
    }
    private boolean lookLeft() {
        switch (robot.getRobotFacing()) {
            case "N":
                return westClear();
            case "S":
                return eastClear();
            case "E":
                return northClear();
            case "W":
                return southClear();
        }
        return false;
    }
    private boolean lookRight() {
        switch (robot.getRobotFacing()) {
            case "N":
                return eastClear();
            case "S":
                return westClear();
            case "E":
                return southClear();
            case "W":
                return northClear();
        }
        return false;
    }
    private boolean lookForward() {
        switch (robot.getRobotFacing()) {
            case "N":
                return northClear();
            case "S":
                return southClear();
            case "E":
                return eastClear();
            case "W":
                return westClear();
        }
        return false;
    }
    private boolean northClear(){
        int x=robot.getRobotX();
        int y=robot.getRobotY();
        return(isExploredAndNotObstacle(x-1,y-1)&&isExploredAndNotObstacle(x-1,y+1)&&isExploredAndIsFree(x+1,y));
    }
    private boolean southClear(){
        int x=robot.getRobotX();
        int y=robot.getRobotY();
        return(isExploredAndNotObstacle(x+1,y-1)&&isExploredAndNotObstacle(x+1,y+1)&&isExploredAndIsFree(x+1,y));
    }
    private boolean eastClear(){
        int x=robot.getRobotX();
        int y=robot.getRobotY();
        return(isExploredAndNotObstacle(x-1,y+1)&&isExploredAndNotObstacle(x+1,y+1)&&isExploredAndIsFree(x,y+1));
    }
    private boolean westClear(){
        int x=robot.getRobotX();
        int y=robot.getRobotY();
        return(isExploredAndNotObstacle(x-1,y-1)&&isExploredAndNotObstacle(x+1,y-1)&&isExploredAndIsFree(x,y-1));
    }
    private boolean isExploredAndNotObstacle(int x, int y) {
        if (exploredArena.checkValidCell(x, y)) {
            Cell temp = exploredArena.getCell(x, y);
            return (temp.getIsVisited() && !temp.getIsObstacle());
        }
        return false;
    }
    private boolean isExploredAndIsFree(int x, int y) {
        if (exploredArena.checkValidCell(x, y)) {
            Cell temp = exploredArena.getCell(x, y);
            return (temp.getIsVisited() && !temp.getVirtualWall() && !temp.getIsObstacle());
        }
        return false;
    }

    /**
     * Returns the number of cells explored in the grid.
     */
    private int calculateAreaExplored() {
        int result = 0;
        for (int x = 0; x < Arena.mapRow; x++) {
            for (int y = 0; y < Arena.mapCol; y++) {
                if (exploredArena.getCell(x, y).getIsVisited()) {
                    result++;
                }
            }
        }
        return result;
    }
    
}
