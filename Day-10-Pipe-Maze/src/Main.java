import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    private final static Map<Character, Character> mapping =
            Map.of('F', '┌', '7', '┐', 'J', '┘', 'L', '└', '|', '│', '-', '─', 'S', 'S');

    private record Point(int i, int j) {}

    Point nextPoints(List<String> maze, Point prevPoint, Point currentPoint) {
        int i = currentPoint.i, j = currentPoint.j;
        var candidates = switch (maze.get(i).charAt(j)) {
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

    Point getStartPoint(List<String> maze) {
        int n = maze.size(), m = maze.getFirst().length();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (maze.get(i).charAt(j) == 'S') {
                    return new Point(i, j);
                }
            }
        }
        throw new RuntimeException();
    }

    private Point firstValidPoint(List<String> maze, Point currentPoint) {
        int i = currentPoint.i, j = currentPoint.j;
        int n = maze.size(), m = maze.getFirst().length();
        if (i > 0) {
            char candidate = maze.get(i - 1).charAt(j);
            if (candidate == 'F' || candidate == '7' || candidate == '|') {
                return new Point(i - 1, j);
            }
        }
        if (i < n - 1) {
            char candidate = maze.get(i + 1).charAt(j);
            if (candidate == 'L' || candidate == 'J' || candidate == '|') {
                return new Point(i + 1, j);
            }
        }
        if (j > 0) {
            char candidate = maze.get(i).charAt(j - 1);
            if (candidate == 'L' || candidate == 'F' || candidate == '-') {
                return new Point(i, j - 1);
            }
        }
        if (j < m - 1) {
            char candidate = maze.get(i).charAt(j + 1);
            if (candidate == '7' || candidate == 'J' || candidate == '-') {
                return new Point(i, j + 1);
            }
        }
        throw new RuntimeException();
    }

    private int solve(List<String> maze) {
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

    private void solve() throws IOException {
        String line = in.readLine();
        var maze = new ArrayList<String>();
        while (line != null) {
            maze.add(line);
            System.out.println(
                    line.chars()
                            .map(c -> mapping.getOrDefault((char) c, ' '))
                            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                            .toString()
            );
            line = in.readLine();
        }
        int ansPartOne = solve(maze);
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
