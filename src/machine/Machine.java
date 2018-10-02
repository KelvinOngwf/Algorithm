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
/**
 *
 * @author Kelvin
 */
public class Machine {
    private int currentX;
    private int currentY;
    private FACING currentF;
    private boolean reachedGoal;
    private int speed;
    public static boolean simulationMachine;
    //right sensor
    public static Sensor sensorR;
    //front right sensor
    public static Sensor sensorFR;
    //front centre sensor
    public static Sensor sensorFC;
    //front left sensor
    public static Sensor sensorFL;
    //left sendor
    public static Sensor sensorL;
    //left Long range sensor
    public static Sensor sensorLL;
    
    
    
    
    public Machine(int x,int y,FACING facing,boolean simulationMachine){
        currentX=x;
        currentY=y;
        this.speed =MachineConfig.speed;
        currentF=facing;
        reachedGoal=false;
        this.simulationMachine=simulationMachine;
        sensorR = new Sensor(MachineConfig.sensorShortRangeUpperRange,MachineConfig.sensorShortRangeLowerRange,"SR");
        sensorFR = new Sensor(MachineConfig.sensorShortRangeUpperRange,MachineConfig.sensorShortRangeLowerRange,"SFR");
        sensorFC = new Sensor(MachineConfig.sensorShortRangeUpperRange,MachineConfig.sensorShortRangeLowerRange,"SFC");
        sensorFL = new Sensor(MachineConfig.sensorShortRangeUpperRange,MachineConfig.sensorShortRangeLowerRange,"SFL");
        sensorL = new Sensor(MachineConfig.sensorShortRangeUpperRange,MachineConfig.sensorShortRangeLowerRange,"SL");
        sensorLL = new Sensor(MachineConfig.sensorLongRangeUpperRange,MachineConfig.sensorLongRangeLowerRange,"SLL");
        
    }
    public int getMachineX(){
        return currentX;
    }
    public int getMachineY(){
        return currentY;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public void setMachine(int x, int y){
        currentX =x;
        currentY=y;
    }
    public FACING getMachineFacing(){
        return currentF;
    }
    public boolean getSimulationMachine(){
        return simulationMachine;
    }
    public void reachedGoal(){
        if(currentX==Arena.goalX && currentY==Arena.goalY)
            reachedGoal=true;
    }
    public boolean getReachedGoal(){
        return reachedGoal;
    }
    public boolean machineSize(int x,int y){
            return x >= currentX-1 && x<= currentX +1 && y>= currentY-1 && y <=currentY+1;
    }
    public boolean machineFacingCell(int x,int y){
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
            case BACKWARD:
                switch (currentF) {
                    case NORTH:
                        currentX--;
                        break;
                    case EAST:
                        currentY--;
                        break;
                    case SOUTH:
                        currentX++;
                        break;
                    case WEST:
                        currentY++;
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

        if (!simulationMachine) sendMovement(m, sendMoveToAndroid);
        else System.out.println("Move: " + MOVEMENT.print(m));

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
        //CommMgr comm = CommMgr.getCommMgr();
        //comm.sendMsg(MOVEMENT.print(m) + "", CommMgr.INSTRUCTIONS);
        if (m != MOVEMENT.CALIBRATE && sendMoveToAndroid) {
            //comm.sendMsg(this.getRobotPosRow() + "," + this.getRobotPosCol() + "," + DIRECTION.print(this.getRobotCurDir()), CommMgr.BOT_POS);
        }
    }

    /**
     * Overloaded method that calls this.move(MOVEMENT m, boolean sendMoveToAndroid = true).
     */
    public void move(MOVEMENT m) {
        this.move(m, true);
    }
    
    public void moveForwardMultiple(int count) {
        if (count == 1) {
            move(MOVEMENT.FORWARD);
        }/* 
        else {
            CommMgr comm = CommMgr.getCommMgr();
            if (count == 10) {
                comm.sendMsg("0", CommMgr.INSTRUCTIONS);
            } else if (count < 10) {
                comm.sendMsg(Integer.toString(count), CommMgr.INSTRUCTIONS);
            }*/

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

            //comm.sendMsg(this.getRobotPosRow() + "," + this.getRobotPosCol() + "," + DIRECTION.print(this.getRobotCurDir()), CommMgr.BOT_POS);   
    }

    
    public FACING setSensorLeft(){
        switch(currentF){
            case NORTH :
                return FACING.WEST;
            case SOUTH:
                return FACING.EAST;
            case EAST :
                return FACING.NORTH;
            case WEST :
                return FACING.SOUTH;
        }
        return null;
    }
    public FACING setSensorRight(){
        switch(currentF){
            case NORTH :
                return FACING.EAST;
            case SOUTH :
                return FACING.WEST;
            case EAST :
                return FACING.SOUTH;
            case WEST :
                return FACING.NORTH;

        }
        return null;
    }
    public void setSensors(){
        switch(currentF){
            case NORTH:
                sensorR.setSensor(currentX+1, currentY+1, setSensorRight());
                sensorFR.setSensor(currentX+1, currentY+1, currentF);
                sensorFC.setSensor(currentX+1, currentY, currentF);
                sensorFL.setSensor(currentX+1, currentY-1, currentF);
                sensorL.setSensor(currentX+1, currentY-1, setSensorLeft());
                sensorLL.setSensor(currentX, currentY-1, setSensorLeft());
                break;
            case SOUTH:
                sensorR.setSensor(currentX-1, currentY-1, setSensorRight());
                sensorFR.setSensor(currentX-1, currentY-1, currentF);
                sensorFC.setSensor(currentX-1, currentY, currentF);
                sensorFL.setSensor(currentX-1, currentY+1, currentF);
                sensorL.setSensor(currentX-1, currentY+1, setSensorLeft());
                sensorLL.setSensor(currentX, currentY+1, setSensorLeft());
                break;
            case EAST:
                sensorR.setSensor(currentX-1, currentY+1, setSensorRight());
                sensorFR.setSensor(currentX-1, currentY+1, currentF);
                sensorFC.setSensor(currentX, currentY+1, currentF);
                sensorFL.setSensor(currentX+1, currentY+1, currentF);
                sensorL.setSensor(currentX+1, currentY+1, setSensorLeft());
                sensorLL.setSensor(currentX+1, currentY, setSensorLeft());
                break;
            case WEST:
                sensorR.setSensor(currentX+1, currentY-1, setSensorRight());
                sensorFR.setSensor(currentX+1, currentY-1, currentF);
                sensorFC.setSensor(currentX, currentY-1, currentF);
                sensorFL.setSensor(currentX-1, currentY-1, currentF);
                sensorL.setSensor(currentX-1, currentY-1, setSensorLeft());
                sensorLL.setSensor(currentX-1, currentY, setSensorLeft());
                break;
        }
    }
    public int[] detect(Arena exploredArena, Arena realArena) {
        int[] result = new int[6];
        if (simulationMachine) {
            result[0] = sensorR.virtualDetect(exploredArena, realArena);
            result[1] = sensorFR.virtualDetect(exploredArena, realArena);
            result[2] = sensorFC.virtualDetect(exploredArena, realArena);
            result[3] = sensorFL.virtualDetect(exploredArena, realArena);
            result[4] = sensorL.virtualDetect(exploredArena, realArena);
            result[5] = sensorLL.virtualDetect(exploredArena, realArena);
        }
        else{
            //real detection not yet input
        }
        return result;
    }
}
