#include <iostream>
#include <fstream>
#include <vector>

using namespace std;

void solve(const vector<string>& field) {
    for (const auto& row : field) {
        cout << row << endl;
    }
    cout << endl;
}

int main(int argc, char** argv) {
    ifstream ifs(argv[1]);
    string line;
    vector<string> field;
    while (getline(ifs, line)) {
        if (line.empty()) {
            solve(field);
            field.clear();
        } else {
            field.push_back(line);
        }
    }
    solve(field);

    return 0;
}
