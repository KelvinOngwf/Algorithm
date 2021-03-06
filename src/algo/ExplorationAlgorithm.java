/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algo;

import machine.Machine;
import arena.*;
import java.util.ArrayList;
import machine.MachineConfig.*;
import main.ArenaUI;
import main.CommMgr;
import static main.MapDescriptor.generateMapDescriptor;

/**
 *
 * @author Kelvin
 * @author Chris
 */
public class ExplorationAlgorithm {

    private Arena exploredArena;
    private Arena realArena;
    private Machine machine;
    private int exploreLimit;
    private int timeLimit;
    private int areaVisited;
    private long startTime;
    private long endTime;
    private int counter = 0;
    private int lastCalibrate;

    public ExplorationAlgorithm(Arena exploredArena, Arena realArena, Machine machine, int exploreLimit, int timeLimit) {
        this.exploredArena = exploredArena;
        this.realArena = realArena;
        this.machine = machine;
        this.exploreLimit = exploreLimit;
        this.timeLimit = timeLimit;
    }

    public void startExploration() {
        if (!machine.getSimulationMachine()) {
            while (true) {
                System.out.println("Waiting for EX_START...");
                String msg = CommMgr.getCommMgr().recvMsg();
                String[] msgArr = msg.split(";");
                if (msgArr[0].equalsIgnoreCase(CommMgr.EX_START)) {
                    break;
                }
            }
        }
        System.out.println("Starting exploration...");

        startTime = System.currentTimeMillis();
        endTime = startTime + (timeLimit * 1000);
        if (!machine.getSimulationMachine()) {
            CommMgr.getCommMgr().sendMsg(null, CommMgr.MACHINE_START);
        }
        detectRepaint();
        areaVisited = calculateAreaVisited();
        System.out.println("Explored Area: " + areaVisited);

        startExplorationLoop(machine.getMachineX(), machine.getMachineY());

    }

    private void startExplorationLoop(int x, int y) {
        lastCalibrate = 1;
        do {
            moveAlgo();
            //detectRepaint();
            
            areaVisited = calculateAreaVisited();
            System.out.println("Area explored: " + areaVisited);

            if (machine.getMachineX() == x && machine.getMachineY() == y) {
                if (areaVisited >= 100) {
                    break;
                }
            }

        } while (areaVisited <= exploreLimit && System.currentTimeMillis() <= endTime);
        checkUnexploredCell();
        backToStart();
        moveMachine(MOVEMENT.CALIBRATE);

    }

    private void checkUnexploredCell() {
        for (int i = exploredArena.arenaX - 1; i >= 0; i--) {
            for (int j = 0; j < exploredArena.arenaY; j++) {
                if (!exploredArena.getCell(i, j).getIsVisited()) {
                    if (!exploredArena.getCell(i, j + 2).getIsObstacle() && exploredArena.getCell(i, j + 2).getIsVisited()) {
                        FastestPathAlgorithm test = new FastestPathAlgorithm(exploredArena, machine, realArena);
                        test.runFastestPath(i, j + 2);
                    } else if (!exploredArena.getCell(i, j - 2).getIsObstacle() && exploredArena.getCell(i, j - 2).getIsVisited()) {
                        FastestPathAlgorithm test = new FastestPathAlgorithm(exploredArena, machine, realArena);
                        test.runFastestPath(i, j - 2);
                    } else if (!exploredArena.getCell(i - 2, j).getIsObstacle() && exploredArena.getCell(i - 2, j).getIsVisited()) {
                        FastestPathAlgorithm test = new FastestPathAlgorithm(exploredArena, machine, realArena);
                        test.runFastestPath(i - 2, j);
                    } else if (!exploredArena.getCell(i + 2, j).getIsObstacle() && exploredArena.getCell(i + 2, j).getIsVisited()) {
                        FastestPathAlgorithm test = new FastestPathAlgorithm(exploredArena, machine, realArena);
                        test.runFastestPath(i + 2, j);
                    }
                }
            }
        }
    }
        
