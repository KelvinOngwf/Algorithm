/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algo;

import machine.Machine;
import arena.*;
import machine.MachineConfig.FACING;
import machine.MachineConfig.MOVEMENT;
import main.ArenaUI;
import static main.MapDescriptor.generateMapDescriptor;

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
    private int lastCalibrate;
    private boolean calibrationMode;
    
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
            
            goHome();
            
        }
        //goBackToStart();
    
    private void moveAlgo(){
        if (lookRight()) {
            moveBot(MOVEMENT.RIGHT);
            if (lookForward()) 
                moveBot(MOVEMENT.FORWARD);
        } else if (lookForward()) {
            moveBot(MOVEMENT.FORWARD);
        } else if (lookLeft()) {
            moveBot(MOVEMENT.LEFT);
            if (lookForward()) moveBot(MOVEMENT.FORWARD);
        } else {
            moveBot(MOVEMENT.RIGHT);
            moveBot(MOVEMENT.RIGHT);
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
        return(isExploredAndNotObstacle(x+1,y-1)&&isExploredAndNotObstacle(x+1,y+1)&&isExploredAndIsFree(x+1,y));
    }
    private boolean southClear(){
        int x=machine.getMachineX();
        int y=machine.getMachineY();
        return(isExploredAndNotObstacle(x-1,y-1)&&isExploredAndNotObstacle(x-1,y+1)&&isExploredAndIsFree(x-1,y));
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
    private void goHome() {
        if (!machine.getReachedGoal() && exploreLimit == 300 && timeLimit == 3600) {
            FastestPathAlgorithm goToGoal = new FastestPathAlgorithm(exploredArena, machine, realArena);
            goToGoal.runFastestPath(exploredArena.goalX, exploredArena.goalY);
        }

        FastestPathAlgorithm returnToStart = new FastestPathAlgorithm(exploredArena, machine, realArena);
        returnToStart.runFastestPath(exploredArena.startX, exploredArena.startY);

        System.out.println("Exploration complete!");
        areaExplored = calculateAreaExplored();
        System.out.printf("%.2f%% Coverage", (areaExplored / 300.0) * 100.0);
        System.out.println(", " + areaExplored + " Cells");
        System.out.println((System.currentTimeMillis() - startTime) / 1000 + " Seconds");

        if (!machine.getSimulationMachine()) {
            turnBotDirection(FACING.WEST);
            moveBot(MOVEMENT.CALIBRATE);
            turnBotDirection(FACING.SOUTH);
            moveBot(MOVEMENT.CALIBRATE);
            turnBotDirection(FACING.WEST);
            moveBot(MOVEMENT.CALIBRATE);
        }
        turnBotDirection(FACING.EAST);
        generateMapDescriptor(exploredArena);
    }
    private void moveBot(MOVEMENT m) {
        machine.move(m);
        ArenaUI.repaintBtn();
        ArenaUI.paintMachine();
        if (m != MOVEMENT.CALIBRATE) {
            senseAndRepaint();
        } else {
            //CommMgr commMgr = CommMgr.getCommMgr();
         //   commMgr.recvMsg();
        }

        if (!machine.getSimulationMachine() && !calibrationMode) {
            calibrationMode = true;

            if (canCalibrateOnTheSpot(machine.getMachineFacing())) {
                lastCalibrate = 0;
                moveBot(MOVEMENT.CALIBRATE);
            } else {
                lastCalibrate++;
                if (lastCalibrate >= 5) {
                    FACING targetDir = getCalibrationDirection();
                    if (targetDir != null) {
                        lastCalibrate = 0;
                        calibrateBot(targetDir);
                    }
                }
            }

            calibrationMode = false;
        }
    }
    private boolean canCalibrateOnTheSpot(FACING machineF) {
        int row = machine.getMachineX();
        int col = machine.getMachineY();

        switch (machineF) {
            case NORTH:
                return exploredArena.getIsObstacleOrWall(row + 2, col - 1) && exploredArena.getIsObstacleOrWall(row + 2, col) && exploredArena.getIsObstacleOrWall(row + 2, col + 1);
            case EAST:
                return exploredArena.getIsObstacleOrWall(row + 1, col + 2) && exploredArena.getIsObstacleOrWall(row, col + 2) && exploredArena.getIsObstacleOrWall(row - 1, col + 2);
            case SOUTH:
                return exploredArena.getIsObstacleOrWall(row - 2, col - 1) && exploredArena.getIsObstacleOrWall(row - 2, col) && exploredArena.getIsObstacleOrWall(row - 2, col + 1);
            case WEST:
                return exploredArena.getIsObstacleOrWall(row + 1, col - 2) && exploredArena.getIsObstacleOrWall(row, col - 2) && exploredArena.getIsObstacleOrWall(row - 1, col - 2);
        }

        return false;
    }
    private FACING getCalibrationDirection() {
        FACING origDir = machine.getMachineFacing();
        FACING dirToCheck;

        dirToCheck = FACING.getNext(origDir);                    // right turn
        if (canCalibrateOnTheSpot(dirToCheck)) return dirToCheck;

        dirToCheck = FACING.getPrevious(origDir);                // left turn
        if (canCalibrateOnTheSpot(dirToCheck)) return dirToCheck;

        dirToCheck = FACING.getPrevious(dirToCheck);             // u turn
        if (canCalibrateOnTheSpot(dirToCheck)) return dirToCheck;

        return null;
    }

    /**
     * Turns the bot in the needed direction and sends the CALIBRATE movement. Once calibrated, the bot is turned back
     * to its original direction.
     */
    private void calibrateBot(FACING targetDir) {
        FACING origDir = machine.getMachineFacing();

        turnBotDirection(targetDir);
        moveBot(MOVEMENT.CALIBRATE);
        turnBotDirection(origDir);
    }
    private void senseAndRepaint() {
        machine.setSensors();
        machine.detect(exploredArena, realArena);
        ArenaUI.repaintBtn();
        ArenaUI.paintMachine();
    }
    private void turnBotDirection(FACING targetDir) {
        int numOfTurn = Math.abs(machine.getMachineFacing().ordinal() - targetDir.ordinal());
        if (numOfTurn > 2) numOfTurn = numOfTurn % 2;

        if (numOfTurn == 1) {
            if (FACING.getNext(machine.getMachineFacing()) == targetDir) {
                moveBot(MOVEMENT.RIGHT);
            } else {
                moveBot(MOVEMENT.LEFT);
            }
        } else if (numOfTurn == 2) {
            moveBot(MOVEMENT.RIGHT);
            moveBot(MOVEMENT.RIGHT);
        }
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
