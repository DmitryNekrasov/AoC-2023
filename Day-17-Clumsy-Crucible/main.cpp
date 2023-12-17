#include <iostream>
#include <fstream>
#include <vector>
#include <queue>
#include <unordered_set>
#include <limits>

using namespace std;

static constexpr int RIGHT = 0;
static constexpr int DOWN = 1;
static constexpr int LEFT = 2;
static constexpr int UP = 3;
static constexpr int DIRECTION_NUMBER = 4;

int first_adjacent(int direction) {
    return (direction + 1) % DIRECTION_NUMBER;
}

int second_adjacent(int direction) {
    return (direction + 3) % DIRECTION_NUMBER;
}

struct Vertex {
    int i, j, direction, straight_line_len, weight;

    Vertex(int _i, int _j, int _direction, int _straight_line_len, int _weight) :
        i(_i), j(_j), direction(_direction), straight_line_len(_straight_line_len), weight(_weight) {}

    bool operator<(const Vertex& o) const {
        return o.weight < weight;
    }
};

bool operator==(const Vertex& lhs, const Vertex& rhs) {
    return lhs.i == rhs.i && lhs.j == rhs.j &&
           lhs.direction == rhs.direction && lhs.straight_line_len == rhs.straight_line_len;
}

struct VertexHash {
    size_t operator()(const Vertex& v) const {
        size_t h = 0;
        for (int value : {v.i, v.j, v.direction, v.straight_line_len}) {
            h += (h * 31) + value;
        }
        return h;
    }
};

void print_grid(const vector<string>& grid) {
    for (const auto& row : grid) {
        cout << row << endl;
    }
}

int solve(const vector<string>& grid, int start_direction, int line_min_limit, int line_max_limit) {
    const size_t n = grid.size(), m = grid.front().length();
    unordered_set<Vertex, VertexHash> visited;
    priority_queue<Vertex> q;
    Vertex start_v(0, 0, start_direction, 0, 0);
    visited.insert(start_v);
    q.push(start_v);
    int result = numeric_limits<int>::max();
    auto try_move = [n, m, &grid, &visited, &q, line_min_limit, line_max_limit](const Vertex& v, int next_direction) {
        auto [i, j, direction, straight_line_len, weight] = v;
        int next_i = i + (next_direction == DOWN ? 1 : (next_direction == UP ? -1 : 0));
        int next_j = j + (next_direction == RIGHT ? 1 : (next_direction == LEFT ? -1 : 0));
        if (next_i >= 0 && next_i < n && next_j >= 0 && next_j < m) {
            if (straight_line_len < line_max_limit && next_direction == direction ||
                straight_line_len >= line_min_limit &&
                    (next_direction == first_adjacent(direction) || next_direction == second_adjacent(direction))
            ) {
                Vertex candidate(next_i, next_j, next_direction,
                                 direction == next_direction ? straight_line_len + 1 : 1,
                                 weight + grid[next_i][next_j] - '0');
                if (visited.count(candidate) == 0) {
                    visited.insert(candidate);
                    q.push(candidate);
                }
            }
        }
    };
    while (!q.empty()) {
        auto v = q.top();
        if (v.i == n - 1 && v.j == m - 1 && v.straight_line_len >= line_min_limit) {
            result = min(result, v.weight);
        }
        q.pop();
        for (int direction = 0; direction < DIRECTION_NUMBER; direction++) {
            try_move(v, direction);
        }
    }
    return result;
}

int solve(const vector<string>& grid, int line_min_limit, int line_max_limit) {
    return min(solve(grid, RIGHT, line_min_limit, line_max_limit),
               solve(grid, DOWN, line_min_limit, line_max_limit));
}

int solve_part_one(const vector<string>& grid) {
    return solve(grid, 0, 3);
}

int solve_part_two(const vector<string>& grid) {
    return solve(grid, 4, 10);
}

int main(int argc, char** argv) {
    ifstream ifs(argv[1]);
    string line;
    vector<string> grid;
    while (getline(ifs, line)) {
        grid.push_back(line);
    }
    cout << solve_part_one(grid) << endl;
    cout << solve_part_two(grid) << endl;
    return 0;
}
