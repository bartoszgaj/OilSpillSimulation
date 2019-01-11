package Simulation;

public class Statistics {

    public static int[] getCellsTypeInfo(Area area) {
        int coasts = 0;
        int lands = 0;
        int oils = 0;
        int sources = 0;
        int water = 0;

        int[]result = new int[5];

        for (int i = 0; i < area.getSize(); i++) {
            for (int j = 0; j < area.getSize(); j++) {
                switch (area.getCell(i, j).getType()) {
                    case WATER:
                        water++;
                        break;
                    case OIL:
                        oils++;
                        break;
                    case LAND:
                        lands++;
                        break;
                    case SOURCE:
                        sources++;
                        break;
                    case COAST:
                        coasts++;
                        break;
                    default:
                        break;
                }
            }
        }
        result[0] = oils;
        result[1] = sources;
        result[2] = water;
        result[3] = coasts;
        result[4] = lands;

        return result;
    }

    public static double getOverallOilLevel(Area area){
        double overAllOilLevel = 0;
        for (int i = 0; i < area.getSize(); i++) {
            for (int j = 0; j < area.getSize(); j++) {
                overAllOilLevel = overAllOilLevel + area.getCell(i,j).getOilLevel();
            }
        }
        return overAllOilLevel;
    }

}
