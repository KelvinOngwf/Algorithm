/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algo;

import arena.*;
import robot.*;

/**
 *
 * @author Kelvin
 */
public class Exploration {
    private Arena exploredArena;
    private Arena realArena;
    private Robot robot;
    private int exploreLimit;
    private int timeLimit;
    private int areaExplored;
    private long startTime;
    private long endTime;
    
    public Exploration(Arena exploredArena,Arena realArena, Robot robot,int exploreLimit, int timeLimit){
        this.exploredArena= exploredArena;
        this.realArena=realArena;
        this.robot=robot;
        this.exploreLimit=exploreLimit;
        this.timeLimit=timeLimit;
    }
    public void startExploration(){
        if(!robot.getSimulationBot()){
            
        }
    }
    // need rename lol
    private void explorationLoop(int x, int y){
        while(areaExplored <= exploreLimit && System.currentTimeMillis()<= endTime){
            
        }
    }
    //private void 
    
}
