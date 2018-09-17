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
public class Sensor {
    private int upperRange;
    private int lowerRange;
    private int sensorX;
    private int sensorY;
    private String sensorF;
    private String sensorID;
    
    public Sensor(int upperRange, int lowerRange,String id){
        this.lowerRange = lowerRange;
        this.upperRange = upperRange;
        sensorID = id;
    }
    public void setSensor(int x,int y,String facing){
        sensorX=x;
        sensorY=y;
        sensorF=facing;
    }
    public int virtualDetect(Arena exploredArena,Arena realArena ){
        switch(sensorF){
            case "N" :
                return getSensorVal(exploredArena, realArena, 1, 0);
            //move down
            case "S" :
                return getSensorVal(exploredArena, realArena, -1, 0);
            //move right
            case "E" :
                return getSensorVal(exploredArena, realArena, 0, 1);
            //move left
            case "W" :
                return getSensorVal(exploredArena, realArena, 0, -1); 
        }
        return -1;
    }
    private int getSensorVal(Arena exploredArena, Arena realArena, int x, int y) {
        // Check if starting point is valid for sensors with lowerRange > 1.
        if (lowerRange > 1) {
            for (int i = 1; i < lowerRange; i++) {
                int row = sensorX + (x * i);
                int col = sensorY + (y * i);

                if (!exploredArena.checkValidCell(row, col)) 
                    return i;
                if (realArena.getCell(row, col).getIsObstacle()) 
                    return i;
            }
        }

        // Check if anything is detected by the sensor and return that value.
        for (int i = this.lowerRange; i <= this.upperRange; i++) {
            int row = sensorX + (x * i);
            int col = sensorY + (y * i);

            if (!exploredArena.checkValidCell(row, col)) 
                return i;

            exploredArena.getCell(row, col).setVisited(true);

            if (realArena.getCell(row, col).getIsObstacle()) {
                exploredArena.placeObstacle(row, col, true);
                return i;
            }
        }

        // Else, return -1.
        return -1;
    }
     public void realDetect(Arena exploredArena, int sensorVal) {
        switch (sensorF) {
            case "N":
                processSensorVal(exploredArena, sensorVal, 1, 0);
                break;
            case "S":
                processSensorVal(exploredArena, sensorVal, -1, 0);
                break;
            case "E":
                processSensorVal(exploredArena, sensorVal, 0, 1);
                break;
            case "W":
                processSensorVal(exploredArena, sensorVal, 0, -1);
                break;
        }
    }

    /**
     * Sets the correct cells to explored and/or obstacle according to the actual sensor value.
     */
    private void processSensorVal(Arena exploredArena, int sensorVal, int x, int y) {
        if (sensorVal == 0) return;  // return value for LR sensor if obstacle before lowerRange

        // If above fails, check if starting point is valid for sensors with lowerRange > 1.
        for (int i = 1; i < this.lowerRange; i++) {
            int row = sensorX + (x * i);
            int col = sensorY + (y * i);

            if (!exploredArena.checkValidCell(row, col)) return;
            if (exploredArena.getCell(row, col).getIsObstacle()) return;
        }

        // Update map according to sensor's value.
        for (int i = lowerRange; i <= upperRange; i++) {
            int row = sensorX + (x * i);
            int col = sensorY + (y * i);

            if (!exploredArena.checkValidCell(row, col)) continue;

            exploredArena.getCell(row, col).setVisited(true);

            if (sensorVal == i) {
                exploredArena.placeObstacle(row, col, true);
                break;
            }

            // Override previous obstacle value if front sensors detect no obstacle.
            if (exploredArena.getCell(row, col).getIsObstacle()) {
                if (sensorID.equals("SFL") || sensorID.equals("SFC") || sensorID.equals("SFR")) {
                    exploredArena.placeObstacle(row, col, false);
                } else {
                    break;
                }
            }
        }
    }
    
}
