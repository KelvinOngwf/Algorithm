package algo;

import arena.*;
import machine.Machine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import machine.MachineConfig.*;
import main.ArenaUI;
import main.CommMgr;

/**
 *
 * @author Kelvin
 * @author Chris
 */
public class FastestPathAlgorithm {

    private ArrayList<Cell> toVisit;        // array of Cells to be visited
    private ArrayList<Cell> visited;        // array of visited Cells
    private HashMap<Cell, Cell> parents;    // HashMap of Child --> Parent
    private Cell current;                   // current Cell
    private Cell[] neighbors;               // array of neighbors of current Cell
    private FACING curFacing;               // current direction of robot
    private double[][] gCosts;              // array of real cost from START to [row][col] i.e. g(n)
    private Machine _machine;
    private Arena exploredMap;
    private final Arena realMap;
    private int loopCount;
    private boolean explorationMode;
    public int INFINITE_COST = 9999;
    public int MOVE_COST = 10;                         // cost of FORWARD, BACKWARD movement
    public int TURN_COST = 20;

    public FastestPathAlgorithm(Arena exploredMap, Machine machine) {
        this.realMap = null;
        initObject(exploredMap, machine);
    }

    public FastestPathAlgorithm(Arena exploredMap, Machine machine, Arena realMap) {
        this.realMap = realMap;
        this.explorationMode = true;
        initObject(exploredMap, machine);
    }

    /**
     * Initialise the FastestPathAlgo object.
     */
    private void initObject(Arena exploredMap, Machine bot) {
        this._machine = bot;
        this.exploredMap = exploredMap;
        this.toVisit = new ArrayList<>();
        this.visited = new ArrayList<>();
        this.parents = new HashMap<>();
        this.neighbors = new Cell[4];
        this.current = exploredMap.getCell(_machine.getMachineX(), _machine.getMachineY());
        this.curFacing = _machine.getMachineFacing();
        this.gCosts = new double[exploredMap.arenaX][exploredMap.arenaY];

        // Initialise gCosts array
        for (int i = 0; i < exploredMap.arenaX; i++) {
            for (int j = 0; j < exploredMap.arenaY; j++) {
                Cell cell = exploredMap.getCell(i, j);
                if (!canBeVisited(cell)) {
                    gCosts[i][j] = INFINITE_COST;
                } else {
                    gCosts[i][j] = 0;
                }
            }
        }
        toVisit.add(current);

        // Initialise starting point
        gCosts[_machine.getMachineX()][_machine.getMachineY()] = 0;
        this.loopCount = 0;
    }

    /**
     * Returns true if the cell can be visited.
     */
    private boolean canBeVisited(Cell c) {
        return c.getIsVisited() && !c.getIsObstacle() && !c.getVirtualWall();
    }

    /**
     * Returns the Cell inside toVisit with the minimum g(n) + h(n).
     */
    private Cell minimumCostCell(int goalRow, int getCol) {
        int size = toVisit.size();
        double minCost = INFINITE_COST;
        Cell result = null;

        for (int i = size - 1; i >= 0; i--) {
            double gCost = gCosts[(toVisit.get(i).getRow())][(toVisit.get(i).getCol())];
            double cost = gCost + costH(toVisit.get(i), goalRow, getCol);
            if (cost < minCost) {
                minCost = cost;
                result = toVisit.get(i);
            }
        }

        return result;
    }

    /**
     * Returns the heuristic cost i.e. h(n) from a given Cell to a given
     * [goalRow, goalCol] in the maze.
     */
    private double costH(Cell b, int goalRow, int goalCol) {
        // Heuristic: The no. of moves will be equal to the difference in the row and column values.
        double movementCost = (Math.abs(goalCol - b.getCol()) + Math.abs(goalRow - b.getRow())) * MOVE_COST;

        if (movementCost == 0) {
            return 0;
        }

        // Heuristic: If b is not in the same row or column, one turn will be needed.
        double turnCost = 0;
        if (goalCol - b.getCol() != 0 || goalRow - b.getRow() != 0) {
            turnCost = TURN_COST;
        }

        return movementCost + turnCost;
    }

