package Simulation;

import java.util.Random;

public class Area {

    private int size;
    private double windPower = 0;
    private Direction windDirection = Direction.N;
    private double[] windDirectionsPower = new double[8];
    private double temperature = 20;
    private Cell[][] areaGrid;
    private int sourceX = -1;
    private int sourceY = -1;
    private double overallSourceLevel = 255;
    private int iteration = 0;


    /**
     * Tworzenie przestrzeni o zadanym rozmiarze
     */
    public Area(int size) {
        this.size = size;
        areaGrid = new Cell[this.size][this.size];
//        this.generateArea();
        generateRandomArea();
        this.generateWindDireciontsPower();
    }

    public void generateRandomSimulation() {
        generateRandomSpillSource();
        generateRandomWCurrent();
    }


    public void setSimulationParameters(String windDirection, Double windSpeed, String waterDirection, Double waterSpeed){
        this.windDirection = Direction.stringToDirection(windDirection);
        this.windPower=windSpeed;
    }

    public void printSimulationParameters(){
        System.out.println("PARAMETRY SYMULACJI");
        System.out.println("Kierunek Wiatru = " + this.windDirection);
        System.out.println("Predkosc Wiatru = " + this.windPower);
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

    public void generateRandomArea () {
        Random generator = new Random();

        for (int x = 0; x < this.size; x++)
            for (int y = 0; y < this.size; y++) {
                areaGrid[x][y] = new Cell(x,y,Type.WATER);
                if (generator.nextInt(1000) > 512)
                    areaGrid[x][y].setType(Type.LAND);
                else
                    areaGrid[x][y].setType(Type.WATER);
            }

        for (int i = 0; i < 10; i++) {
            for (int x = 0; x < this.size; x++)
                for (int y = 0; y < this.size; y++)
                    if (areaGrid[x][y].getType() == Type.LAND) {
                        if (areaGrid[x][y].getNumberOfNeighbours8Directions(this, Type.WATER) >= 4)
                            areaGrid[x][y].setType(Type.WATER);
                    }
                    else if (areaGrid[x][y].getType() == Type.WATER)
                        if (areaGrid[x][y].getNumberOfNeighbours8Directions(this, Type.LAND) >= 4)
                             areaGrid[x][y].setType(Type.LAND);
        }

        generateCoast();
//        generateCurrent(250);
//        generateRandomWCurrent();
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


    /**
     * Generuje losowy prąd w losowym miejscu
     */
    public void generateRandomWCurrent() {
        Random generator = new Random();
        int x = generator.nextInt(this.size - 50);

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

        for (int x = xWCurrent; x < xWCurrent + 30; x++)
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


    public void upgradeOilExpansion() {

        for(int x=1; x < this.getSize()-1; x++){
            for(int y=1; y < this.getSize()-1; y++){
                areaGrid[x][y].updateOilLevel();
            }
        }

        updateSource();
    }

    /**
     * Przelicza poziom oleju w każdej komórce
     */
    public void checkOilForCircle() {

        for(int x=1; x < this.getSize() - 1; x++){
            for(int y=1; y < this.getSize() - 1; y++){
                areaGrid[x][y].generateNewOilLevel(this);
            }
        }

        this.upgradeOilExpansion();
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



    /**
     * Wyświetla różne informacje o obszarze w konsoli
     */
    public void displayAreaInfo () {
        System.out.println("Wind power & direction: " + this.windPower + "" + this.windDirection.toString());
        System.out.print("Wind power at directions: ");
        for (int i = 0; i < 8; i++) System.out.print(windDirectionsPower[i] + " ");
        System.out.println();
        System.out.println("Source X Y level: " + sourceX + " " + sourceY + " " + getCell(sourceX,sourceY).getOilLevel());
        System.out.println("Source Overall: " + overallSourceLevel);
//        System.out.println("Source Current: " + getCell(sourceX,sourceY).getCurrentPower() + getCell(sourceX,sourceY).getCurrentDir().toString());
        System.out.print("Source power at directions: ");
        for (int i = 0; i < 8; i++) System.out.print(getCell(sourceX,sourceY).getCurrentPowerAtDirection(Direction.values()[i]) + " ");
        System.out.println();
//        System.out.println("Cell 150 250 level: " + getCell(150,250).getOilLevel());
//        System.out.println("Cell 351 250 level: " + getCell(351,250).getOilLevel());
//        System.out.println("Cell 250 150 level: " + getCell(250,150).getOilLevel());
//        System.out.println("Cell 250 351 level: " + getCell(250,351).getOilLevel());
    }
}
