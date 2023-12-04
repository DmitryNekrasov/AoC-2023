import java.io.*;
import java.util.*;

public class Main {
    private Set<Integer> stringNumbersToSet(String str) {
        return Set.copyOf(stringNumbersToList(str));
    }
    
    private List<Integer> stringNumbersToList(String str) {
        List<Integer> numbers = new ArrayList<>();
        var st = new StringTokenizer(str, " ");
        while (st.hasMoreTokens()) {
            numbers.add(Integer.parseInt(st.nextToken()));
        }
        return numbers;
    }

    private int winningNumbersCount(Set<Integer> haystack, List<Integer> needle) {
        int result = 0;
        for (var num : needle) {
            if (haystack.contains(num)) {
                result++;
            }
        }
        return result;
    }

    private int pow2(int n) {
        if (n < 0) return 0;
        int result = 1;
        for (int i = 0; i < n; i++) {
            result <<= 1;
        }
        return result;
    }

    private int solveLine(String line) {
        var st = new StringTokenizer(line, ":|");
        st.nextToken();
        var winningNumbers = stringNumbersToSet(st.nextToken());
        var myNumbers = stringNumbersToList(st.nextToken());
        int winningCount = winningNumbersCount(winningNumbers, myNumbers);
        return pow2(winningCount - 1);
    }
    private void solve() throws IOException {
        String line = in.readLine();
        int ans = 0;
        while (line != null) {
            ans += solveLine(line);
            line = in.readLine();
        }
        System.out.println(ans);
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