    /**
     * Returns the target direction of the bot from [botR, botC] to target Cell.
     */
    private FACING getTargetDir(int x, int y, FACING machineFacing, Cell target) {
        if (y - target.getCol() > 0) {
            return FACING.WEST;
        } else if (target.getCol() - y > 0) {
            return FACING.EAST;
        } else {
            if (x - target.getRow() > 0) {
                return FACING.SOUTH;
            } else if (target.getRow() - x > 0) {
                return FACING.NORTH;
            } else {
                return machineFacing;
            }
        }
    }

    /**
     * Get the actual turning cost from one DIRECTION to another.
     */
    private double getTurnCost(FACING a, FACING b) {
        int numOfTurn = Math.abs(a.ordinal() - b.ordinal());
        if (numOfTurn > 2) {
            numOfTurn = numOfTurn % 2;
        }
        return (numOfTurn * TURN_COST);
    }

    /**
     * Calculate the actual cost of moving from Cell a to Cell b (assuming both
     * are neighbors).
     */
    private double costG(Cell a, Cell b, FACING aDir) {
        double moveCost = MOVE_COST; // one movement to neighbor

        double turnCost;
        FACING targetDir = getTargetDir(a.getRow(), a.getCol(), aDir, b);
        turnCost = getTurnCost(aDir, targetDir);

        return moveCost + turnCost;
    }

    /**
     * Find the fastest path from the robot's current position to [goalRow,
     * goalCol].
     */
    public String runFastestPath(int goalRow, int goalCol) {
        System.out.println("Calculating fastest path from (" + current.getRow() + ", " + current.getCol() + ") to goal (" + goalRow + ", " + goalCol + ")...");

        Stack<Cell> path;
        do {
            loopCount++;

            // Get cell with minimum cost from toVisit and assign it to current.
            current = minimumCostCell(goalRow, goalCol);

            // Point the robot in the direction of current from the previous cell.
            if (parents.containsKey(current)) {
                curFacing = getTargetDir(parents.get(current).getRow(), parents.get(current).getCol(), curFacing, current);
            }

            visited.add(current);       // add current to visited
            toVisit.remove(current);    // remove current from toVisit

            if (visited.contains(exploredMap.getCell(goalRow, goalCol))) {
                System.out.println("Goal visited. Path found!");
                path = getPath(goalRow, goalCol);
                printFastestPath(path);
                return executePath(path, goalRow, goalCol);
            }

            // Setup neighbors of current cell. [Top, Bottom, Left, Right].
            if (exploredMap.checkValidCell(current.getRow() + 1, current.getCol())) {
                neighbors[0] = exploredMap.getCell(current.getRow() + 1, current.getCol());
                if (!canBeVisited(neighbors[0])) {
                    neighbors[0] = null;
                }
            }
            if (exploredMap.checkValidCell(current.getRow() - 1, current.getCol())) {
                neighbors[1] = exploredMap.getCell(current.getRow() - 1, current.getCol());
                if (!canBeVisited(neighbors[1])) {
                    neighbors[1] = null;
                }
            }
            if (exploredMap.checkValidCell(current.getRow(), current.getCol() - 1)) {
                neighbors[2] = exploredMap.getCell(current.getRow(), current.getCol() - 1);
                if (!canBeVisited(neighbors[2])) {
                    neighbors[2] = null;
                }
            }
            if (exploredMap.checkValidCell(current.getRow(), current.getCol() + 1)) {
                neighbors[3] = exploredMap.getCell(current.getRow(), current.getCol() + 1);
                if (!canBeVisited(neighbors[3])) {
                    neighbors[3] = null;
                }
            }

            // Iterate through neighbors and update the g(n) values of each.
            for (int i = 0; i < 4; i++) {
                if (neighbors[i] != null) {
                    if (visited.contains(neighbors[i])) {
                        continue;
                    }

                    if (!(toVisit.contains(neighbors[i]))) {
                        parents.put(neighbors[i], current);
                        gCosts[neighbors[i].getRow()][neighbors[i].getCol()] = gCosts[current.getRow()][current.getCol()] + costG(current, neighbors[i], curFacing);
                        toVisit.add(neighbors[i]);
                    } else {
                        double currentGScore = gCosts[neighbors[i].getRow()][neighbors[i].getCol()];
                        double newGScore = gCosts[current.getRow()][current.getCol()] + costG(current, neighbors[i], curFacing);
                        if (newGScore < currentGScore) {
                            gCosts[neighbors[i].getRow()][neighbors[i].getCol()] = newGScore;
                            parents.put(neighbors[i], current);
                        }
                    }
                }
            }
        } while (!toVisit.isEmpty());

        System.out.println("Path not found!");
        return null;
    }