    private void moveAlgo() {

        if (counter < 5) {
            if (checkRight()) {
                moveMachine(MOVEMENT.RIGHT);
                if (checkForward()) {
                    moveMachine(MOVEMENT.FORWARD);
                }
                counter++;
            } else if (checkForward()) {
                moveMachine(MOVEMENT.FORWARD);
                counter = 0;
            } else if (checkLeft()) {
                moveMachine(MOVEMENT.LEFT);
                if (checkForward()) {
                    moveMachine(MOVEMENT.FORWARD);
                }
                counter = 0;
            } else {
                moveMachine(MOVEMENT.RIGHT);
                moveMachine(MOVEMENT.RIGHT);
            }
        } else {
            FACING curr = machine.getMachineFacing();
            switch (curr) {
                case NORTH:
                    while (!exploredArena.getIsObstacleOrWall(machine.getMachineX() + 2, machine.getMachineY() - 1) && !exploredArena.getIsObstacleOrWall(machine.getMachineX() + 2, machine.getMachineY()) && !exploredArena.getIsObstacleOrWall(machine.getMachineX() + 2, machine.getMachineY() + 1)) {
                        if (checkForward()) {
                            moveMachine(MOVEMENT.FORWARD);
                        } else {
                            moveMachine(MOVEMENT.LEFT);
                        }
                    }
                    moveMachine(MOVEMENT.LEFT);
                    counter = 0;
                    break;
                case SOUTH:
                    while (!exploredArena.getIsObstacleOrWall(machine.getMachineX() - 2, machine.getMachineY() - 1) && !exploredArena.getIsObstacleOrWall(machine.getMachineX() - 2, machine.getMachineY()) && !exploredArena.getIsObstacleOrWall(machine.getMachineX() - 2, machine.getMachineY() + 1)) {
                        if (checkForward()) {
                            moveMachine(MOVEMENT.FORWARD);
                        } else {
                            moveMachine(MOVEMENT.LEFT);
                        }
                    }
                    moveMachine(MOVEMENT.LEFT);
                    counter = 0;
                    break;
                case EAST:
                    while (!exploredArena.getIsObstacleOrWall(machine.getMachineX() - 1, machine.getMachineY() + 2) && !exploredArena.getIsObstacleOrWall(machine.getMachineX(), machine.getMachineY() + 2) && !exploredArena.getIsObstacleOrWall(machine.getMachineX() + 1, machine.getMachineY() + 2)) {
                        if (checkForward()) {
                            moveMachine(MOVEMENT.FORWARD);
                        } else {
                            moveMachine(MOVEMENT.LEFT);
                        }
                    }
                    moveMachine(MOVEMENT.LEFT);
                    counter = 0;
                    break;
                case WEST:
                    while (!exploredArena.getIsObstacleOrWall(machine.getMachineX() - 1, machine.getMachineY() - 2) && !exploredArena.getIsObstacleOrWall(machine.getMachineX(), machine.getMachineY() + 2) && !exploredArena.getIsObstacleOrWall(machine.getMachineX() + 1, machine.getMachineY() + 2)) {
                        if (checkForward()) {
                            moveMachine(MOVEMENT.FORWARD);
                        } else {
                            moveMachine(MOVEMENT.LEFT);
                        }
                    }
                    moveMachine(MOVEMENT.LEFT);
                    counter = 0;
                    break;
            }
        }
        switch(machine.getMachineFacing()){
                case NORTH:
                    if(exploredArena.getIsObstacleOrWall(machine.getMachineX()+2, machine.getMachineY()-1)&&exploredArena.getIsObstacleOrWall(machine.getMachineX()+2, machine.getMachineY())&&exploredArena.getIsObstacleOrWall(machine.getMachineX()+2, machine.getMachineY()+1)
                            &&exploredArena.getIsObstacleOrWall(machine.getMachineX()+1, machine.getMachineY()+2)&&exploredArena.getIsObstacleOrWall(machine.getMachineX(), machine.getMachineY()+2)&&exploredArena.getIsObstacleOrWall(machine.getMachineX()-1, machine.getMachineY()+2))
                    {
                        moveMachine(MOVEMENT.CALIBRATE);
                        lastCalibrate=1;
                    }
                    break;
                case SOUTH:
                    if(exploredArena.getIsObstacleOrWall(machine.getMachineX()-2, machine.getMachineY()-1)&&exploredArena.getIsObstacleOrWall(machine.getMachineX()-2, machine.getMachineY())&&exploredArena.getIsObstacleOrWall(machine.getMachineX()-2, machine.getMachineY()+1)
                            &&exploredArena.getIsObstacleOrWall(machine.getMachineX()-1, machine.getMachineY()-2)&&exploredArena.getIsObstacleOrWall(machine.getMachineX(), machine.getMachineY()-2)&&exploredArena.getIsObstacleOrWall(machine.getMachineX()+1, machine.getMachineY()-2))
                    {
                        moveMachine(MOVEMENT.CALIBRATE);
                        lastCalibrate = 1;
                    }
                    break;
                case EAST:
                    if(exploredArena.getIsObstacleOrWall(machine.getMachineX()-1, machine.getMachineY()+2)&&exploredArena.getIsObstacleOrWall(machine.getMachineX(), machine.getMachineY()+2)&&exploredArena.getIsObstacleOrWall(machine.getMachineX()+1, machine.getMachineY()+2)
                            &&exploredArena.getIsObstacleOrWall(machine.getMachineX()-2, machine.getMachineY()-1)&&exploredArena.getIsObstacleOrWall(machine.getMachineX()-2, machine.getMachineY())&&exploredArena.getIsObstacleOrWall(machine.getMachineX()-2, machine.getMachineY()+1))
                    {
                        moveMachine(MOVEMENT.CALIBRATE);
                        lastCalibrate = 1;
                    }
                    break;
                case WEST:
                    if(exploredArena.getIsObstacleOrWall(machine.getMachineX()-1, machine.getMachineY()-2)&&exploredArena.getIsObstacleOrWall(machine.getMachineX(), machine.getMachineY()-2)&&exploredArena.getIsObstacleOrWall(machine.getMachineX()+1, machine.getMachineY()-2)
                            &&exploredArena.getIsObstacleOrWall(machine.getMachineX()+2, machine.getMachineY()-1)&&exploredArena.getIsObstacleOrWall(machine.getMachineX()+2, machine.getMachineY())&&exploredArena.getIsObstacleOrWall(machine.getMachineX()+2, machine.getMachineY()+1))
                    {
                        moveMachine(MOVEMENT.CALIBRATE);
                        lastCalibrate = 1;
                    }
                    break;
            }
        if (lastCalibrate % 3 == 0) {
                if (canCalibrate()) {
                    lastCalibrate = 1;
                    moveMachine(MOVEMENT.CALIBRATE);
                    
                }
            } else {
                lastCalibrate++;
            }
    }

