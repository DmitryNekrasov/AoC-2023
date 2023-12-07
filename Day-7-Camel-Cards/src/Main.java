import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    private static class Hand implements Comparable<Hand> {
        private static final Map<Character, Character> mapping;

        static {
            final String priority = "23456789TJQKA";
            mapping = new HashMap<>();
            for (int i = 0; i < priority.length(); i++) {
                mapping.put(priority.charAt(i), (char) ('A' + i));
            }
        }

        private final String handStr;
        private final int bid;
        private final int type;

        public Hand(String handStr, int bid) {
            this.handStr = map(handStr);
            this.bid = bid;
            this.type = getType(handStr);
        }

        private static int getType(String hand) {
            var dict = new HashMap<Character, Integer>();
            for (int i = 0; i < hand.length(); i++) {
                char c = hand.charAt(i);
                dict.put(c, dict.getOrDefault(c, 0) + 1);
            }
            int type = 0, d = 10_000;
            for (int count : dict.values().stream().sorted(Comparator.reverseOrder()).toList()) {
                type += count * d;
                d /= 10;
            }
            return type;
        }

        private static String map(String hand) {
            var sb = new StringBuilder();
            for (int i = 0; i < hand.length(); i++) {
                sb.append(mapping.get(hand.charAt(i)));
            }
            return sb.toString();
        }

        @Override
        public String toString() {
            return handStr + ", " + bid + ", " + type;
        }

        @Override
        public int compareTo(Hand o) {
            if (type == o.type) {
                return handStr.compareTo(o.handStr);
            }
            return Integer.compare(type, o.type);
        }
    }

    int solvePartOne(List<Hand> hands) {
        Collections.sort(hands);
        int ans = 0;
        for (int i = 0; i < hands.size(); i++) {
            ans += (i + 1) * hands.get(i).bid;
        }
        return ans;
    }

    private void solve() throws IOException {
        String line = in.readLine();
        var hands = new ArrayList<Hand>();
        while (line != null) {
            var st = new StringTokenizer(line, " ");
            var hand = new Hand(st.nextToken(), Integer.parseInt(st.nextToken()));
            hands.add(hand);
            line = in.readLine();
        }
        int ansPartOne = solvePartOne(hands);
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
