/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algo;

import machine.Machine;
import arena.*;
import main.ArenaUI;

/**
 *
 * @author Kelvin
 */
public class ExplorationAlgorithm {
    private Arena exploredArena;
    private Arena realArena;
    private Machine machine;
    private int exploreLimit;
    private int timeLimit;
    private int areaExplored;
    private long startTime;
    private long endTime;
    
    public ExplorationAlgorithm(Arena exploredArena,Arena realArena, Machine machine,int exploreLimit, int timeLimit){
        this.exploredArena= exploredArena;
        this.realArena=realArena;
        this.machine=machine;
        this.exploreLimit=exploreLimit;
        this.timeLimit=timeLimit;
    }
    public void startExploration(){
        if(!machine.getSimulationMachine()){
            
        }
        System.out.println("Starting exploration...");

        startTime = System.currentTimeMillis();
        endTime = startTime + (timeLimit * 1000);
        
        senseAndRepaint();
        areaExplored = calculateAreaExplored();
        System.out.println("Explored Area: " + areaExplored);

        explorationLoop(machine.getMachineX(), machine.getMachineY());

    }
    // need rename lol
    private void explorationLoop(int x, int y){
        do {
            moveAlgo();
            senseAndRepaint();
            areaExplored = calculateAreaExplored();
            System.out.println("Area explored: " + areaExplored);

            if (machine.getMachineX()== x && machine.getMachineY() == y) {
                if (areaExplored >= 100) {
                    break;
                }
            }
            
        } while (areaExplored <= exploreLimit && System.currentTimeMillis() <= endTime);
            
            
        }
        //goBackToStart();
    
    private void moveAlgo(){
        if (lookRight()) {
            machine.turnRight();
            if (lookForward()) 
                machine.moveForward();
        } else if (lookForward()) {
            machine.moveForward();
        } else if (lookLeft()) {
            machine.turnLeft();
            if (lookForward()) 
                machine.moveForward();
        } else {
            machine.turnRight();
            machine.turnRight();
        }
    }
    private boolean lookLeft() {
        switch (machine.getMachineFacing()) {
            case NORTH:
                return westClear();
            case EAST:
                return northClear();
            case SOUTH:
                return eastClear();

            case WEST:
                return southClear();
        }
        return false;
    }

    private boolean lookRight() {
        switch (machine.getMachineFacing()) {
            case NORTH:
                return eastClear();
            case EAST:
                return southClear();
            case SOUTH:
                return westClear();

            case WEST:
                return northClear();
        }
        return false;
    }

    private boolean lookForward() {
        switch (machine.getMachineFacing()) {
            case NORTH:
                return northClear();
            case EAST:
                return eastClear();
            case SOUTH:
                return southClear();

            case WEST:
                return westClear();
        }
        return false;
    }
    private boolean northClear(){
        int x=machine.getMachineX();
        int y=machine.getMachineY();
        return(isExploredAndNotObstacle(x-1,y-1)&&isExploredAndNotObstacle(x-1,y+1)&&isExploredAndIsFree(x-1,y));
    }
    private boolean southClear(){
        int x=machine.getMachineX();
        int y=machine.getMachineY();
        return(isExploredAndNotObstacle(x+1,y-1)&&isExploredAndNotObstacle(x+1,y+1)&&isExploredAndIsFree(x+1,y));
    }
    private boolean eastClear(){
        int x=machine.getMachineX();
        int y=machine.getMachineY();
        return(isExploredAndNotObstacle(x-1,y+1)&&isExploredAndNotObstacle(x+1,y+1)&&isExploredAndIsFree(x,y+1));
    }
    private boolean westClear(){
        int x=machine.getMachineX();
        int y=machine.getMachineY();
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
    private void senseAndRepaint() {
        machine.setSensors();
        machine.detect(exploredArena, realArena);
        ArenaUI.repaintBtn();
        ArenaUI.paintMachine();
    }

    /**
     * Returns the number of cells explored in the grid.
     */
    private int calculateAreaExplored() {
        int result = 0;
        for (int x = 0; x < Arena.arenaX; x++) {
            for (int y = 0; y < Arena.arenaY; y++) {
                if (exploredArena.getCell(x, y).getIsVisited()) {
                    result++;
                }
            }
        }
        return result;
    }
    
}
