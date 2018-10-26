/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package machine;

/**
 *
 * @author Kelvin
 * @author Chris
 */
public class MachineConfig {

    public static int sensorShortRangeUpperRange = 2;
    public static int sensorShortRangeLowerRange = 1;
    public static int sensorLongRangeUpperRange = 6;
    public static int sensorLongRangeLowerRange = 3;

    public static int speed = 100;

    public enum FACING {
        NORTH, EAST, SOUTH, WEST;

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

    public enum MOVEMENT {
        FORWARD, RIGHT, LEFT, CALIBRATE, ERROR;

        public static char print(MOVEMENT m) {
            switch (m) {
                case FORWARD:
                    return 'f';
                case RIGHT:
                    return 'r';
                case LEFT:
                    return 'l';
                case CALIBRATE:
                    return 'c';
                case ERROR:
                default:
                    return 'E';
            }
        }
    }
}
