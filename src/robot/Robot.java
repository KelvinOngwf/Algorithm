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
    
    
    public Robot(int x,int y,String facing){
        currentX=x;
        currentY=y;
        currentF=facing;
        reachedGoal=false;
        
        sensorR = new Sensor(sensorShortRangeUpperRange,sensorShortRangeLowerRange,currentX+1,currentY+1,setSensorRight(),"SR");
        sensorFR = new Sensor(sensorShortRangeUpperRange,sensorShortRangeLowerRange,currentX+1,currentY+1,currentF,"SFR");
        sensorFC = new Sensor(sensorShortRangeUpperRange,sensorShortRangeLowerRange,currentX,currentY+1,currentF,"SFC");
        sensorFL = new Sensor(sensorShortRangeUpperRange,sensorShortRangeLowerRange,currentX-1,currentY+1,currentF,"SFL");
        sensorL = new Sensor(sensorShortRangeUpperRange,sensorShortRangeLowerRange,currentX-1,currentY+1,setSensorLeft(),"SL");
        sensorLL = new Sensor(sensorLongRangeUpperRange,sensorLongRangeLowerRange,currentX-1,currentY,setSensorLeft(),"SLL");
        
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
    
    
}
