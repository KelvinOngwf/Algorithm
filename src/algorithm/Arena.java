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
        
    public Arena(){
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
    public void placeObstacle(int x, int y){
        c[x][y].setObstacle(true);
        //bottom 
        c[x+1][y-1].setVirtualWall(true);
        c[x+1][y].setVirtualWall(true);
        c[x+1][y+1].setVirtualWall(true);
        //top
        c[x-1][y-1].setVirtualWall(true);
        c[x-1][y].setVirtualWall(true);
        c[x-1][y+1].setVirtualWall(true);
        //left
        c[x][y-1].setVirtualWall(true);
        //right
        c[x][y+1].setVirtualWall(true);
        
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
