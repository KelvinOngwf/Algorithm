/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package machine;

/**
 *
 * @author Kelvin
 */
public class MachineConfig {
    
    public static int sensorShortRangeUpperRange = 2;
    public static int sensorShortRangeLowerRange = 1;
    public static int sensorLongRangeUpperRange = 4;
    public static int sensorLongRangeLowerRange = 3;
    
    public static int speed = 100;
    public enum FACING {
        NORTH, EAST, SOUTH, WEST, ERROR;

        public static FACING getNext(FACING curFacing) {
            return values()[(curFacing.ordinal() + 1) % values().length];
        }

        public static FACING getPrevious(FACING curFacing) {
            return values()[(curFacing.ordinal() + values().length - 1) % values().length];
        }

        public static char print(FACING d) {
            switch (d) {
                case NORTH:
                    return 'N';
                case EAST:
                    return 'E';
                case SOUTH:
                    return 'S';
                case WEST:
                    return 'W';
                default:
                    return 'X';
            }
        }
    }
}