    /**
     * Generates path in reverse using the parents HashMap.
     */
    private Stack<Cell> getPath(int goalRow, int goalCol) {
        Stack<Cell> actualPath = new Stack<>();
        Cell temp = exploredMap.getCell(goalRow, goalCol);
        actualPath.push(exploredMap.getCell(_machine.getMachineX(), _machine.getMachineY()));

        while (true) {
            actualPath.push(temp);
            temp = parents.get(temp);
            if (temp == null) {
                break;
            }
        }
        return actualPath;
    }

    /**
     * Executes the fastest path and returns a StringBuilder object with the
     * path steps.
     */
    private String executePath(Stack<Cell> path, int goalRow, int goalCol) {
        StringBuilder outputString = new StringBuilder();

        Cell temp = path.pop();
        FACING targetDir;

        ArrayList<MOVEMENT> movements = new ArrayList<>();

        Machine tempBot = new Machine(_machine.getMachineX(), _machine.getMachineY(), _machine.getMachineFacing(), false);

        while ((tempBot.getMachineX() != goalRow) || (tempBot.getMachineY() != goalCol)) {
            if (tempBot.getMachineX() == temp.getRow() && tempBot.getMachineY() == temp.getCol()) {
                temp = path.pop();
            }

            targetDir = getTargetDir(tempBot.getMachineX(), tempBot.getMachineY(), tempBot.getMachineFacing(), temp);

            MOVEMENT m;
            if (tempBot.getMachineFacing() != targetDir) {
                m = getTargetMove(tempBot.getMachineFacing(), targetDir);
            } else {
                m = MOVEMENT.FORWARD;
            }

            System.out.println("Movement " + MOVEMENT.print(m) + " from (" + tempBot.getMachineX() + ", " + tempBot.getMachineY() + ") to (" + temp.getRow() + ", " + temp.getCol() + ")");

            tempBot.move(m,false);

            movements.add(m);
            outputString.append(MOVEMENT.print(m));
        }

        if (_machine.getSimulationMachine() || explorationMode) {
            for (MOVEMENT x : movements) {
                if (x == MOVEMENT.FORWARD) {
                    if (!canMoveForward()) {
                        System.out.println("Early termination of fastest path execution.");
                        return "T";
                    }
                }

                _machine.move(x);
                ArenaUI.repaintBtn();
                ArenaUI.paintMachine();

                // During exploration, use sensor data to update exploredMap.
                if (explorationMode) {
                    _machine.setSensors();
                    _machine.detect(this.exploredMap, this.realMap);
                    ArenaUI.repaintBtn();
                    ArenaUI.paintMachine();
                }
            }
        } else {
            int fCount = 0;
            for (MOVEMENT x : movements) {
                if (x == MOVEMENT.FORWARD) {
                    fCount++;
                    if (fCount == 10) {
 
                        _machine.moveForwardMultiple(fCount);
                        if (!_machine.getSimulationMachine()) {
                        CommMgr.getCommMgr().recvMsg();
                        }

                        fCount = 0;
                        ArenaUI.repaintBtn();
                        ArenaUI.paintMachine();
                    }
                } else if (x == MOVEMENT.RIGHT || x == MOVEMENT.LEFT) {
                    //if (fCount > 0) {
                    if (fCount > 0) {

                        _machine.moveForwardMultiple(fCount);
                        if (!_machine.getSimulationMachine()) {
                        CommMgr.getCommMgr().recvMsg();
                        }
                        fCount = 0;
                        ArenaUI.repaintBtn();
                        ArenaUI.paintMachine();
                    }

                    _machine.move(x);
                    if (!_machine.getSimulationMachine()) {
                    CommMgr.getCommMgr().recvMsg();
                    }
                    ArenaUI.repaintBtn();
                    ArenaUI.paintMachine();
                }
            }

            if (fCount > 0) {
                //String tempBuffer = CommMgr.getCommMgr().recvMsg();

                _machine.moveForwardMultiple(fCount);

                ArenaUI.repaintBtn();
                ArenaUI.paintMachine();

            }
        }

        System.out.println("\nMovements: " + outputString.toString());
        return outputString.toString();
    }

