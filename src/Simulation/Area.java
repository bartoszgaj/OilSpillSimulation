package Simulation;

import java.util.Random;

public class Area {

    private int size;
    private double windPower;
    private Direction windDirection;
    private double[] windDirectionsPower;
    private double temperature;
    private Cell[][] areaGrid;
    private int sourceX;
    private int sourceY;
    private double overallSourceLevel;
    private int iteration;


    /**
     * Tworzenie przestrzeni o zadanym rozmiarze
     */
    public Area(int size) {
        this.size = size;
        areaGrid = new Cell[this.size][this.size];
        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                areaGrid[x][y] = new Cell(x, y, Type.WATER);
            }
        }
        this.generateDefaultParamsAs0();
        this.generateWindDireciontsPower();
    }

    public void generateRandomSimulation() {
        generateRandomSpillSource();
        generateRandomWCurrent();
    }

    /**
     * Konstrukcja parametrow o wartosciach symbolu początkowego
     */
    public void generateDefaultParamsAs0() {
        this.sourceX = -1;
        this.sourceY = -1;
        this.windDirection = Direction.N;
        this.temperature = 20.0;
        this.overallSourceLevel = 1000;
        this.iteration = 0;
        this.windDirectionsPower = new double[8];
//        generateCoast();
    }

    private void setDefaultParams() {
        this.generateDefaultParamsAs0();
    }


    public void generateWindDireciontsPower() {
        int windDirection = this.windDirection.ordinal();
        this.windDirectionsPower[windDirection] = this.windPower;
        this.windDirectionsPower[(windDirection + 4) % 8] = -this.windPower;
        this.windDirectionsPower[(windDirection + 1) % 8] = this.windPower / 2;
        this.windDirectionsPower[(windDirection + 7) % 8] = this.windPower / 2;
        this.windDirectionsPower[(windDirection + 3) % 8] = -this.windPower / 2;
        this.windDirectionsPower[(windDirection + 5) % 8] = -this.windPower / 2;
        this.windDirectionsPower[(windDirection + 2) % 8] = 0;
        this.windDirectionsPower[(windDirection + 6) % 8] = 0;
    }


    public void generateArea() {

        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                areaGrid[x][y] = new Cell(x, y, Type.WATER);
            }
        }
    }

    /**
     * Generuje źródło wycieku w losowym miejscu na wodzie
     */
    public void generateRandomSpillSource() {
        Random generator = new Random();

        while (true) {
            int x = generator.nextInt(this.size);
            int y = generator.nextInt(this.size);

            if (areaGrid[x][y].getType() == Type.WATER) {
                this.generateSpillSource(x, y);
                break;
            }
        }
    }

    /**
     * Tworzy źródło wycieku w danym miejscu,
     * początek wycieku jako 100%
     *
     * @param x
     * @param y
     */
    public void generateSpillSource(int x, int y) {
        areaGrid[x][y].setType(Type.SOURCE);
        this.sourceX = x;
        this.sourceY = y;
        areaGrid[x][y].setOilLevel(100.0);
    }

    public void checkAndGenerateSpillSource(int x, int y) {
        if (areaGrid[x][y].getType() == Type.WATER) {
            this.generateSpillSource(x, y);
        } else {
            setDefaultParams();
        }
    }

    /**
     * Generuje losowy prąd w losowym miejscu
     */
    public void generateRandomWCurrent() {
        Random generator = new Random();
        int x = generator.nextInt(this.size - 20);

        this.generateWCurrent(x);
    }

    /**
     * Generuje prąd o losowej sile wokół podanego punktu
     *
     * @param xWCurrent
     */
    public void generateWCurrent(int xWCurrent) {
        Random generator = new Random();

        double randomWCurrentPower = generator.nextDouble();
        Direction currentWDirection = Direction.values()[generator.nextInt(8)];

        for (int x = xWCurrent - 30; x < xWCurrent + 30; x++)
            for (int y = 0; y < this.size; y++)
                areaGrid[x][y].setWCurrent(randomWCurrentPower, currentWDirection);
    }


    /**
     * Ustawia typ odpowiednich komorek na wybrzeze
     */
    public void generateCoast() {
        for (int x = 0; x < this.size; x++)
            for (int y = 0; y < this.size; y++)
                areaGrid[x][y].checkCoast(this);
    }

    public void updateSource() {
        if (overallSourceLevel <= 0) return;
        overallSourceLevel -= 100.0 - getSource().getOilLevel();
        getSource().setOilLevel(100.0 - (overallSourceLevel >= 0 ? 0 : overallSourceLevel));
    }


    public void updateOilLevelForCircle() {
        int minX = this.sourceX - this.iteration > 0 ? this.sourceX - this.iteration : 1;
        int maxX = this.sourceX + this.iteration < this.size ? this.sourceX + this.iteration : this.size - 1;
        int minY = this.sourceY - this.iteration > 0 ? sourceY - this.iteration : 1;
        int maxY = this.sourceY + this.iteration < this.size ? this.sourceY + this.iteration : this.size - 1;

        for (int x = minX; x < maxX; x++)
            for (int y = minY; y < maxY; y++)
                areaGrid[x][y].updateOilLevel();

        updateSource();
    }

    /**
     * Przelicza poziom oleju w każdej komórce
     */
    public void checkOilForCircle() {
        this.iteration++;

        int minX = this.sourceX - this.iteration > 0 ? this.sourceX - this.iteration : 1;
        int maxX = this.sourceX + this.iteration < this.size ? this.sourceX + this.iteration : this.size - 1;
        int minY = this.sourceY - this.iteration > 0 ? sourceY - this.iteration : 1;
        int maxY = this.sourceY + this.iteration < this.size ? this.sourceY + this.iteration : this.size - 1;

        for (int x = minX; x < maxX; x++)
            for (int y = minY; y < maxY; y++)
                areaGrid[x][y].generateNewOilLevel(this);

        this.updateOilLevelForCircle();
    }


    // need for Area setters..
    public void setWind(double windPower, Direction windDirection) {
        this.windDirection = windDirection;
        this.windPower = windPower;
        this.generateWindDireciontsPower();
    }

    // need for Area getters..
    public int getSize() {
        return this.size;
    }

    private Cell getSource() {
        return areaGrid[sourceX][sourceY];
    }

    public double getTemperature() {
        return this.temperature;
    }

    public Cell[][] getAreaGrid() {
        return this.areaGrid;
    }

    public double getWindPower() {
        return this.windPower;
    }

    public Cell getCell(int x, int y) {
        return areaGrid[x][y];
    }

    public double getWindPowerAtDirection(Direction windDirection) {
        return this.windDirectionsPower[windDirection.ordinal()];
    }
}
