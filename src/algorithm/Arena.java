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
public class Arena {
    
    private int mapRow = 20;
    private int mapCol = 15;
    private Cell c[][];
    private Robot robot;
        
    public Arena(Robot robot){
        this.robot=robot;
        c = new Cell[mapRow][mapCol];
        for(int i=0;i<mapRow-1;i++){
            for(int j=0;j<mapCol-1;j++){
                c[i][j]=new Cell(i,j);
            }
        }
        defineVirtualWall();
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
        for(int i=0; i<mapRow;i++)
            for(int j=0;j<mapCol;j++)
                if(goalArea(i,j)||startArea(i,j)){
                    c[i][j].setVisited(true);
                }
                else
                    c[i][j].setVisited(false);                
    }
    private boolean goalArea(int x,int y){
        if(x==2 && y==12)
            return true;
        return false;
    }
    private boolean startArea(int x,int y){
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
