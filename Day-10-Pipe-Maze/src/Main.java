import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

public class Main {
    private final static Map<Character, Character> printMapping =
            Map.of('F', '┌', '7', '┐', 'J', '┘', 'L', '└', '|', '│', '-', '─', 'S', 'S');

    private final Map<Character, int[][]> extendedMapping =
            Map.of(
                    'F', new int[][]{
                            {0, 0, 0},
                            {0, 1, 1},
                            {0, 1, 0}
                    },
                    '7', new int[][]{
                            {0, 0, 0},
                            {1, 1, 0},
                            {0, 1, 0}
                    },
                    'J', new int[][]{
                            {0, 1, 0},
                            {1, 1, 0},
                            {0, 0, 0}
                    },
                    'L', new int[][]{
                            {0, 1, 0},
                            {0, 1, 1},
                            {0, 0, 0}
                    },
                    '|', new int[][]{
                            {0, 1, 0},
                            {0, 1, 0},
                            {0, 1, 0}
                    },
                    '-', new int[][]{
                            {0, 0, 0},
                            {1, 1, 1},
                            {0, 0, 0}
                    },
                    'S', new int[][]{
                            {0, 1, 0},
                            {1, 1, 1},
                            {0, 1, 0}
                    }
            );

    private record Point(int i, int j) {
    }

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

    private final Set<Point> walls = new HashSet<>();

    private int solvePartOne(char[][] maze) {
        var startPoint = getStartPoint(maze);
        var prevPoint = startPoint;
        var currentPoint = firstValidPoint(maze, prevPoint);
        walls.add(prevPoint);
        walls.add(currentPoint);
        int stepCount = 1;
        do {
            stepCount++;
            var nextPoint = nextPoints(maze, prevPoint, currentPoint);
            walls.add(nextPoint);
            prevPoint = currentPoint;
            currentPoint = nextPoint;
        } while (!currentPoint.equals(startPoint));
        return stepCount / 2;
    }

    private int[][] toExtendedMaze(char[][] maze) {
        int n = maze.length, m = maze[0].length;
        int[][] result = new int[n * 3][m * 3];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                char c = maze[i][j];
                if (c != '.') {
                    var mapTo = extendedMapping.get(c);
                    for (int k = 0; k < 3; k++) {
                        System.arraycopy(mapTo[k], 0, result[i * 3 + k], j * 3, 3);
                    }
                }
            }
        }
        return result;
    }

    private void printExtendedMaze(int[][] extendedMaze) {
        int n = extendedMaze.length, m = extendedMaze[0].length;
        for (int[] row : extendedMaze) {
            for (int j = 0; j < m; j++) {
                System.out.print(row[j] == 1 ? "# " : "  ");
            }
            System.out.println();
        }
    }

    private void bfs(int[][] extendedMaze) {
        int n = extendedMaze.length, m = extendedMaze[0].length;
        Queue<Point> queue = new LinkedList<>();
        queue.offer(new Point(0, 0));
        while (!queue.isEmpty()) {
            var p = queue.poll();
            int i = p.i, j = p.j;
            if (i < 0 || i >= n || j < 0 || j >= m || extendedMaze[i][j] == 1) continue;
            extendedMaze[i][j] = 1;
            queue.offer(new Point(i + 1, j));
            queue.offer(new Point(i - 1, j));
            queue.offer(new Point(i, j + 1));
            queue.offer(new Point(i, j - 1));
        }
    }

    private int countZeros(int[][] extendedMaze) {
        int n = extendedMaze.length, m = extendedMaze[0].length;
        int result = 0;
        for (int i = 1; i < n; i += 3) {
            for (int j = 1; j < m; j += 3) {
                if (extendedMaze[i][j] == 0) {
                    result++;
                }
            }
        }
        return result;
    }

    private int solvePartTwo(char[][] maze) {
        var extendedMaze = toExtendedMaze(maze);
        int n = maze.length, m = maze[0].length;
        bfs(extendedMaze);
        printExtendedMaze(extendedMaze);
        return countZeros(extendedMaze);
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
                            .map(c -> printMapping.getOrDefault(c, ' '))
                            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            );
        }
    }

    private void removeExtraWalls(char[][] maze) {
        int n = maze.length, m = maze[0].length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (!walls.contains(new Point(i, j))) {
                    maze[i][j] = '.';
                }
            }
        }
    }

    private void solvePartOne() throws IOException {
        String line = in.readLine();
        var mazeAsList = new ArrayList<String>();
        while (line != null) {
            mazeAsList.add(line);
            line = in.readLine();
        }
        var maze = toCharArrays(mazeAsList);
        int ansPartOne = solvePartOne(maze); // 6613
        removeExtraWalls(maze);
        printMaze(maze);
        System.out.println(ansPartOne);
        int ansPartTwo = solvePartTwo(maze);
        System.out.println(ansPartTwo);
    }

    public static void main(String[] args) throws IOException {
        new Main().run();
    }

    private BufferedReader in;

    private void run() throws IOException {
        in = new BufferedReader(new InputStreamReader(new FileInputStream("input.txt")));
        solvePartOne();
        in.close();
    }
}
