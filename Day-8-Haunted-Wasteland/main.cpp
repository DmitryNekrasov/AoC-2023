#include <iostream>
#include <fstream>
#include <sstream>
#include <unordered_map>

using namespace std;
using Graph = unordered_map<string, pair<string, string>>;

pair<string , pair<string, string>> parse_line(const string& line) {
    string from, left, right, skip;
    stringstream ss(line);
    ss >> from >> skip >> left >> right;
    return make_pair(from, make_pair(left.substr(1, 3), right.substr(0, 3)));
}

int solve_part_1(const string& direction, const Graph& graph, const string& start, const string& end) {
    string current = start;
    int n = direction.length(), step_count = 0;
    do {
        current = direction[step_count % n] == 'L' ? graph.at(current).first : graph.at(current).second;
        step_count++;
    } while (current != end);
    return step_count;
}

int solve_part_2(const string& direction, const Graph& graph) {
    return -1;
}

int main(int argc, char** argv) {
    ifstream ifs(argv[1]);
    string direction;
    getline(ifs, direction);
    string line;
    getline(ifs, line);
    Graph graph;
    while (getline(ifs, line)) {
        const auto [key, pair] = parse_line(line);
        graph[key] = pair;
    }
    cout << solve_part_1(direction, graph, "AAA", "ZZZ") << endl;
    cout << solve_part_2(direction, graph) << endl;
    return 0;
}
