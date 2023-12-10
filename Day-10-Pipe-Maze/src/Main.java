import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class Main {
    private final static Map<Character, Character> mapping =
            Map.of('F', '┌', '7', '┐', 'J', '┘', 'L', '└', '|', '│', '-', '─', 'S', 'S');

    private record Point(int i, int j) {}

    Point nextPoints(char[][] maze, Point prevPoint, Point currentPoint) {
        int i = currentPoint.i, j = currentPoint.j;
        var candidates = switch (maze[i][j]) {
            case 'F' -> new Point[]{new Point(i, j + 1), new Point(i + 1, j)};
            case '7' -> new Point[]{new Point(i, j - 1), new Point(i + 1, j)};
            case 'J' -> new Point[]{new Point(i, j - 1), new Point(i - 1, j)};
            case 'L' -> new Point[]{new Point(i, j + 1), new Point(i - 1, j)};
            case '-' -> new Point[]{new Point(i, j - 1), new Point(i, j + 1)};
            case '|' -> new Point[]{new Point(i - 1, j), new Point(i + 1, j)};
            default -> throw new RuntimeException();
        };
        return !candidates[0].equals(prevPoint) ? candidates[0] : candidates[1];
    }

    Point getStartPoint(char[][] maze) {
        int n = maze.length, m = maze[0].length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (maze[i][j] == 'S') {
                    return new Point(i, j);
                }
            }
        }
        throw new RuntimeException();
    }

    private Point firstValidPoint(char[][] maze, Point currentPoint) {
        int i = currentPoint.i, j = currentPoint.j;
        int n = maze.length, m = maze[0].length;
        if (i > 0) {
            char candidate = maze[i - 1][j];
            if (candidate == 'F' || candidate == '7' || candidate == '|') {
                return new Point(i - 1, j);
            }
        }
        if (i < n - 1) {
            char candidate = maze[i + 1][j];
            if (candidate == 'L' || candidate == 'J' || candidate == '|') {
                return new Point(i + 1, j);
            }
        }
        if (j > 0) {
            char candidate = maze[i][j - 1];
            if (candidate == 'L' || candidate == 'F' || candidate == '-') {
                return new Point(i, j - 1);
            }
        }
        if (j < m - 1) {
            char candidate = maze[i][j + 1];
            if (candidate == '7' || candidate == 'J' || candidate == '-') {
                return new Point(i, j + 1);
            }
        }
        throw new RuntimeException();
    }

    private int solve(char[][] maze) {
        var startPoint = getStartPoint(maze);
        var prevPoint = startPoint;
        var currentPoint = firstValidPoint(maze, prevPoint);
        int stepCount = 1;
        do {
            stepCount++;
            var nextPoint = nextPoints(maze, prevPoint, currentPoint);
            prevPoint = currentPoint;
            currentPoint = nextPoint;
        } while (!currentPoint.equals(startPoint));
        return stepCount / 2;
    }

    char[][] toCharArrays(List<String> maze) {
        int n = maze.size(), m = maze.getFirst().length();
        char[][] result = new char[n][m];
        for (int i = 0; i < n; i++) {
            System.arraycopy(maze.get(i).toCharArray(), 0, result[i], 0, m);
        }
        return result;
    }

    private void printMaze(char[][] maze) {
        int n = maze.length, m = maze[0].length;
        for (int i = 0; i < n; i++) {
            final int row = i;
            System.out.println(
                    IntStream.range(0, m).mapToObj(j -> maze[row][j])
                            .map(c -> mapping.getOrDefault((char) c, ' '))
                            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            );
        }
    }

    private void solve() throws IOException {
        String line = in.readLine();
        var mazeAsList = new ArrayList<String>();
        while (line != null) {
            mazeAsList.add(line);
            line = in.readLine();
        }
        var maze = toCharArrays(mazeAsList);
        printMaze(maze);
        int ansPartOne = solve(maze); // 6613
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
