import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

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

    private void rotate(char[][] grid) {
        int n = grid.length;
        for (int k = 0, ek = n / 2; k < ek; k++) {
            for (int i = k; i < n - 1 - k; i++) {
                char buf = grid[k][i];
                grid[k][i] = grid[n - 1 - i][k];
                grid[n - 1 - i][k] = grid[n - 1 - k][n - 1 - i];
                grid[n - 1 - k][n - 1 - i] = grid[i][n - 1 - k];
                grid[i][n - 1 - k] = buf;
            }
        }
    }

    private void spinCycle(char[][] grid) {
        for (int i = 0; i < 4; i++) {
            move(grid);
            rotate(grid);
        }
    }

    private int solvePartOne(char[][] grid) {
        move(grid);
        return getScore(grid);
    }

    private static final long BASE = 269L;

    private long hash(char[][] grid) {
        long h = 0;
        for (char[] row : grid) {
            for (char c : row) {
                h = h * BASE + c;
            }
        }
        return h;
    }

    private Map<Long, Integer> countHashes(List<Long> hashes) {
        Map<Long, Integer> result = new HashMap<>();
        for (var hash : hashes) {
            result.put(hash, result.getOrDefault(hash, 0) + 1);
        }
        return result;
    }

    private int solvePartTwo(char[][] grid) {
        List<Long> hashes = new ArrayList<>();
        Map<Long, Integer> mapping = new HashMap<>();
        int index = 0, initialIndex = -1;
        while (true) {
            spinCycle(grid);
            var hash1 = hash(grid);
            hashes.add(hash1);
            mapping.put(hash1, getScore(grid));
            spinCycle(grid);
            var hash2 = hash(grid);
            hashes.add(hash2);
            mapping.put(hash2, getScore(grid));
            if (hashes.getLast().equals(hashes.get(index)) && initialIndex != -1) {
                break;
            }
            if (hashes.getLast().equals(hashes.get(index))) {
                initialIndex = index;
            }
            index++;
        }
        int loopLen = index - initialIndex;
        var ch = countHashes(hashes);
        int loopStart = -1;
        for (int i = 0; i < hashes.size(); i++) {
            if (ch.get(hashes.get(i)) > 1) {
                loopStart = i;
                break;
            }
        }
        return mapping.get(hashes.get(loopStart + (1_000_000_000 - loopStart - 1) % loopLen));
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
        for (char[] row : grid) {
            for (char c : row) {
                System.out.print(c);
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
        var ansPartTwo = solvePartTwo(grid);
        System.out.println(ansPartTwo);
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
