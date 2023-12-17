#include <iostream>
#include <fstream>
#include <vector>

using namespace std;

void print_grid(const vector<string>& grid) {
    for (const auto& row : grid) {
        cout << row << endl;
    }
}

int solve_part_one(const vector<string>& grid) {
    print_grid(grid);
    return -1;
}

int main(int argc, char** argv) {
    ifstream ifs(argv[1]);
    string line;
    vector<string> grid;
    while (getline(ifs, line)) {
        grid.push_back(line);
    }
    cout << solve_part_one(grid) << endl;
    return 0;
}
