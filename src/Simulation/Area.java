package Simulation;

public class Area {
    private int size;
    private Cell[][] areaGrid;

    public Area (int size){
        this.size = size;
        areaGrid = new Cell[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                areaGrid[x][y] = new Cell(x,y,255);
            }
        }

    }

    public int getSize() {
        return size;
    }

    public Cell[][] getAreaGrid() {
        return areaGrid;
    }

    public Cell getCell(int x,int y) {
        return areaGrid[x][y];
    }
}
