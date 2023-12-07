import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    private void solve() throws IOException {
        String line = in.readLine();
        while (line != null) {
            var st = new StringTokenizer(line, " ");
            var hand = st.nextToken();
            var bid = Integer.parseInt(st.nextToken());
            System.out.println(hand + " " + bid);
            line = in.readLine();
        }
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