    /**
     * Returns true if the robot can move forward one cell with the current
     * heading.
     */
    private boolean canMoveForward() {
        int x = _machine.getMachineX();
        int y = _machine.getMachineY();

        switch (_machine.getMachineFacing()) {
            case NORTH:
                if (!exploredMap.isObstacle(x + 2, y - 1) && !exploredMap.isObstacle(x + 2, y) && !exploredMap.isObstacle(x + 2, y + 1)) {
                    return true;
                }
                break;
            case EAST:
                if (!exploredMap.isObstacle(x + 1, y + 2) && !exploredMap.isObstacle(x, y + 2) && !exploredMap.isObstacle(x - 1, y + 2)) {
                    return true;
                }
                break;
            case SOUTH:
                if (!exploredMap.isObstacle(x - 2, y - 1) && !exploredMap.isObstacle(x - 2, y) && !exploredMap.isObstacle(x - 2, y + 1)) {
                    return true;
                }
                break;
            case WEST:
                if (!exploredMap.isObstacle(x + 1, y - 2) && !exploredMap.isObstacle(x, y - 2) && !exploredMap.isObstacle(x - 1, y - 2)) {
                    return true;
                }
                break;
        }

        return false;
    }

    /**
     * Returns the movement to execute to get from one direction to another.
     */
    private MOVEMENT getTargetMove(FACING a, FACING b) {
        switch (a) {
            case NORTH:
                switch (b) {
                    case NORTH:
                        return MOVEMENT.ERROR;
                    case SOUTH:
                        return MOVEMENT.LEFT;
                    case WEST:
                        return MOVEMENT.LEFT;
                    case EAST:
                        return MOVEMENT.RIGHT;
                }
                break;
            case SOUTH:
                switch (b) {
                    case NORTH:
                        return MOVEMENT.LEFT;
                    case SOUTH:
                        return MOVEMENT.ERROR;
                    case WEST:
                        return MOVEMENT.RIGHT;
                    case EAST:
                        return MOVEMENT.LEFT;
                }
                break;
            case WEST:
                switch (b) {
                    case NORTH:
                        return MOVEMENT.RIGHT;
                    case SOUTH:
                        return MOVEMENT.LEFT;
                    case WEST:
                        return MOVEMENT.ERROR;
                    case EAST:
                        return MOVEMENT.LEFT;
                }
                break;
            case EAST:
                switch (b) {
                    case NORTH:
                        return MOVEMENT.LEFT;
                    case SOUTH:
                        return MOVEMENT.RIGHT;
                    case WEST:
                        return MOVEMENT.LEFT;
                    case EAST:
                        return MOVEMENT.ERROR;
                }
        }
        return MOVEMENT.ERROR;
    }

    /**
     * Prints the fastest path from the Stack object.
     */
    private void printFastestPath(Stack<Cell> path) {
        System.out.println("\nLooped " + loopCount + " times.");
        System.out.println("The number of steps is: " + (path.size() - 1) + "\n");

        Stack<Cell> pathForPrint = (Stack<Cell>) path.clone();
        Cell temp;
        System.out.println("Path:");
        while (!pathForPrint.isEmpty()) {
            temp = pathForPrint.pop();
            if (!pathForPrint.isEmpty()) {
                System.out.print("(" + temp.getRow() + ", " + temp.getCol() + ") --> ");
            } else {
                System.out.print("(" + temp.getRow() + ", " + temp.getCol() + ")");
            }
        }

        System.out.println("\n");
    }

    /**
     * Prints all the current g(n) values for the cells.
     */
    public void printGCosts() {
        for (int i = 0; i < exploredMap.arenaX; i++) {
            for (int j = 0; j < exploredMap.arenaY; j++) {
                System.out.print(gCosts[exploredMap.arenaX - 1 - i][j]);
                System.out.print(";");
            }
            System.out.println("\n");
        }
    }
}