    private boolean checkLeft() {
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

    private boolean checkRight() {
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

    private boolean checkForward() {
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

    private boolean northClear() {
        int x = machine.getMachineX();
        int y = machine.getMachineY();
        return (isExploredAndNotObstacle(x + 1, y - 1) && isExploredAndNotObstacle(x + 1, y + 1) && isExploredAndIsFree(x + 1, y));
    }

    private boolean southClear() {
        int x = machine.getMachineX();
        int y = machine.getMachineY();
        return (isExploredAndNotObstacle(x - 1, y - 1) && isExploredAndNotObstacle(x - 1, y + 1) && isExploredAndIsFree(x - 1, y));
    }

    private boolean eastClear() {
        int x = machine.getMachineX();
        int y = machine.getMachineY();
        return (isExploredAndNotObstacle(x - 1, y + 1) && isExploredAndNotObstacle(x + 1, y + 1) && isExploredAndIsFree(x, y + 1));
    }

    private boolean westClear() {
        int x = machine.getMachineX();
        int y = machine.getMachineY();
        return (isExploredAndNotObstacle(x - 1, y - 1) && isExploredAndNotObstacle(x + 1, y - 1) && isExploredAndIsFree(x, y - 1));
    }

    private boolean canCalibrate() {
        int x = machine.getMachineX();
        int y = machine.getMachineY();
        switch (machine.getMachineFacing()) {
            case NORTH:
                return ((isExploredAndIsObstacleOrWall(x + 1, y + 2) && isExploredAndIsObstacleOrWall(x - 1, y + 2)) || (isExploredAndIsObstacleOrWall(x + 2, y - 1) && isExploredAndIsObstacleOrWall(x + 2, y) && isExploredAndIsObstacleOrWall(x + 2, y + 1)));
            case SOUTH:
                return ((isExploredAndIsObstacleOrWall(x + 1, y - 2) && isExploredAndIsObstacleOrWall(x - 1, y - 2)) || (isExploredAndIsObstacleOrWall(x - 2, y - 1) && isExploredAndIsObstacleOrWall(x - 2, y) && isExploredAndIsObstacleOrWall(x - 2, y + 1)));
            case EAST:
                return ((isExploredAndIsObstacleOrWall(x - 2, y + 1) && isExploredAndIsObstacleOrWall(x - 2, y - 1)) || (isExploredAndIsObstacleOrWall(x + 1, y + 2) && isExploredAndIsObstacleOrWall(x, y + 2) && isExploredAndIsObstacleOrWall(x - 1, y + 2)));
            case WEST:
                return ((isExploredAndIsObstacleOrWall(x + 2, y + 1) && isExploredAndIsObstacleOrWall(x + 2, y - 1)) || (isExploredAndIsObstacleOrWall(x - 1, y - 2) && isExploredAndIsObstacleOrWall(x, y - 2) && isExploredAndIsObstacleOrWall(x + 1, y - 2)));
        }

        return false;
    }

    private boolean isExploredAndNotObstacle(int x, int y) {
        if (exploredArena.checkValidCell(x, y)) {
            Cell temp = exploredArena.getCell(x, y);
            return (temp.getIsVisited() && !temp.getIsObstacle());
        }
        return false;
    }

    private boolean isExploredAndIsObstacleOrWall(int x, int y) {
        if (exploredArena.checkValidCell(x, y)) {
            if (exploredArena.getIsObstacleOrWall(x, y)) {
                return true;
            }
        }
        if(!exploredArena.checkValidCell(x, y))
            return true;
        return false;
    }

    private boolean isExploredAndIsFree(int x, int y) {
        if (exploredArena.checkValidCell(x, y)) {
            Cell temp = exploredArena.getCell(x, y);
            return (temp.getIsVisited() && !temp.getVirtualWall() && !temp.getIsObstacle());
        }
        return false;
    }

    private void backToStart() {
        FastestPathAlgorithm returnToStart = new FastestPathAlgorithm(exploredArena, machine, realArena);
        returnToStart.runFastestPath(exploredArena.startX, exploredArena.startY);

        System.out.println("Exploration complete!");
        areaVisited = calculateAreaVisited();
        System.out.printf("%.2f%% Coverage", (areaVisited / 300.0) * 100.0);
        System.out.println(", " + areaVisited + " Cells");
        System.out.println((System.currentTimeMillis() - startTime) / 1000 + " Seconds");

        turnMachineFacing(FACING.EAST);
    }

    private void moveMachine(MOVEMENT m) {
        machine.move(m);
        detectRepaint();
    }

    private void detectRepaint() {
        machine.setSensors();
        machine.detect(exploredArena, realArena);
        ArenaUI.repaintBtn();
        ArenaUI.paintMachine();
    }

    private void turnMachineFacing(FACING targetDir) {
        int numOfTurn = Math.abs(machine.getMachineFacing().ordinal() - targetDir.ordinal());
        if (numOfTurn > 2) {
            numOfTurn = numOfTurn % 2;
        }

        if (numOfTurn == 1) {
            if (FACING.getNext(machine.getMachineFacing()) == targetDir) {
                moveMachine(MOVEMENT.RIGHT);
            } else {
                moveMachine(MOVEMENT.LEFT);
            }
        } else if (numOfTurn == 2) {
            moveMachine(MOVEMENT.RIGHT);
            moveMachine(MOVEMENT.RIGHT);
        }
    }

    private int calculateAreaVisited() {
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
