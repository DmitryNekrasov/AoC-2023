#include <iostream>
#include <fstream>
#include <vector>

using namespace std;

void print_field(const vector<string>& field) {
    for (const auto& line : field) {
        cout << line << endl;
    }
}

int solve_port_one(const vector<string>& field) {
    print_field(field);
    return -1;
}

int main(int argc, char** argv) {
    ifstream ifs(argv[1]);
    string line;
    vector<string> field;
    while (getline(ifs, line)) {
        field.push_back(line);
    }
    int ans_part_one = solve_port_one(field);
    cout << ans_part_one << endl;
    return 0;
}
