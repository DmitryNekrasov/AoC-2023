import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private void solvePartOne(char[][] grid) {
        printGrid(grid);
    }

    private char[][] asCharArray(List<String> gridList) {
        int n = gridList.size(), m = gridList.getFirst().length();
        char[][] grid = new char[n][m];
        for (int i = 0; i < n; i++) {
            System.arraycopy(gridList.get(i).toCharArray(), 0, grid[i], 0, m);
        }
        return grid;
    }

    private void printGrid(char[][] grid) {
        int n = grid.length, m = grid[0].length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
    }

    private void solve() throws IOException {
        String line = in.readLine();
        var gridList = new ArrayList<String>();
        while (line != null) {
            gridList.add(line);
            line = in.readLine();
        }
        var grid = asCharArray(gridList);
        solvePartOne(grid);
    }

    public static void main(String[] args) throws IOException {
        new Main().run();
    }

    private BufferedReader in;

    private void run() throws IOException {
        in = new BufferedReader(new InputStreamReader(new FileInputStream("input_simple.txt")));
        solve();
        in.close();
    }
}
