package Simulation;

public class Cell {
    // Const propagation of oil
    public static final double OIL_B_ADJ = 0.14;
    public static final double OIL_B_DIA = 0.18 * OIL_B_ADJ;

    private int x;
    private int y;

    private Type type = Type.WATER;

    private double oilLevel;
    private double nextOilLevel;

    private Direction wCurrentDirection = Direction.N;
    private double wCurrentPower = 0;
    private double[] wCurrentDirectionsPowers = new double[8];

    public void setOilLevel(double oilLevel) {
        this.oilLevel = oilLevel;
    }

    public double getOilLevel() {
        return oilLevel;
    }

    public void setwCurrentDirection(Direction wCurrentDirection) {
        this.wCurrentDirection = wCurrentDirection;
    }

    public double getwCurrentPower() {
        return wCurrentPower;
    }

    public void setwCurrentPower(double wCurrentPower) {
        this.wCurrentPower = wCurrentPower;
    }

    public double[] getwCurrentDirectionsPowers() {
        return wCurrentDirectionsPowers;
    }

    public void setwCurrentDirectionsPowers(double[] wCurrentDirectionsPowers) {
        this.wCurrentDirectionsPowers = wCurrentDirectionsPowers;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setWCurrent (double wPower, Direction direction) {
        this.wCurrentPower = wPower;
        this.wCurrentDirection = direction;
        this.generateWCurrentDirectionsPower();
    }

    public double getCurrentPowerAtDirection(Direction direction){
        return wCurrentDirectionsPowers[direction.ordinal()];
    }

    public Cell(int x, int y, Type type, double oilLevel, double nextOilLevel, Direction wCurrentDirection, double wCurrentPower, double[] wCurrentDirectionsPowers) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.oilLevel = oilLevel;
        this.nextOilLevel = nextOilLevel;
        this.wCurrentDirection = wCurrentDirection;
        this.wCurrentPower = wCurrentPower;
        this.wCurrentDirectionsPowers = wCurrentDirectionsPowers;
    }

    public Cell() {
    }

