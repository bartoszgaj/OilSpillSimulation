package Simulation;

import java.util.Random;

public class Cell {
    private int x;
    private int y;
    private int oilLevel;


    public Cell (int x, int y) {
        this.x = x;
        this.y = y;
        Random rand = new Random();
        this.oilLevel = rand.nextInt(100);
    }

    public int getOilLevel() {
        return oilLevel;
    }
}
