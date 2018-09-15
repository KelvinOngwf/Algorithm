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
    int currentX=17;
    int currentY=2;
    String currentF="E";
    public Robot(ArenaGen x){
        updateRobotPos(x);
    }
    public void moveForward(ArenaGen x){
        switch(currentF){
            //move up
            case "N" :
                currentX -=1;
                updateRobotPos(x);
                break;
            //move down
            case "S" :
                currentX +=1;
                updateRobotPos(x);
                break;
            //move right
            case "E" :
                currentY += 1;
                updateRobotPos(x);
                break;
            //move left
            case "W" :
                currentY -=1;
                updateRobotPos(x);
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
    //update Arena where the robot is
    public void updateRobotPos(ArenaGen x){
        exploredCell(x);
        x.updateArena();
        //middle
        x.map[currentX][currentY]= "R";
        // top middle
        x.map[currentX+1][currentY]= "R";
        // bottom middle
        x.map[currentX-1][currentY]= "R";
        //left
        x.map[currentX][currentY-1]="R";
        //top left
        x.map[currentX+1][currentY-1]="R";
        //bottom left
        x.map[currentX-1][currentY-1]="R";
        //right
        x.map[currentX][currentY+1]="R";
        //top right
        x.map[currentX+1][currentY+1]="R";
        //bottom right
        x.map[currentX-1][currentY+1]="R";
        
    }
    public void exploredCell(ArenaGen x){
        switch(currentF){
            //move up
            case "N" :
                for(int i=0;i<3;i++){
                    x.map[currentX+2][currentY]="1";
                    x.map[currentX+2][currentY+1]="1";
                    x.map[currentX+2][currentY-1]="1";
                }
                break;
            //move down
            case "S" :
                for(int i=0;i<3;i++){
                    x.map[currentX-2][currentY]="1";
                    x.map[currentX-2][currentY+1]="1";
                    x.map[currentX-2][currentY-1]="1";
                }
                break;
            //move right
            case "E" :
                for(int i=0;i<3;i++){
                    x.map[currentX][currentY-2]="1";
                    x.map[currentX+1][currentY-2]="1";
                    x.map[currentX-1][currentY-2]="1";
                }
                break;
            //move left
            case "W" :
                for(int i=0;i<3;i++){
                    x.map[currentX][currentY+2]="1";
                    x.map[currentX+1][currentY+2]="1";
                    x.map[currentX-1][currentY+2]="1";
                }
                break;
        }
    }
    public void explorationMode(ArenaGen x){
        while(!x.arenaExplored())
        {
            switch(currentF){
            //move up
            case "N" :
                while(nFaceNoObs(x)){
                    moveForward(x);
                    x.printArena();
                }
                turnLeft();
                break;
            //move down
            case "S" :
                while(sFaceNoObs(x)){
                    moveForward(x);
                    x.printArena();
                }  
                turnLeft();
                break;
            //move right
            case "E" :
                while(eFaceNoObs(x)){
                    moveForward(x);
                    x.printArena();
                }
                turnLeft();
                break;
            //move left
            case "W" :
                while(wFaceNoObs(x)){
                    moveForward(x);
                    x.printArena();
                }
                turnLeft();
                break;
        }
        }
    }
    public boolean nFaceNoObs(ArenaGen x){
        for(int i=-1;i<2;i++)
            if(x.map[currentX-2][currentY-i]=="X" ||x.map[currentX-2][currentY-i]=="-")
                return false;
        return true;
    }
    public boolean sFaceNoObs(ArenaGen x){
        for(int i=-1;i<2;i++)
            if(x.map[currentX+2][currentY-i]=="X" ||x.map[currentX+2][currentY-i]=="-")
                return false;
        return true;
    } 
    public boolean eFaceNoObs(ArenaGen x){
        for(int i=-1;i<2;i++)
            if(x.map[currentX-i][currentY+2]=="X" ||x.map[currentX-i][currentY+2]=="|")
                return false;
        return true;
    } 
    public boolean wFaceNoObs(ArenaGen x){
        for(int i=-1;i<2;i++)
            if(x.map[currentX-i][currentY-2]=="X" ||x.map[currentX-i][currentY-2]=="|")
                return false;
        return true;
    } 
}
