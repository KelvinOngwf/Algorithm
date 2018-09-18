/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robot;
import arena.Arena;
/**
 *
 * @author Kelvin
 */
public class Robot {
    private int currentX;
    private int currentY;
    private String currentF;
    private boolean reachedGoal;
    //right sensor
    private Sensor sensorR;
    //front right sensor
    private Sensor sensorFR;
    //front centre sensor
    private Sensor sensorFC;
    //front left sensor
    private Sensor sensorFL;
    //left sendor
    private Sensor sensorL;
    //left Long range sensor
    private Sensor sensorLL;
    private int sensorShortRangeUpperRange = 2;
    private int sensorShortRangeLowerRange = 1;
    private int sensorLongRangeUpperRange = 4;
    private int sensorLongRangeLowerRange = 3;
    private boolean simulationBot;
    
    
    public Robot(int x,int y,String facing,boolean simulationBot){
        currentX=x;
        currentY=y;
        currentF=facing;
        reachedGoal=false;
        this.simulationBot=simulationBot;
        sensorR = new Sensor(sensorShortRangeUpperRange,sensorShortRangeLowerRange,"SR");
        sensorFR = new Sensor(sensorShortRangeUpperRange,sensorShortRangeLowerRange,"SFR");
        sensorFC = new Sensor(sensorShortRangeUpperRange,sensorShortRangeLowerRange,"SFC");
        sensorFL = new Sensor(sensorShortRangeUpperRange,sensorShortRangeLowerRange,"SFL");
        sensorL = new Sensor(sensorShortRangeUpperRange,sensorShortRangeLowerRange,"SL");
        sensorLL = new Sensor(sensorLongRangeUpperRange,sensorLongRangeLowerRange,"SLL");
        
    }
    public int getRobotX(){
        return currentX;
    }
    public int getRobotY(){
        return currentY;
    }
    public String getRobotFacing(){
        return currentF;
    }
    public boolean getSimulationBot(){
        return simulationBot;
    }
    public void reachedGoal(){
        if(currentX==Arena.goalX && currentY==Arena.goalY)
            reachedGoal=true;
    }
    public boolean getReachedGoal(){
        return reachedGoal;
    }
    public void moveForward(){
        switch(currentF){
            //move up
            case "N" :
                currentX -=1;
                break;
            //move down
            case "S" :
                currentX +=1;
                break;
            //move right
            case "E" :
                currentY += 1;
                break;
            //move left
            case "W" :
                currentY -=1;
                break;
        }
    }
    public void moveBackward(){
        switch(currentF){
            //move up
            case "N" :
                currentX +=1;
                break;
            //move down
            case "S" :
                currentX -=1;
                break;
            //move right
            case "E" :
                currentY -= 1;
                break;
            //move left
            case "W" :
                currentY +=1;
                break;
        }
    }
    public void turnLeft(){
        //robot turn left
        switch(currentF){
            //move up
            case "N" :
                currentF="W";
                break;
            //move down
            case "S" :
                currentF="E";
                break;
            //move right
            case "E" :
                currentF="N";
                break;
            //move left
            case "W" :
                currentF="S";
                break;
        }
    }
    public void turnRight(){
        //robot turn right
        switch(currentF){
            //move up
            case "N" :
                currentF="E";
                break;
            //move down
            case "S" :
                currentF="W";
                break;
            //move right
            case "E" :
                currentF="S";
                break;
            //move left
            case "W" :
                currentF="N";
                break;
        }
    }
    public String setSensorLeft(){
        switch(currentF){
            case "N" :
                return "W";
            case "S" :
                return "E";
            case "E" :
                return "N";
            case "W" :
                return "S";
        }
        return "-1";
    }
    public String setSensorRight(){
        switch(currentF){
            case "N" :
                return "E";
            case "S" :
                return "W";
            case "E" :
                return "S";
            case "W" :
                return "N";

        }
        return "-1";
    }
    public void setSensors(){
        switch(currentF){
            case "N":
                sensorR.setSensor(currentX-1, currentY+1, setSensorRight());
                sensorFR.setSensor(currentX-1, currentY+1, currentF);
                sensorFC.setSensor(currentX-1, currentY, currentF);
                sensorFL.setSensor(currentX-1, currentY-1, currentF);
                sensorL.setSensor(currentX-1, currentY-1, setSensorLeft());
                sensorLL.setSensor(currentX, currentY-1, setSensorLeft());
                break;
            case "S":
                sensorR.setSensor(currentX+1, currentY-1, setSensorRight());
                sensorFR.setSensor(currentX+1, currentY-1, currentF);
                sensorFC.setSensor(currentX+1, currentY, currentF);
                sensorFL.setSensor(currentX+1, currentY+1, currentF);
                sensorL.setSensor(currentX+1, currentY+1, setSensorLeft());
                sensorLL.setSensor(currentX, currentY+1, setSensorLeft());
                break;
            case "E":
                sensorR.setSensor(currentX+1, currentY+1, setSensorRight());
                sensorFR.setSensor(currentX+1, currentY+1, currentF);
                sensorFC.setSensor(currentX, currentY+1, currentF);
                sensorFL.setSensor(currentX-1, currentY+1, currentF);
                sensorL.setSensor(currentX-1, currentY+1, setSensorLeft());
                sensorLL.setSensor(currentX-1, currentY, setSensorLeft());
                break;
            case "W":
                sensorR.setSensor(currentX-1, currentY-1, setSensorRight());
                sensorFR.setSensor(currentX-1, currentY-1, currentF);
                sensorFC.setSensor(currentX, currentY-1, currentF);
                sensorFL.setSensor(currentX+1, currentY-1, currentF);
                sensorL.setSensor(currentX+1, currentY-1, setSensorLeft());
                sensorLL.setSensor(currentX+1, currentY, setSensorLeft());
                break;
        }
    }
    public int[] detect(Arena exploredArena, Arena realArena) {
        int[] result = new int[6];
        if (simulationBot) {
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
