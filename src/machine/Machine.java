/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package machine;

import arena.Arena;
import java.util.concurrent.TimeUnit;
import machine.MachineConfig.FACING;
import machine.MachineConfig.MOVEMENT;
import main.CommMgr;
import main.MapDescriptor;

/**
 *
 * @author Kelvin
 * @author Chris
 */
public class Machine {

    private int currentX;
    private int currentY;
    private FACING currentF;
    private boolean reachedGoal;
    private int speed;
    public static boolean simulationMachine;
    //right sensor
    public static Sensor sensorRF;
    //front right sensor
    public static Sensor sensorFR;
    //front centre sensor
    public static Sensor sensorFC;
    //front left sensor
    public static Sensor sensorFL;
    //left sendor
    public static Sensor sensorRB;
    //left Long range sensor
    public static Sensor sensorLL;

    public Machine(int x, int y, FACING facing, boolean simulationMachine) {
        currentX = x;
        currentY = y;
        this.speed = MachineConfig.speed;
        currentF = facing;
        reachedGoal = false;
        this.simulationMachine = simulationMachine;
        sensorRF = new Sensor(MachineConfig.sensorShortRangeUpperRange, MachineConfig.sensorShortRangeLowerRange, "SRF");
        sensorFR = new Sensor(MachineConfig.sensorShortRangeUpperRange, MachineConfig.sensorShortRangeLowerRange, "SFR");
        sensorFC = new Sensor(MachineConfig.sensorShortRangeUpperRange, MachineConfig.sensorShortRangeLowerRange, "SFC");
        sensorFL = new Sensor(MachineConfig.sensorShortRangeUpperRange, MachineConfig.sensorShortRangeLowerRange, "SFL");
        sensorRB = new Sensor(MachineConfig.sensorShortRangeUpperRange, MachineConfig.sensorShortRangeLowerRange, "SRB");
        sensorLL = new Sensor(MachineConfig.sensorLongRangeUpperRange, MachineConfig.sensorLongRangeLowerRange, "SLL");

    }

    public int getMachineX() {
        return currentX;
    }

