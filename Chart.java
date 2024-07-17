import java.util.*;

public class Chart {
    Train train = new Train();
    void printChart(){
        HashMap<Character,int[]> masterList = train.getMasterList();

        char[][] chart = new char[train.getTotalSeats()][train.getTotalStations() + 1];
        char startRow = 'A', startCol = '1';
        char[] stations = new char[train.getTotalStations() + 1];

        for(int i = 1; i < stations.length; i++) stations[i] = startRow++;
        for(int i = 0; i < chart.length; i++) chart[i][0] = startCol++;

        int col = 1;
        for(Character k : masterList.keySet()){
            int row = 0;
            int[] cur = masterList.get(k);
            for(int i = 0; i < cur.length; i++){
                if(cur[i] > 0) chart[row++][col] = '*';
            }
            col++;
        }

        for(int i = 0; i < stations.length; i++) System.out.print(stations[i] + " ");
        System.out.println();
        for(int i = 0; i < chart.length; i++){
            for (int j = 0; j < chart[0].length; j++) System.out.print(chart[i][j] + " ");
            System.out.println();
        }
    }
}