    public Cell(int x, int y, Type type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void updateOilLevel() {
        //updating oil level
        if(this.nextOilLevel > 0){
            this.oilLevel = this.nextOilLevel;
        }
        else{
            this.oilLevel = 0;
        }

        //updating cell type
        if(this.type == Type.WATER && this.oilLevel > 0){
            this.type = Type.OIL;
        }
        else if(this.type == Type.OIL && this.oilLevel == 0){
            this.type = Type.WATER;
        }
    }

    /**
     * Generates different watter current values for each direction
     */
    private void generateWCurrentDirectionsPower() {
        //TODO generate ENUM methods for gettin directions
        int direction = this.wCurrentDirection.ordinal();
        this.wCurrentDirectionsPowers[direction] = this.wCurrentPower;
        //opposite direction
        this.wCurrentDirectionsPowers[(direction + 4) % 8] = -this.wCurrentPower;
        //right neighbour
        this.wCurrentDirectionsPowers[(direction + 1) % 8] = this.wCurrentPower / 2;
        //left neighbour
        this.wCurrentDirectionsPowers[(direction + 7) % 8] = this.wCurrentPower / 2;
        //right opposite neighbour
        this.wCurrentDirectionsPowers[(direction + 3) % 8] = -this.wCurrentPower / 2;
        //left opposite neighbour
        this.wCurrentDirectionsPowers[(direction + 5) % 8] = -this.wCurrentPower / 2;
        //right direction
        this.wCurrentDirectionsPowers[(direction + 2) % 8] = 0;
        //left direction
        this.wCurrentDirectionsPowers[(direction + 6) % 8] = 0;
    }

    /**
     * Checks whether a cell is a border
     * @param area
     */
    public Boolean isBorder(Area area){
        return (this.x == 0 || this.x == area.getSize() ||
                this.y == 0 || this.y == area.getSize());
    }

    /**
     * Checks whether a cell is a part of a coast
     * @param area
     */
    public void checkCoast (Area area) {
        if (this.type == Type.LAND && this.getNumberOfNeighbours8Directions(area, Type.WATER) > 0)
            this.type = Type.COAST;
    }

    /**
     * Checks how many cells in N, E, S, W directions hass the same type as given
     * @param area
     * @param cellType - type of a cell we want to find neighbours for
     * @return number of neighbours in N, E, S, W directions
     */
    public int getNumberOfNeighbours4Directions(Area area, Type cellType){
        int neighbours = 0;
        if (y > 0 && area.getCell(x,y-1).getType() == type) // NORTH
            neighbours++;
        if (x < area.getSize()-1 && area.getCell(x+1,y).getType() == type) // EAST
            neighbours++;
        if (y < area.getSize()-1 && area.getCell(x,y+1).getType() == type) // SOUTH
            neighbours++;
        if (this.x > 0 && area.getCell(x-1,y).getType() == type) // WEST
            neighbours++;

        return neighbours;
    }

    /**
     * Checks how many cells in N, NE, E, SE, S, SW, W, NW directions hass the same type as given
     * @param area
     * @param cellType - type of a cell we want to find neighbours for
     * @return number of neighbours in N, NE, E, SE, S, SW, W, NW directions
     */
    public int getNumberOfNeighbours8Directions(Area area, Type cellType){
        int neighbours = 0;

        neighbours += this.getNumberOfNeighbours4Directions(area, cellType);

        if (x > 0 && y > 0 && area.getCell(x-1,y-1).getType() == type) // NORTH WEST
            neighbours++;
        if (x > 0 && y < area.getSize()-1 && area.getCell(x-1,y+1).getType() == type) // SOUTH WEST
            neighbours++;
        if (x < area.getSize()-1 && y > 0 && area.getCell(x+1,y-1).getType() == type) // NORTH EAST
            neighbours++;
        if (x < area.getSize()-1 &&  y < area.getSize()-1 && area.getCell(x+1,y+1).getType() == type) // SOUTH EAST
            neighbours++;

        return neighbours;
    }



    /**
     * Generates new oil level based on surrounding cells
     * @param area
     */
    public void generateNewOilLevel(Area area){
        if(this.isBorder(area) ||
        this.type == Type.LAND ||
        this.type == Type.COAST){
            return;
        }
        // no oil in surroundings
        if(this.getNumberOfNeighbours8Directions(area, Type.OIL) == 0 &&
        this.getNumberOfNeighbours8Directions(area, Type.SOURCE) == 0){
            return;
        }

        double nextOilLevel = this.oilLevel;

        double oilChange = 0;
        Cell cell;
        Type cellType;

        // propagation of oil with cells sticked on sides
        cell = area.getCell(x-1, y);
        cellType = cell.getType();
        if (cellType == Type.WATER || cellType == Type.OIL || cellType == Type.SOURCE)
            oilChange += (1+area.getWindPowerAtDirection(Direction.S)+this.getCurrentPowerAtDirection(Direction.S)) * cell.getOilLevel() - this.oilLevel;

        cell = area.getCell(x+1,y);
        cellType = cell.getType();
        if (cellType == Type.WATER || cellType == Type.OIL || cellType == Type.SOURCE)
            oilChange += (1+area.getWindPowerAtDirection(Direction.N)+this.getCurrentPowerAtDirection(Direction.N)) * cell.getOilLevel() - this.oilLevel;

        cell = area.getCell(x,y-1);
        cellType = cell.getType();
        if (cellType == Type.WATER || cellType == Type.OIL || cellType == Type.SOURCE)
            oilChange += (1+area.getWindPowerAtDirection(Direction.E)+this.getCurrentPowerAtDirection(Direction.E)) * cell.getOilLevel() - this.oilLevel;

        cell = area.getCell(x,y+1);
        cellType = cell.getType();
        if (cellType == Type.WATER || cellType == Type.OIL || cellType == Type.SOURCE)
            oilChange += (1+area.getWindPowerAtDirection(Direction.W)+this.getCurrentPowerAtDirection(Direction.W)) * cell.getOilLevel() - this.oilLevel;

        nextOilLevel += oilChange * this.OIL_B_ADJ;
        oilChange = 0;

        // propagation of oil with cells sticked on corners
        cell = area.getCell(x-1,y-1);
        cellType = cell.getType();
        if (cellType == Type.WATER || cellType == Type.OIL || cellType == Type.SOURCE)
            oilChange += (1+area.getWindPowerAtDirection(Direction.SW)+this.getCurrentPowerAtDirection(Direction.SW)) * cell.getOilLevel() - this.oilLevel;

        cell = area.getCell(x+1,y-1);
        cellType = cell.getType();
        if (cellType == Type.WATER || cellType == Type.OIL || cellType == Type.SOURCE)
            oilChange += (1+area.getWindPowerAtDirection(Direction.NW)+this.getCurrentPowerAtDirection(Direction.NW)) * cell.getOilLevel() - this.oilLevel;

        cell = area.getCell(x-1,y+1);
        cellType = cell.getType();
        if (cellType == Type.WATER || cellType == Type.OIL || cellType == Type.SOURCE)
            oilChange += (1+area.getWindPowerAtDirection(Direction.SE)+this.getCurrentPowerAtDirection(Direction.SE)) * cell.getOilLevel() - this.oilLevel;

        cell = area.getCell(x+1,y+1);
        cellType = cell.getType();
        if (cellType == Type.WATER || cellType == Type.OIL || cellType == Type.SOURCE)
            oilChange += (1+area.getWindPowerAtDirection(Direction.NE)+this.getCurrentPowerAtDirection(Direction.NE)) * cell.getOilLevel() - this.oilLevel;

        nextOilLevel += oilChange * this.OIL_B_DIA;

        //setting the next oil level
        this.nextOilLevel = nextOilLevel;

    }

}
