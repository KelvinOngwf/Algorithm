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
        

        areaExplored = calculateAreaExplored();
        System.out.println("Explored Area: " + areaExplored);

        explorationLoop(machine.getMachineX(), machine.getMachineY());
        
        machine.moveForward();
        System.out.print(machine.getMachineX() +  " : " + machine.getMachineY());
        
    }
    // need rename lol
    private void explorationLoop(int x, int y){
        do {
            
            
            
            

            areaExplored = calculateAreaExplored();
            System.out.println("Area explored: " + areaExplored);

            if (machine.getMachineX()== x && machine.getMachineY() == y) {
                if (areaExplored >= 100) {
                    break;
                }
            }
            ArenaUI.paintMachine();
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
            case "N":
                return westClear();
            case "E":
                return northClear();
            case "S":
                return eastClear();

            case "W":
                return southClear();
        }
        return false;
    }

    private boolean lookRight() {
        switch (machine.getMachineFacing()) {
            case "N":
                return eastClear();
            case "E":
                return southClear();
            case "S":
                return westClear();

            case "W":
                return northClear();
        }
        return false;
    }

    private boolean lookForward() {
        switch (machine.getMachineFacing()) {
            case "N":
                return northClear();
            case "E":
                return eastClear();
            case "S":
                return southClear();

            case "W":
                return westClear();
        }
        return false;
    }
    private boolean northClear(){
        int x=machine.getMachineX();
        int y=machine.getMachineY();
        return(isExploredAndNotObstacle(x-1,y-1)&&isExploredAndNotObstacle(x-1,y+1)&&isExploredAndIsFree(x+1,y));
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
