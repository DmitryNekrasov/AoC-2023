#include <algorithm>
#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>

using namespace std;

vector<vector<int>> to_pyramid(const vector<int> &values) {
    vector<vector<int>> result(1, values);
    while (true) {
        const auto &last = result.back();
        vector<int> current;
        for (size_t i = 0, ei = last.size() - 1; i < ei; i++) {
            current.push_back(last[i + 1] - last[i]);
        }
        result.push_back(current);
        if (all_of(begin(current), end(current), [](int value) { return value == 0; })) {
            break;
        }
    }
    return result;
}

pair<int, int> solve(const vector<int> &values) {
    auto pyramid = to_pyramid(values);
    int result_last = 0, result_first = 0, sg = 1;
    for (const auto& vec : pyramid) {
        result_last += vec.back();
        result_first += sg * vec.front();
        sg = -sg;
    }
    return make_pair(result_last, result_first);
}

int main(int argc, char **argv) {
    ifstream ifs(argv[1]);
    string line;
    int ans_part_one = 0, ans_part_two = 0;
    while (getline(ifs, line)) {
        stringstream ss(line);
        int value;
        vector<int> values;
        while (ss >> value) {
            values.push_back(value);
        }
        auto [result_last, result_first] = solve(values);
        ans_part_one += result_last;
        ans_part_two += result_first;
    }
    cout << ans_part_one << endl;
    cout << ans_part_two << endl;
    return 0;
}
