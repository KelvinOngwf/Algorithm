/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arena;

/**
*
* @author Kelvin
* @author Chris
*/
public class Cell {
    private int row;
    private int col;
    private boolean isVirtualWall;
    private boolean isObstacle;
    private boolean isVisited;
    private boolean isWayPoint;
    
    public Cell(int x, int y){
        row = x;
        col = y;
        isVirtualWall=false;
        isObstacle =false;
        isVisited=false;
        isWayPoint=false;
    }
    public int getRow(){
        return row;
    }
    public int getCol(){
        return col;
    }
    public void setWayPoint(boolean wayPoint){
        isWayPoint = wayPoint;
    }
    public boolean getWayPoint(){
        return isWayPoint;
    }
    public void setObstacle(boolean setObstacle){
        isObstacle=setObstacle;
    }
    public boolean getIsObstacle(){
        return isObstacle;
    }
    public void setVirtualWall(boolean setVirtualWall ){
        isVirtualWall=setVirtualWall;
    }
    public boolean getVirtualWall(){
        return isVirtualWall;
    }
    public void setVisited(boolean setVisited){
        isVisited = setVisited;
    }
    public boolean getIsVisited(){
        return isVisited;
    }
    
}
