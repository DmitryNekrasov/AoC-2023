import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private int getScore(char[][] grid) {
        int n = grid.length;
        int result = 0;
        for (int i = 0; i < n; i++) {
            int stoneCount = 0;
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 'O') {
                    stoneCount++;
                }
            }
            result += stoneCount * (n - i);
        }
        return result;
    }

    private void move(char[][] grid) {
        int n = grid.length;
        for (int j = 0; j < n; j++) {
            int p0 = 0;
            while (p0 < n && grid[p0][j] != '.') {
                p0++;
            }
            int p1 = p0 + 1;
            while (p1 < n) {
                if (grid[p1][j] == '.') {
                    p1++;
                } else if (grid[p1][j] == 'O') {
                    grid[p0++][j] = 'O';
                    grid[p1++][j] = '.';
                } else {
                    p0 = p1;
                    while (p0 < n && grid[p0][j] != '.') {
                        p0++;
                    }
                    p1 = p0 + 1;
                }
            }
        }
    }

    private int solvePartOne(char[][] grid) {
        move(grid);
        return getScore(grid);
    }

    private char[][] asCharArray(List<String> gridList) {
        int n = gridList.size();
        char[][] grid = new char[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(gridList.get(i).toCharArray(), 0, grid[i], 0, n);
        }
        return grid;
    }

    private void printGrid(char[][] grid) {
        int n = grid.length;
        for (char[] chars : grid) {
            for (int j = 0; j < n; j++) {
                System.out.print(chars[j]);
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
        var ansPartOne = solvePartOne(grid);
        System.out.println(ansPartOne);
    }

    public static void main(String[] args) throws IOException {
        new Main().run();
    }

    private BufferedReader in;

    private void run() throws IOException {
        in = new BufferedReader(new InputStreamReader(new FileInputStream("input.txt")));
        solve();
        in.close();
    }
}
