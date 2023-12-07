import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    private static class Hand {
        private static final Map<Character, Character> mapping;

        static {
            final String priority = "23456789TJQKA";
            mapping = new HashMap<>();
            for (int i = 0; i < priority.length(); i++) {
                mapping.put(priority.charAt(i), (char) ('A' + i));
            }
        }

        private final String handStrPart1;
        private final String handStrPart2;
        private final int bid;
        private final int typePart1;
        private final int typePart2;

        public Hand(String handStr, int bid) {
            mapping.put('J', 'J');
            this.handStrPart1 = map(handStr);
            mapping.put('J', (char) ('A' - 1));
            this.handStrPart2 = map(handStr);
            this.bid = bid;
            this.typePart1 = getTypePart1(handStr);
            this.typePart2 = getTypePart2(handStr);
        }

        private static int getTypePart1(String hand) {
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

        private int getTypePart2(String hand) {
            var dict = new HashMap<Character, Integer>();
            int jCount = 0;
            for (int i = 0; i < hand.length(); i++) {
                char c = hand.charAt(i);
                if (c == 'J') {
                    jCount++;
                } else {
                    dict.put(c, dict.getOrDefault(c, 0) + 1);
                }
            }
            var list = new ArrayList<>(dict.values().stream().sorted(Comparator.reverseOrder()).toList());
            if (list.isEmpty()) {
                list.add(jCount);
            } else {
                list.set(0, list.getFirst() + jCount);
            }
            int type = 0, d = 10_000;
            for (int count : list) {
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
            return "(s1=" + handStrPart1 + ", s2=" + handStrPart2 + ", t1=" + typePart1 + ", t2=" + typePart2 + ", b=" + bid + ")";
        }
    }

    int solvePartOne(List<Hand> hands) {
        hands.sort((h1, h2) -> {
            if (h1.typePart1 == h2.typePart1) {
                return h1.handStrPart1.compareTo(h2.handStrPart1);
            }
            return Integer.compare(h1.typePart1, h2.typePart1);
        });
        int ans = 0;
        for (int i = 0; i < hands.size(); i++) {
            ans += (i + 1) * hands.get(i).bid;
        }
        return ans;
    }

    int solvePartTwo(List<Hand> hands) {
        hands.sort((h1, h2) -> {
            if (h1.typePart2 == h2.typePart2) {
                return h1.handStrPart2.compareTo(h2.handStrPart2);
            }
            return Integer.compare(h1.typePart2, h2.typePart2);
        });
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
        int ansPartTwo = solvePartTwo(hands);
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