    public int getMachineY() {
        return currentY;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setMachine(int x, int y) {
        currentX = x;
        currentY = y;
    }

    public FACING getMachineFacing() {
        return currentF;
    }
    public void setMachineFacing(FACING direction) {
        currentF = direction;
    }

    public boolean getSimulationMachine() {
        return simulationMachine;
    }

    public void reachedGoal() {
        if (currentX == Arena.goalX && currentY == Arena.goalY) {
            reachedGoal = true;
        }
    }

    public boolean getReachedGoal() {
        return reachedGoal;
    }

    public boolean machineSize(int x, int y) {
        return x >= currentX - 1 && x <= currentX + 1 && y >= currentY - 1 && y <= currentY + 1;
    }

    public boolean machineFacingCell(int x, int y) {
        switch (currentF) {
            case NORTH:
                return x == currentX + 1 && y == currentY;
            case EAST:
                return x == currentX && y == currentY + 1;
            case SOUTH:
                return x == currentX - 1 && y == currentY;
            case WEST:
                return x == currentX && y == currentY - 1;
        }
        return false;
    }

    public void move(MOVEMENT m, boolean sendMoveToAndroid) {
        if (simulationMachine) {
            // Emulate real movement by pausing execution.
            try {
                TimeUnit.MILLISECONDS.sleep(speed);
            } catch (InterruptedException e) {
                System.out.println("Something went wrong in Robot.move()!");
            }
        }
        switch (m) {
            case FORWARD:
                switch (currentF) {
                    case NORTH:
                        currentX++;
                        break;
                    case EAST:
                        currentY++;
                        break;
                    case SOUTH:
                        currentX--;
                        break;
                    case WEST:
                        currentY--;
                        break;
                }
                break;
            case RIGHT:
            case LEFT:
                currentF = findNewDirection(m);
                break;
            case CALIBRATE:
                break;
            default:
                System.out.println("Error in Robot.move()!");
                break;
        }

        if (!simulationMachine) {
            sendMovement(m, sendMoveToAndroid);
        } else {
            System.out.println("Move: " + MOVEMENT.print(m));
        }

        reachedGoal();
    }

    private FACING findNewDirection(MOVEMENT m) {
        if (m == MOVEMENT.RIGHT) {
            return FACING.getNext(currentF);
        } else {
            return FACING.getPrevious(currentF);
        }
    }

    private void sendMovement(MOVEMENT m, boolean sendMoveToAndroid) {
        CommMgr comm = CommMgr.getCommMgr();

        try {
            
            comm.sendMsg(MOVEMENT.print(m) + "", CommMgr.INSTRUCTION);
            Thread.sleep(300);
            comm.sendMsg(MOVEMENT.print(m) + "", CommMgr.AINSTRUCTION);
            Thread.sleep(300);
            comm.sendMsg(getMachineX() + "," + getMachineY() + "," + FACING.print(getMachineFacing()), CommMgr.MACHINE_POS);
            Thread.sleep(300);
        } catch (Exception e) {

        }
    }

    public void move(MOVEMENT m) {
        this.move(m, true);
    }

    public void moveForwardMultiple(int count) {
        if (count == 1) {
            move(MOVEMENT.FORWARD);
        } else {
            CommMgr comm = CommMgr.getCommMgr();
            if (count == 10) {
                comm.sendMsg("0", CommMgr.INSTRUCTION);
            } else if (count < 10) {
                comm.sendMsg(Integer.toString(count), CommMgr.INSTRUCTION);
                comm.sendMsg(Integer.toString(count), CommMgr.AINSTRUCTION);
                
            }

            switch (currentF) {
                case NORTH:
                    currentX += count;
                    break;
                case EAST:
                    currentY += count;
                    break;
                case SOUTH:
                    currentX += count;
                    break;
                case WEST:
                    currentY += count;
                    break;
            }

            comm.sendMsg(getMachineX() + "," + getMachineY() + "," + FACING.print(getMachineFacing()), CommMgr.MACHINE_POS);
        }
    }

    public FACING setSensorLeft() {
        switch (currentF) {
            case NORTH:
                return FACING.WEST;
            case SOUTH:
                return FACING.EAST;
            case EAST:
                return FACING.NORTH;
            case WEST:
                return FACING.SOUTH;
        }
        return null;
    }

    public FACING setSensorRight() {
        switch (currentF) {
            case NORTH:
                return FACING.EAST;
            case SOUTH:
                return FACING.WEST;
            case EAST:
                return FACING.SOUTH;
            case WEST:
                return FACING.NORTH;

        }
        return null;
    }

    public void setSensors() {
        switch (currentF) {
            case NORTH:
                sensorRF.setSensor(currentX + 1, currentY + 1, setSensorRight());
                sensorFR.setSensor(currentX + 1, currentY + 1, currentF);
                sensorFC.setSensor(currentX + 1, currentY, currentF);
                sensorFL.setSensor(currentX + 1, currentY - 1, currentF);
                sensorRB.setSensor(currentX - 1, currentY + 1, setSensorRight());
                sensorLL.setSensor(currentX, currentY + 1, setSensorLeft());
                break;
            case SOUTH:
                sensorRF.setSensor(currentX - 1, currentY - 1, setSensorRight());
                sensorFR.setSensor(currentX - 1, currentY - 1, currentF);
                sensorFC.setSensor(currentX - 1, currentY, currentF);
                sensorFL.setSensor(currentX - 1, currentY + 1, currentF);
                sensorRB.setSensor(currentX + 1, currentY - 1, setSensorRight());
                sensorLL.setSensor(currentX, currentY - 1, setSensorLeft());
                break;
            case EAST:
                sensorRF.setSensor(currentX - 1, currentY + 1, setSensorRight());
                sensorFR.setSensor(currentX - 1, currentY + 1, currentF);
                sensorFC.setSensor(currentX, currentY + 1, currentF);
                sensorFL.setSensor(currentX + 1, currentY + 1, currentF);
                sensorRB.setSensor(currentX - 1, currentY - 1, setSensorRight());
                sensorLL.setSensor(currentX - 1, currentY, setSensorLeft());
                break;
            case WEST:
                sensorRF.setSensor(currentX + 1, currentY - 1, setSensorRight());
                sensorFR.setSensor(currentX + 1, currentY - 1, currentF);
                sensorFC.setSensor(currentX, currentY - 1, currentF);
                sensorFL.setSensor(currentX - 1, currentY - 1, currentF);
                sensorRB.setSensor(currentX + 1, currentY + 1, setSensorRight());
                sensorLL.setSensor(currentX + 1, currentY, setSensorLeft());
                break;
        }
    }

    public int[] detect(Arena exploredArena, Arena realArena) {
        int[] result = new int[6];
        if (simulationMachine) {
            result[0] = sensorRF.virtualDetect(exploredArena, realArena);
            result[1] = sensorFR.virtualDetect(exploredArena, realArena);
            result[2] = sensorFC.virtualDetect(exploredArena, realArena);
            result[3] = sensorFL.virtualDetect(exploredArena, realArena);
            result[4] = sensorRB.virtualDetect(exploredArena, realArena);
            result[5] = sensorLL.virtualDetect(exploredArena, realArena);
        } else {
            CommMgr comm = CommMgr.getCommMgr();
            String msg = comm.recvMsg();
            String[] msgArr = msg.split(",");

            if (msgArr[0].equals(CommMgr.SENSOR_DATA)) {
                result[0] = (int) Math.abs((Math.floor(Double.parseDouble(msgArr[1])))+5f)/10;
                System.out.println(result[0]);
                result[1] = (int) Math.abs((Math.floor(Double.parseDouble(msgArr[2])))+5f)/10;
                result[2] = (int) Math.abs((Math.floor(Double.parseDouble(msgArr[3])))+5f)/10;
                result[3] = (int) Math.abs((Math.floor(Double.parseDouble(msgArr[4])))+5f)/10;
                result[4] = (int) Math.abs((Math.floor(Double.parseDouble(msgArr[5])))+5f)/10;
                System.out.println(result[4]);
                result[5] = (int) Math.abs((Math.floor(Double.parseDouble(msgArr[6])))+5f)/10;
            }

            sensorRF.realDetect(exploredArena, result[0]);
            sensorFR.realDetect(exploredArena, result[1]);
            sensorFC.realDetect(exploredArena, result[2]);
            sensorFL.realDetect(exploredArena, result[3]);
            sensorRB.realDetect(exploredArena, result[4]);
            sensorLL.realDetect(exploredArena, result[5]);

            String[] mapStrings = MapDescriptor.generateMapDescriptor(exploredArena);
            comm.sendMsg(mapStrings[0] + "," + mapStrings[1], CommMgr.MAP_STRINGS);
        }
        return result;
    }
}
