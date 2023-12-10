import java.io.*;
import java.util.Map;

public class Main {
    private final static Map<Character, Character> mapping =
            Map.of('F', '┌', '7', '┐', 'J', '┘', 'L', '└', '|', '│', '-', '─');

    private void solve() throws IOException {
        String line = in.readLine();
        while (line != null) {
            System.out.println(
                    line.chars()
                            .map(c -> mapping.getOrDefault((char) c, ' '))
                            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                            .toString()
            );

            line = in.readLine();
        }
    }

    public static void main(String[] args) throws IOException {
        new Main().run();
    }

    private BufferedReader in;

    private void run() throws IOException {
        in = new BufferedReader(new InputStreamReader(new FileInputStream("input_simple_2.txt")));
        solve();
        in.close();
    }
}
