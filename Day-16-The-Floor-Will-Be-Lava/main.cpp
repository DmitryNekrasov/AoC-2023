#include <iostream>
#include <fstream>
#include <vector>
#include <queue>

using namespace std;

static constexpr int RIGHT = 1;
static constexpr int DOWN = 2;
static constexpr int LEFT = 4;
static constexpr int UP = 8;

void print_field(const vector<string>& field) {
    for (const auto& line : field) {
        cout << line << endl;
    }
}

int energized_number(const vector<vector<int>>& visited) {
    int result = 0;
    for (const auto& row : visited) {
        for (int value : row) {
            result += value != 0;
        }
    }
    return result;
}

int solve_part_one(const vector<string>& field, int start_i = 0, int start_j = 0, int start_direction = RIGHT) {
    size_t n = field.size(), m = field.front().length();
    vector<vector<int>> visited(n, vector<int>(m, 0));
    queue<tuple<int, int, int>> q;
    q.emplace(start_i, start_j, start_direction);
    while (!q.empty()) {
        auto [i, j, direction] = q.front();
        q.pop();
        if (i < 0 || i >= n || j < 0 || j >= m) continue;
        if ((visited[i][j] & direction) != 0) continue;
        visited[i][j] |= direction;
        switch (field[i][j]) {
            case '/':
                switch (direction) {
                    case RIGHT: q.emplace(i - 1, j, UP); break;
                    case DOWN: q.emplace(i, j - 1, LEFT); break;
                    case LEFT: q.emplace(i + 1, j, DOWN); break;
                    case UP: q.emplace(i, j + 1, RIGHT); break;
                    default: cout << "SHOULD_NOT_REACH_HERE" << endl;
                }
                break;
            case '\\':
                switch (direction) {
                    case RIGHT: q.emplace(i + 1, j, DOWN); break;
                    case DOWN: q.emplace(i, j + 1, RIGHT); break;
                    case LEFT: q.emplace(i - 1, j, UP); break;
                    case UP: q.emplace(i, j - 1, LEFT); break;
                    default: cout << "SHOULD_NOT_REACH_HERE" << endl;
                }
                break;
            case '-':
                switch (direction) {
                    case DOWN:
                    case UP: q.emplace(i, j - 1, LEFT); q.emplace(i, j + 1, RIGHT); break;
                    case RIGHT: q.emplace(i, j + 1, RIGHT); break;
                    case LEFT: q.emplace(i, j - 1, LEFT); break;
                    default: cout << "SHOULD_NOT_REACH_HERE" << endl;
                }
                break;
            case '|':
                switch (direction) {
                    case RIGHT:
                    case LEFT: q.emplace(i - 1, j, UP); q.emplace(i + 1, j, DOWN); break;
                    case DOWN: q.emplace(i + 1, j, DOWN); break;
                    case UP: q.emplace(i - 1, j, UP); break;
                    default: cout << "SHOULD_NOT_REACH_HERE" << endl;
                }
                break;
            case '.':
                switch (direction) {
                    case RIGHT: q.emplace(i, j + 1, RIGHT); break;
                    case DOWN: q.emplace(i + 1, j, DOWN); break;
                    case LEFT: q.emplace(i, j - 1, LEFT); break;
                    case UP: q.emplace(i - 1, j, UP); break;
                    default: cout << "SHOULD_NOT_REACH_HERE" << endl;
                }
                break;
            default: cout << "SHOULD_NOT_REACH_HERE" << endl;
        }
    }
    return energized_number(visited);
}

int solve_part_two(const vector<string>& field) {
    size_t n = field.size(), m = field.front().length();
    int result = 0;
    for (size_t i = 0; i < n; i++) {
        result = max(result, max(solve_part_one(field, i, 0), solve_part_one(field, i, m - 1, LEFT)));
    }
    for (size_t j = 0; j < m; j++) {
        result = max(result, max(solve_part_one(field, 0, j, DOWN), solve_part_one(field, n - 1, j, UP)));
    }
    return result;
}

int main(int argc, char** argv) {
    ifstream ifs(argv[1]);
    string line;
    vector<string> field;
    while (getline(ifs, line)) {
        field.push_back(line);
    }
    cout << solve_part_one(field) << endl;
    cout << solve_part_two(field) << endl;
    return 0;
}
