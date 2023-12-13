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

unordered_set<int> solve(const vector<vector<char>>& field) {
    unordered_set<int> intersection;
    for (int i = 0, ei = int(field.front().size()); i < ei; i++) {
        intersection.insert(i);
    }
    for (const auto& row : field) {
        auto palindrome_indices = get_palindrome_indices(row);
        intersect(intersection, palindrome_indices);
    }
    return intersection;
}

pair<int, int> solve_part_one(const vector<vector<char>>& field, int last_vertical = 0, int last_horizontal = 0) {
    auto intersection = solve(field);
    intersection.erase(last_vertical - 1);
    int vertical = intersection.empty() ? 0 : *intersection.begin() + 1;
    intersection = solve(transpose(field));
    intersection.erase(last_horizontal - 1);
    int horizontal = intersection.empty() ? 0 : *intersection.begin() + 1;
    return make_pair(vertical, horizontal);
}

char reverse(char c) {
    return c == '#' ? '.' : '#';
}

int solve_part_two(vector<vector<char>>& field, int last_vertical = 0, int last_horizontal = 0) {
    size_t n = field.size(), m = field.front().size();
    for (size_t i = 0; i < n; i++) {
        for (size_t j = 0; j < m; j++) {
            field[i][j] = reverse(field[i][j]);
            auto [vertical, horizontal] = solve_part_one(field, last_vertical, last_horizontal);
            if (vertical != 0) return vertical;
            if (horizontal != 0) return 100 * horizontal;
            field[i][j] = reverse(field[i][j]);
        }
    }
    cout << "SHOULD_NOT_REACH_HERE" << endl;
    return -1;
}

int main(int argc, char** argv) {
    ifstream ifs(argv[1]);
    string line;
    vector<vector<char>> field;
    int ans_part_one = 0, ans_part_two = 0;
    auto calc = [&field, &ans_part_one, &ans_part_two]() {
        auto [vertical, horizontal] = solve_part_one(field);
        ans_part_one += vertical + 100 * horizontal;
        ans_part_two += solve_part_two(field, vertical, horizontal);
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
    cout << ans_part_two << endl;
    return 0;
}
