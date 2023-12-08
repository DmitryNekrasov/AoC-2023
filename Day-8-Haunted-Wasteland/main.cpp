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

int solve(const string& direction, const Graph& graph) {
    string current = "AAA";
    int direction_index = 0, n = direction.length(), step_count = 0;
    while (current != "ZZZ") {
        current = direction[direction_index % n] == 'L' ? graph.at(current).first : graph.at(current).second;
        direction_index++;
        step_count++;
    }
    return step_count;
}

int main(int argc, char** argv) {
    ifstream ifs(argv[1]);
    string direction;
    getline(ifs, direction);
    string line;
    getline(ifs, line);
    Graph graph;
    while (getline(ifs, line)) {
        auto res = parse_line(line);
        graph[res.first] = res.second;
    }
    int ans_part_one = solve(direction, graph);
    cout << ans_part_one << endl;
    return 0;
}
