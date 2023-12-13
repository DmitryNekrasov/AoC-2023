#include <iostream>
#include <fstream>
#include <vector>
#include <unordered_set>

using namespace std;

bool is_palindrome(const vector<char>& row, int index) {
    for (int left = index, right = index + 1; left >= 0 && right < row.size(); left--, right++) {
        if (row[left] != row[right]) {
            return false;
        }
    }
    return true;
}

unordered_set<int> get_palindrome_indices(const vector<char>& row) {
    unordered_set<int> result;
    for (int i = 0; i < row.size() - 1; i++) {
        if (is_palindrome(row, i)) {
            result.insert(i);
        }
    }
    return result;
}

void intersect(unordered_set<int>& intersection, const unordered_set<int>& palindrome_indices) {
    vector<int> to_remove;
    for (int candidate : intersection) {
        if (palindrome_indices.count(candidate) == 0) {
            to_remove.push_back(candidate);
        }
    }
    for (int elem : to_remove) {
        intersection.erase(elem);
    }
}

vector<vector<char>> transpose(const vector<vector<char>>& field) {
    size_t n = field.size(), m = field.front().size();
    vector<vector<char>> result(m, vector<char>(n, 0));
    for (size_t i = 0; i < n; i++) {
        for (size_t j = 0; j < m; j++) {
            result[j][i] = field[i][j];
        }
    }
    return result;
}

int solve(const vector<vector<char>>& field) {
    unordered_set<int> intersection;
    for (int i = 0, ei = int(field.front().size()); i < ei; i++) {
        intersection.insert(i);
    }
    for (const auto& row : field) {
        auto palindrome_indices = get_palindrome_indices(row);
        intersect(intersection, palindrome_indices);
    }
    return intersection.empty() ? 0 : *intersection.begin() + 1;
}

int solve_part_one(const vector<vector<char>>& field) {
    int vertical = solve(field);
    int horizontal = solve(transpose(field));
    return vertical + 100 * horizontal;
}

int main(int argc, char** argv) {
    ifstream ifs(argv[1]);
    string line;
    vector<vector<char>> field;
    int ans_part_one = 0;
    auto calc = [&field, &ans_part_one]() {
        ans_part_one += solve_part_one(field);
        field.clear();
    };
    while (getline(ifs, line)) {
        if (line.empty()) {
            calc();
        } else {
            vector<char> vec(begin(line), end(line));
            field.push_back(vec);
        }
    }
    calc();
    cout << ans_part_one << endl;
    return 0;
}
