/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arena;

import robot.*;

/**
 *
 * @author Kelvin
 */
public class Arena{
    
    public static int arenaX = 20;
    public static int arenaY = 15;
    public static int goalX=2;
    public static int goalY=12;
    public int startX=17;
    public int startY=2;
    private Cell c[][];
    private Robot robot;
    
        
    public Arena(Robot robot){
        this.robot=robot;
        c = new Cell[arenaX][arenaY];
        for(int i=0;i<arenaX-1;i++){
            for(int j=0;j<arenaY-1;j++){
                c[i][j]=new Cell(i,j);
            }
        }
        defineVirtualWall();
    }
    public int getStartX(){
        return startX;
    }
    public int getStartY(){
        return startY;
    }
    public void defineVirtualWall(){
        for(int i=0;i<15;i++){
            c[0][i].setVirtualWall(true);
            c[19][i].setVirtualWall(true);
        }
        //define left & right wall
        for(int i=0;i<20;i++){
            c[i][0].setVirtualWall(true);
            c[i][14].setVirtualWall(true);
        }
    }
    public boolean isObstacle(int x, int y){
        return c[x][y].getIsObstacle();
    }
    public boolean isVirtualWall(int x,int y){
        return c[x][y].getVirtualWall();
    }
    public void setAllUnexplored(){
        for(int i=0; i<arenaX;i++)
            for(int j=0;j<arenaY;j++)
                if(goalArea(i,j)||startArea(i,j)){
                    c[i][j].setVisited(true);
                }
                else
                    c[i][j].setVisited(false);                
    }
    public boolean goalArea(int x,int y){
        if(x==2 && y==12)
            return true;
        return false;
    }
    public boolean startArea(int x,int y){
        if(x==17 && y==2)
            return true;
        return false;
    }
    public void placeObstacle(int x, int y,boolean obstacle){
        c[x][y].setObstacle(obstacle);
        //bottom 
        c[x+1][y-1].setVirtualWall(obstacle);
        c[x+1][y].setVirtualWall(obstacle);
        c[x+1][y+1].setVirtualWall(obstacle);
        //top
        c[x-1][y-1].setVirtualWall(obstacle);
        c[x-1][y].setVirtualWall(obstacle);
        c[x-1][y+1].setVirtualWall(obstacle);
        //left
        c[x][y-1].setVirtualWall(obstacle);
        //right
        c[x][y+1].setVirtualWall(obstacle);
        
    }
    public boolean checkValidCell(int x, int y){
        if(x >= 0 && x < arenaX && y >= 0 && y < arenaY)
            return true;
        return false;
    }
     public Cell getCell(int x, int y) {
        return c[x][y];
    }
    public String getMDFString(){
        String mdfString = "11";
        for(int i=19;i>=0;i--){
            for(int j=0;j<15;j++){
                mdfString += c[i][j];
            }
        }
        return mdfString+"11";    
    }
}
