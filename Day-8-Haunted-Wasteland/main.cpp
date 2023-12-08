#include <iostream>
#include <fstream>
#include <sstream>
#include <unordered_map>
#include <vector>

using namespace std;
using Graph = unordered_map<string, pair<string, string>>;

pair<string , pair<string, string>> parse_line(const string& line) {
    string from, left, right, skip;
    stringstream ss(line);
    ss >> from >> skip >> left >> right;
    return make_pair(from, make_pair(left.substr(1, 3), right.substr(0, 3)));
}

size_t solve_part_1(const string& direction, const Graph& graph, const string& start, const string& end) {
    string current = start;
    size_t n = direction.length(), step_count = 0;
    do {
        current = direction[step_count % n] == 'L' ? graph.at(current).first : graph.at(current).second;
        step_count++;
    } while (current != end);
    return step_count;
}

long long int gcd(long long int a, long long int b) {
    return b == 0 ? a : gcd(b, a % b);
}

long long int lcm(long long int a, long long int b) {
    return a / gcd (a, b) * b;
}

long long int lcm(const vector<size_t>& vec) {
    auto result = static_cast<long long int>(vec[0]);
    for (size_t i = 1, ei = vec.size(); i < ei; i++) {
        result = lcm(result, static_cast<long long int>(vec[i]));
    }
    return result;
}

long long int solve_part_2(const string& direction, const Graph& graph) {
    vector<string> starts;
    for (const auto& [key, _] : graph) {
        if (key[2] == 'A') {
            starts.push_back(key);
        }
    }
    vector<size_t> step_counts;
    step_counts.reserve(starts.size());
    for (const auto& start : starts) {
        string current = start;
        size_t n = direction.length(), step_count = 0;
        do {
            current = direction[step_count % n] == 'L' ? graph.at(current).first : graph.at(current).second;
            step_count++;
            if (current[2] == 'Z') {
                step_counts.push_back(step_count);
                break;
            }
        } while (true);
    }
    return lcm(step_counts);
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
