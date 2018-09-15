/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm;

/**
 *
 * @author Kelvin
 */
public class Robot {
    int currentX;
    int currentY;
    String currentF;
    public Robot(int x,int y,String facing){
        currentX=x;
        currentY=y;
        currentF=facing;
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
    
    
}
