/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arena;

import machine.Machine;

/**
*
* @author Kelvin
* @author Chris
*/
public class Arena{
    
    public static int arenaX = 20;
    public static int arenaY = 15;
    public static int goalX=18;
    public static int goalY=13;
    public int startX=1;
    public int startY=1;
    private Cell c[][];
    private Machine robot;
    
        
    public Arena(Machine robot){
        this.robot=robot;
        c = new Cell[arenaX][arenaY];
        for(int i=(arenaX-1);i != -1;i--){
            for(int j=0;j<arenaY;j++){
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
    public void setAllExplored(){
        for(int i=0; i<arenaX;i++)
            for(int j=0;j<arenaY;j++)
                    c[i][j].setVisited(true);
    }
    public boolean goalArea(int x,int y){
            return x >= 17 && x<= 19 && y>= 12 && y <=14;
    }
    public boolean startArea(int x,int y){
            return x >= 0 && x <= 2 && y >= 0 && y <= 2;
    }
    public void placeObstacle(int x, int y,boolean obstacle){
        c[x][y].setObstacle(obstacle);

        if (obstacle && (startArea(x, y) || goalArea(x, y)))
            return;

        c[x][y].setObstacle(obstacle);

        if (x >= 1) {
            c[x - 1][y].setVirtualWall(obstacle);            // bottom cell

            if (y < arenaY - 1) {
                c[x - 1][y + 1].setVirtualWall(obstacle);    // bottom-right cell
            }
            if (y >= 1) {
                c[x - 1][y - 1].setVirtualWall(obstacle);    // bottom-left cell
            }
        }
        if (x < arenaX - 1) {
            c[x + 1][y].setVirtualWall(obstacle);            // top cell

            if (y < arenaY - 1) {
                c[x + 1][y + 1].setVirtualWall(obstacle);    // top-right cell
            }

            if (y >= 1) {
                c[x + 1][y - 1].setVirtualWall(obstacle);    // top-left cell
            }
        }
        if (y >= 1) {
            c[x][y - 1].setVirtualWall(obstacle);            // left cell
        }
        if (y < arenaY - 1) {
            c[x][y + 1].setVirtualWall(obstacle);            // right cell
        }
    }
    public boolean getIsObstacleOrWall(int row, int col) {
        return !checkValidCell(row, col) || getCell(row, col).getIsObstacle();
    }
    
    public boolean checkValidCell(int x, int y){
        if(x >= 0 && x < arenaX && y >= 0 && y < arenaY)
            return true;
        return false;
    }
     public Cell getCell(int x, int y) {
        return c[x][y];
    }
}
